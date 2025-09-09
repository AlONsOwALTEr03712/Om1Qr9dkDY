// 代码生成时间: 2025-09-09 09:48:10
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class ApiResponseFormatter {

    /**
     * Main method to run the application
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName(