// 代码生成时间: 2025-08-02 21:12:53
import static spark.Spark.*;

public class MathematicalCalculationTool {

    /**
     * Main method to start the Spark web server.
     */
    public static void main(String[] args) {
        // Define the port for the Spark web server.
        port(8080);

        // Route for GET request to the root path, which displays a welcome message.
        get("/", (req, res) -> "Welcome to the Mathematical Calculation Toolkit!");

        // Define a POST request handler to perform addition.
        post("/add", (req, res) -> {
            String num1 = req.queryParams("num1");
            String num2 = req.queryParams("num2");
            try {
                double result = add(Double.parseDouble(num1), Double.parseDouble(num2));
                return "Addition Result: " + result;
            } catch (Exception e) {
                return "Error: Invalid input. Please provide valid numbers.";
            }
        });

        // Define a POST request handler to perform subtraction.
        post("/subtract", (req, res) -> {
            String num1 = req.queryParams("num1");
            String num2 = req.queryParams("num2");
            try {
                double result = subtract(Double.parseDouble(num1), Double.parseDouble(num2));
                return "Subtraction Result: " + result;
            } catch (Exception e) {
                return "Error: Invalid input. Please provide valid numbers.";
            }
        });

        // Define a POST request handler to perform multiplication.
        post("/multiply", (req, res) -> {
            String num1 = req.queryParams("num1");
            String num2 = req.queryParams("num2");
            try {
                double result = multiply(Double.parseDouble(num1), Double.parseDouble(num2));
                return "Multiplication Result: " + result;
            } catch (Exception e) {
                return "Error: Invalid input. Please provide valid numbers.";
            }
        });

        // Define a POST request handler to perform division.
        post("/divide", (req, res) -> {
            String num1 = req.queryParams("num1");
            String num2 = req.queryParams("num2");
            try {
                double result = divide(Double.parseDouble(num1), Double.parseDouble(num2));
                return "Division Result: " + result;
            } catch (Exception e) {
                return "Error: Invalid input. Please provide valid numbers.";
            }
        });

        // Define a POST request handler to perform power operation.
        post("/power", (req, res) -> {
            String base = req.queryParams("base");
            String exponent = req.queryParams("exponent");
            try {
                double result = power(Double.parseDouble(base), Double.parseDouble(exponent));
                return "Power Result: " + result;
            } catch (Exception e) {
                return "Error: Invalid input. Please provide valid numbers.";
            }
        });

    }

    /**
     * Method to perform addition.
     *
     * @param num1 First number
     * @param num2 Second number
     * @return The sum of the two numbers
     */
    private static double add(double num1, double num2) {
        return num1 + num2;
    }

    /**
     * Method to perform subtraction.
     *
     * @param num1 First number
     * @param num2 Second number
     * @return The difference of the two numbers
     */
    private static double subtract(double num1, double num2) {
        return num1 - num2;
    }

    /**
     * Method to perform multiplication.
     *
     * @param num1 First number
     * @param num2 Second number
     * @return The product of the two numbers
     */
    private static double multiply(double num1, double num2) {
        return num1 * num2;
    }

    /**
     * Method to perform division.
     *
     * @param num1 First number
     * @param num2 Second number
     * @return The quotient of the two numbers
     */
    private static double divide(double num1, double num2) {
        if (num2 == 0) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        return num1 / num2;
    }

    /**
     * Method to perform power operation.
     *
     * @param base Base number
     * @param exponent Exponent
     * @return The result of raising the base to the power of the exponent
     */
    private static double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

}