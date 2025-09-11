// 代码生成时间: 2025-09-11 17:48:09
import static spark.Spark.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

public class NetworkStatusChecker {

    /*
    * Main method to start the Spark web server.
    */
    public static void main(String[] args) {
        // Set the port number for the Spark server
        port(4567);

        // Define the endpoint to check network status
        get("/check-status", (req, res) -> checkNetworkStatus(), "application/json");
    }

    /*
    * Method to check the network status by attempting to connect to a URL.
    *
    * @return A JSON object indicating the network status.
    */
    private static String checkNetworkStatus() {
        String url = "http://www.google.com"; // URL to check for network connectivity
        HttpURLConnection conn = null;
        try {
            URL checkUrl = new URL(url);
            conn = (HttpURLConnection) checkUrl.openConnection();

            // Set the request method and timeout values
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // 5 seconds timeout
            conn.setReadTimeout(5000); // 5 seconds timeout

            // Check the response code from the server
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "{"status": "connected"}";
            } else {
                return "{"status": "disconnected"}";
            }
        } catch (IOException e) {
            return "{"status": "disconnected", "error": """ + e.getMessage() + """}";
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
