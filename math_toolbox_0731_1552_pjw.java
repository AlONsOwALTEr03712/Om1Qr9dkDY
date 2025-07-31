// 代码生成时间: 2025-07-31 15:52:09
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

public class MathToolbox {
    
    private SparkSession spark;
    
    // Constructor to initialize SparkSession
    public MathToolbox(String appName) {
        spark = SparkSession
                .builder()
                .appName(appName)
                .master("local[*]")
                .getOrCreate();
    }
    
    // Method to perform addition
    public double add(double a, double b) {
        return a + b;
    }
    
    // Method to perform subtraction
    public double subtract(double a, double b) {
        return a - b;
    }
    
    // Method to perform multiplication
    public double multiply(double a, double b) {
        return a * b;
    }
    
    // Method to perform division
    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return a / b;
    }
    
    // Method to perform power operation
    public double power(double a, double b) {
        return Math.pow(a, b);
    }
    
    // Utility method to create a dataset of mathematical operations
    public Dataset<Row> createOperationsDataset() {
        Dataset<Row> operations = spark.createDataFrame(
            java.util.Arrays.asList(
                new String[]{