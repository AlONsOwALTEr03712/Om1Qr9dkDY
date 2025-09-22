// 代码生成时间: 2025-09-22 15:02:32
import static spark.Spark.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlValidatorSpark {
    /**
     * Main method to start the Spark application.
     * @param args - command line arguments (if any)
     */
    public static void main(String[] args) {
        port(4567); // Set the port on which Spark will listen
        get("/validate", "application/json", (request, response) -> {
            String url = request.queryParams("url"); // Get the URL parameter from the query string
            return validateUrl(url); // Validate the URL and return the result
        }, new JsonTransformer());
    }

    /**
     * Validates the provided URL.
     * @param urlString - The URL to be validated.
     * @return A JSON object indicating whether the URL is valid or not.
     */
    private static String validateUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000); // 5 seconds timeout
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "{"valid": true, "message": "URL is valid."}";
            } else {
                return "{"valid": false, "message": "URL is not valid. Status code: " + responseCode + ""}";
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during the URL validation process.
            return "{"valid": false, "message": "An error occurred: " + e.getMessage() + ""}";
        }
    }
}
