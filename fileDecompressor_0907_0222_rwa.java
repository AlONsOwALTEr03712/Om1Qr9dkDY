// 代码生成时间: 2025-09-07 02:22:07
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.stream.Collectors;

public class FileDecompressor {

    private static void decompressZipFile(String zipFilePath, String outputDirectory) throws IOException {
        // Ensure the output directory exists
        Path outputDir = Paths.get(outputDirectory);
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        // Open the zip file
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            // Get all the entries
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                Path outputPath = outputDir.resolve(entry.getName());
                // Create directories as needed
                Files.createDirectories(outputPath.getParent());
                if (!entry.isDirectory()) {
                    // Write file content to output directory
                    Files.copy(zipFile.getInputStream(entry), outputPath);
                }
            }
        }
    }

    public static void main(String[] args) {
        // Initialize Spark session and Spark context
        SparkSession spark = SparkSession.builder()
                .appName("File Decompressor")
                .getOrCreate();
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        try {
            // Check if the program is given the correct number of arguments
            if (args.length != 2) {
                System.err.println("Usage: FileDecompressor <zip-file-path> <output-directory>");
                return;
            }

            String zipFilePath = args[0];
            String outputDirectory = args[1];

            // Decompress the zip file
            decompressZipFile(zipFilePath, outputDirectory);

            System.out.println("Decompression completed successfully.");

        } catch (IOException e) {
            System.err.println("Error during decompression: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Stop the Spark context
            sc.stop();
            spark.stop();
        }
    }
}
