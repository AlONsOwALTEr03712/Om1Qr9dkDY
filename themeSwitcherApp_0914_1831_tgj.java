// 代码生成时间: 2025-09-14 18:31:28
import static spark.Spark.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class ThemeSwitcherApp {

    // Define constants for themes
    private static final String DARK_THEME_NAME = "dark-theme";
    private static final String LIGHT_THEME_NAME = "light-theme";
    private static final String DEFAULT_THEME = DARK_THEME_NAME;

    // Initialize the Freemarker configuration
    private static final Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
    static {
        cfg.setClassForTemplateLoading(ThemeSwitcherApp.class, "/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    // Start the Spark server and set up the routes
    public static void main(String[] args) {
        // Set the port number
        port(8080);

        // Set up a session cookie for theme preference
        sessionCookie("theme");

        // Home page route
        get("/", (req, res) -> {
            String theme = req.queryParams("theme");
            if (theme == null) {
                theme = req.session().attribute("theme");
                if (theme == null) {
                    theme = DEFAULT_THEME;
                }
            }
            req.session().attribute("theme", theme);
            return renderTemplate("index.ftl", theme);
        });

        // Route for switching the theme
        get("/switch-theme", (req, res) -> {
            String currentTheme = req.session().attribute("theme");
            if (currentTheme == null || currentTheme.equals(DARK_THEME_NAME)) {
                req.session().attribute("theme", LIGHT_THEME_NAME);
            } else {
                req.session().attribute("theme", DARK_THEME_NAME);
            }
            res.redirect(request().pathInfo());
            return "";
        });

        // Error handling
        exception(Exception.class, (e, req, res) -> {
            res.type("text/plain");
            res.status(500);
            res.body("An error occurred: " + e.getMessage());
        });
    }

    // Helper method to render Freemarker templates
    private static String renderTemplate(String templateName, String theme) {
        try {
            Template template = cfg.getTemplate(templateName);
            StringWriter out = new StringWriter();
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("theme", theme);
            template.process(dataModel, out);
            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error rendering template: " + templateName, e);
        }
    }
}
