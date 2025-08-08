// 代码生成时间: 2025-08-09 04:07:48
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TestReportGenerator {

    /**
     * Main method to run the test report generator.
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("TestReportGenerator").getOrCreate();

        try {
            // Load the test results data from a CSV file
            Dataset<Row> testResults = spark
                .read()
                .option("header", true)
                .option("inferSchema", true)
                .csv("test_results.csv");

            // Generate the test report
            String report = generateTestReport(testResults);

            // Save the report to a file
            saveReportToFile(report, "test_report.html");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Stop the Spark session
            spark.stop();
        }
    }

    /**
     * Generate a test report from the test results data.
     * @param testResults The dataset containing test results
     * @return The generated test report as a String
     */
    public static String generateTestReport(Dataset<Row> testResults) {
        // Calculate the total number of tests and the number of failed tests
        long totalTests = testResults.count();
        long failedTests = testResults.filter(row -> row.getAs("status").equals("FAILED\)).count();

        // Build the report content
        String reportContent = "<!DOCTYPE html>\
<html>\
<head>\
<title>Test Report</title>\
</head>\
<body>\
<h1>Test Report</h1>\
<p>Total tests: " + totalTests + "</p>\
<p>Failed tests: " + failedTests + "</p>\
<table border='1'>\
<tr><th>Test Name</th><th>Status</th></tr>\
";

        // Add each test result to the report
        for (Row row : testResults.collect()) {
            reportContent += "<tr>\
<td>" + row.getAs("test_name") + "</td>\
<td>" + row.getAs("status") + "</td>\
</tr>\
";
        }

        reportContent += "</table>\
</body>\
</html>";

        return reportContent;
    }

    /**
     * Save the generated report to a file.
     * @param report The report content as a String
     * @param filename The name of the file to save the report to
     */
    public static void saveReportToFile(String report, String filename) {
        try (PrintWriter out = new PrintWriter(filename, "UTF-8")) {
            out.println(report);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
