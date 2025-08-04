// 代码生成时间: 2025-08-05 02:34:11
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * NetworkConnectionChecker is a Spark program that checks the connection status of specified URLs.
 */
public class NetworkConnectionChecker {

    private static final List<String> URLs = Arrays.asList(
        "http://www.google.com",
        "http://www.example.com",
        "http://www.nonexistentwebsite1234.com"
    ); // List of URLs to check

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("NetworkConnectionChecker");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        // Parallelize the URL list and map each URL to its connection status
        Dataset<Row> connectionStatuses = spark.sparkContext().parallelize(URLs)
                .map(url -> checkUrl(url))
                .toDF("url", "status");

        // Show the connection status of each URL
        connectionStatuses.show();
    }

    private static String checkUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("HEAD");
            httpURLConnection.setConnectTimeout(5000); // 5 seconds timeout
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            // Check if the response code indicates a successful connection
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return urlStr + " is reachable with status code: " + responseCode;
            } else {
                return urlStr + " failed to connect with status code: " + responseCode;
            }
        } catch (ConnectException e) {
            return urlStr + " cannot be reached. ConnectException: " + e.getMessage();
        } catch (Exception e) {
            return urlStr + " connection check failed with exception: " + e.getMessage();
        }
    }
}
