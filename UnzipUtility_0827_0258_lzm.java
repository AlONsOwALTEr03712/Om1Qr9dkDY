// 代码生成时间: 2025-08-27 02:58:05
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Utility class to unzip files using the Apache Commons Compress library.
 * This class is designed to be easily understandable and maintainable,
 * following best practices for Java development.
 */
public class UnzipUtility {

    /**
     * Unzips a zip file to the specified destination directory.
     *
     * @param zipFilePath The path to the zip file to be unzipped.
     * @param destDirectory The destination directory where the files will be extracted.
     * @throws IOException If an I/O error occurs.
     * @throws ArchiveException If an archive error occurs.
     */
    public void unzipFile(String zipFilePath, String destDirectory) throws IOException, ArchiveException {
        // Ensure the destination directory exists
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            if (!destDir.mkdirs()) {
                throw new IOException("Failed to create destination directory.");
            }
        }

        // Open the zip file and read its contents
        try (ZipArchiveInputStream zipIn = new ZipArchiveInputStream(Files.newInputStream(Paths.get(zipFilePath)))) {
            ArchiveEntry entry = zipIn.getNextEntry();
            // Process each entry in the zip file
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // If the entry is a file, extract it
                    extractFile(zipIn, filePath);
                } else {
                    // If the entry is a directory, create it
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    /**
     * Extracts a single file from the zip input stream to the specified location.
     *
     * @param zipIn The input stream of the zip file.
     * @param filePath The path where the file will be extracted.
     * @throws IOException If an I/O error occurs.
     */
    private void extractFile(ZipArchiveInputStream zipIn, String filePath) throws IOException {
        try (OutputStream out = new FileOutputStream(filePath)) {
            // Copy the file from the zip input stream to the output stream
            IOUtils.copy(zipIn, out);
        }
    }

    /**
     * Main method for testing the UnzipUtility class.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java UnzipUtility <zip file path> <destination directory>");
            System.exit(1);
        }

        UnzipUtility unzipper = new UnzipUtility();
        try {
            unzipper.unzipFile(args[0], args[1]);
            System.out.println("Unzipping completed successfully.");
        } catch (IOException | ArchiveException e) {
            System.err.println("Error during unzipping: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
