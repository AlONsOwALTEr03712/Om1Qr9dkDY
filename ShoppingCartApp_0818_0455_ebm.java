// 代码生成时间: 2025-08-18 04:55:58
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ShoppingCartApp demonstrates the functionality of a shopping cart using Apache Spark.
 * It processes a dataset of purchases and calculates the cart details.
 */
public class ShoppingCartApp {

    public static void main(String[] args) {
        // Initialize Spark session
        SparkSession spark = SparkSession.builder()
                .appName("ShoppingCartApp")
                .master("local[*]")
                .getOrCreate();

        // Create Java Spark Context
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        // Sample data of purchases
        JavaRDD<String> purchases = sc.parallelize(Arrays.asList(
                "Alice,Book,3",
                "Bob,Toy,1",
                "Alice,Toy,2",
                "Charlie,Book,4",
                "Bob,Book,2"
        ));

        // Define a schema for the purchase data
        List<String> fields = Arrays.asList("customer", "item", "quantity");
        JavaRDD<Row> purchaseRows = purchases.map(s -> {
            String[] tokens = s.split(",");
            Row row = RowFactory.create(tokens[0], tokens[1], Integer.parseInt(tokens[2]));
            return row;
        });

        // Convert JavaRDD to Dataset
        Dataset<Row> purchaseDF = spark.createDataFrame(purchaseRows, Row.class);
        purchaseDF.createOrReplaceTempView("purchases");

        // Calculate cart details
        Dataset<Row> cartDetails = spark.sql(
                "SELECT customer, item, SUM(quantity) as total_quantity" +
                " FROM purchases" +
                " GROUP BY customer, item"
        );

        // Show the results
        cartDetails.show();

        // Stop the Spark session
        spark.stop();
    }

    // Helper method to create a SparkSession
    private static SparkSession createSparkSession() {
        return SparkSession.builder()
                .appName("ShoppingCartApp")
                .master("local[*]")
                .getOrCreate();
    }
}
