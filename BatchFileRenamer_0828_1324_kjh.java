// 代码生成时间: 2025-08-28 13:24:17
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class BatchFileRenamer {

    // The path to the directory containing files to be renamed.
    private static final String INPUT_DIR = "/path/to/input/directory";
    // The path to the directory where renamed files will be saved.
    private static final String OUTPUT_DIR = "/path/to/output/directory";

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("BatchFileRenamer");
        JavaSparkContext sc = new JavaSparkContext(conf);

        try {
            // Get the list of files to be renamed.
            JavaRDD<String> files = sc.textFile(INPUT_DIR).map(line -> new File(INPUT_DIR, line).getAbsolutePath());

            // Rename each file using a parallel operation.
            files.foreach(file -> renameFile(file));

            // Stop the Spark context.
            sc.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Renames a single file by appending a timestamp to its name.
     *
     * @param filePath The path to the file to be renamed.
     */
    private static void renameFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File does not exist: " + filePath);
            return;
        }

        // Construct the output path by appending a timestamp to the original file name.
        String outputFileName = file.getName();
        String timestamp = Long.toString(System.currentTimeMillis());
        String outputFilePath = OUTPUT_DIR + File.separator + outputFileName + "_" + timestamp;

        // Rename the file.
        if (!file.renameTo(new File(outputFilePath))) {
            System.err.println("Failed to rename file: " + filePath);
        } else {
            System.out.println("File renamed: " + filePath + " to " + outputFilePath);
        }
    }
}
