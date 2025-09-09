// 代码生成时间: 2025-09-09 16:34:36
import static spark.Spark.get;
import static spark.Spark.post;
import spark.template.freemarker.FreeMarkerEngine;
import com.google.common.html.HtmlEscapers;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import java.nio.charset.StandardCharsets;

public class XSSProtectionApp {

    // Define the port number
    private static final int PORT = 4567;

    public static void main(String[] args) {
        // Configure FreeMarker engine
        Configuration fmConfig = new Configuration(Configuration.VERSION_2_3_31);
        fmConfig.setDefaultEncoding(StandardCharsets.UTF_8.name());
        fmConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        fmConfig.setLogTemplateExceptions(false);
        get("/", (req, res) -> "<form action='/submit' method='post'><input type='text' name='userInput'><button type='submit'>Submit</button></form>", new FreeMarkerEngine(fmConfig));

        post("/submit", (req, res) -> {
            // Get user input and sanitize it for XSS protection
            String userInput = req.queryParams("userInput");
            String sanitizedInput = HtmlEscapers.htmlEscaper().escape(userInput);
            
            // Render the sanitized input in the response
            return "<p>You entered: " + sanitizedInput + "</p>";
        }, new FreeMarkerEngine(fmConfig));

        // Start the Spark server
        get("/hello", (req, res) -> "Hello World!");
        System.out.println("Spark server initiated on port " + PORT);
        get.port(PORT);
    }
}
