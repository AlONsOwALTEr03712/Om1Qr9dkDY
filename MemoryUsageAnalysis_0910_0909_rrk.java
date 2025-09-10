// 代码生成时间: 2025-09-10 09:09:30
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryUsageAnalysis {
    // Main method to run the program
    public static void main(String[] args) {
        try {
            // Configure Spark session
            SparkConf conf = new SparkConf().setAppName("MemoryUsageAnalysis");
            JavaSparkContext sc = new JavaSparkContext(conf);
            SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

            // Perform memory usage analysis
            analyzeMemoryUsage(sc, spark);

            // Stop Spark context
            sc.stop();
            spark.stop();
        } catch (Exception e) {
            System.err.println("Error in MemoryUsageAnalysis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Analyze memory usage and display relevant metrics.
     *
     * @param sc JavaSparkContext
     * @param spark SparkSession
     */
    public static void analyzeMemoryUsage(JavaSparkContext sc, SparkSession spark) {
        // Get memory MX bean to monitor memory usage
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

        // Create a dataset to store memory usage data
        Dataset<Row> memoryUsageData = spark.createDataFrame(
                DataUtil.createMemoryUsageRows(heapMemoryUsage, nonHeapMemoryUsage),
                DataUtil.MEMORY_USAGE_SCHEMA
        );

        // Show memory usage data
        memoryUsageData.show();
    }

    // Helper class to hold memory usage data and schema
    static class DataUtil {
        static final String MEMORY_USAGE_SCHEMA = "struct<" +
                "heapUsed:long,heapMax:long,heapInit:long,heapCommited:long," +
                "nonHeapUsed:long,nonHeapMax:long,nonHeapInit:long,nonHeapCommited:long>;";

        static Dataset<Row> createMemoryUsageRows(MemoryUsage heapMemoryUsage, MemoryUsage nonHeapMemoryUsage) {
            return spark.createDataFrame(Arrays.asList(
                new Row(heapMemoryUsage.getUsed(), heapMemoryUsage.getMax(), heapMemoryUsage.getInit(), heapMemoryUsage.getCommitted(),
                    nonHeapMemoryUsage.getUsed(), nonHeapMemoryUsage.getMax(), nonHeapMemoryUsage.getInit(), nonHeapMemoryUsage.getCommitted())
            ), MEMORY_USAGE_SCHEMA);
        }
    }
}
