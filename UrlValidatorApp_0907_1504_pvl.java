// 代码生成时间: 2025-09-07 15:04:01
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Arrays;

public class UrlValidatorApp {

    // The main method to run the application
    public static void main(String[] args) {
        // Initialize Spark session
        SparkSession spark = SparkSession.builder()
                .appName("UrlValidatorApp")
                .master("local[*]")
                .getOrCreate();

        // Check if input arguments are provided
        if (args.length != 1) {
            System.err.println("Usage: UrlValidatorApp <path-to-url-file>");
            System.exit(1);
        }

        // Load URLs from a text file
        String filePath = args[0];
        Dataset<Row> urlDf = spark.read().textFile(filePath);

        // Define a schema for the output DataFrame
        types.StructType schema = new types.StructType()
                .add("url", types.StringType, false)
                .add("is_valid", types.BooleanType, false);

        // Transform URLs to a DataFrame with validity check
        Dataset<Row> validUrlsDf = urlDf.map(row -> {
            String url = row.getString(0);
            boolean isValid = isValidUrl(url);
            return RowFactory.create(url, isValid);
        }, RowEncoder.apply(schema));

        // Show valid URLs
        validUrlsDf.show();

        // Stop the Spark session
        spark.stop();
    }

    // Helper method to validate the URL
    private static boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | IllegalArgumentException e) {
            return false;
        }
    }
}
