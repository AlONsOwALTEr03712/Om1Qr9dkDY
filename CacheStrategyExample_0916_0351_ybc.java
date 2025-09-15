// 代码生成时间: 2025-09-16 03:51:19
 * and follows Java best practices to ensure code maintainability
 * and scalability.
 */

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;

public class CacheStrategyExample {

    // Main entry point for the application
    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("Cache Strategy Example")
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        try {
            // Example data
            String[] data = new String[] {
                "1,John,Doe",
                "2,Jane,Doe",
                "3,John,Smith"
            };

            // Parallelizing the data and creating an RDD
            JavaRDD<String> peopleRDD = sc.parallelize(data);

            // Caching the RDD at the default storage level (MEMORY_ONLY)
            peopleRDD.cache();
            System.out.println("First action on cached RDD: " + peopleRDD.count());

            // Changing the storage level to MEMORY_AND_DISK
            peopleRDD.persist(StorageLevel.MEMORY_AND_DISK());
            System.out.println("After changing storage level: " + peopleRDD.count());

            // Converting RDD to a DataFrame for further operations
            Dataset<Row> peopleDF = spark.read().csv("people.csv").toDF("id", "first_name", "last_name");

            // Caching the DataFrame
            peopleDF.cache();
            System.out.println("First action on cached DataFrame: " + peopleDF.count());

            // Performing an action to see the effect of caching
            peopleDF.show();
            System.out.println("After performing an action: " + peopleDF.count());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Stopping the Spark context
            sc.stop();
        }
    }
}
