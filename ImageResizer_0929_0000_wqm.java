// 代码生成时间: 2025-09-29 00:00:57
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ImageResizer {

    private static final String INPUT_DIRECTORY = "input_images";
    private static final String OUTPUT_DIRECTORY = "resized_images";
    private static final int TARGET_WIDTH = 800;
    private static final int TARGET_HEIGHT = 600;

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("ImageResizer").getOrCreate();
        JavaSparkContext sc = new JavaSparkContext(spark);
        try {
            List<String> files = Arrays.asList(sc.wholeTextFiles(INPUT_DIRECTORY).toJavaRDD().flatMap(x -> Arrays.asList(x._2().split("
")).iterator()).distinct().collect(Collectors.toList()));
            Dataset<Row> images = spark.createDataFrame(files, String.class);
            images.foreachPartition(partition -> {
                try {
                    for (Iterator<Row> it = partition.toLocalIterator(); it.hasNext(); ) {
                        String imagePath = it.next().getString(0);
                        resizeImage(imagePath);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error resizing image", e);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }

    /**
     * Resizes an image to the target dimensions.
     * @param imagePath The path to the image file.
     * @throws IOException If an I/O error occurs.
     */
    private static void resizeImage(String imagePath) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        if (originalImage == null) {
            throw new IOException("Could not read image: " + imagePath);
        }

        // Create a new BufferedImage with the target dimensions.
        BufferedImage resizedImage = new BufferedImage(TARGET_WIDTH, TARGET_HEIGHT, originalImage.getType());

        // Draw the original image onto the resized image, scaling it to fit.
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, TARGET_WIDTH, TARGET_HEIGHT, null);
        graphics.dispose();

        // Save the resized image to the output directory.
        File outputDir = new File(OUTPUT_DIRECTORY);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File outputFile = new File(OUTPUT_DIRECTORY + File.separator + new File(imagePath).getName());
        ImageIO.write(resizedImage, "jpg", outputFile);
    }
}
