// 代码生成时间: 2025-08-29 19:50:41
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

// 订单类，用于表示一个订单
class Order implements Serializable {
    private String orderId;
    private double amount;
    private String status;

    public Order(String orderId, double amount, String status) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
    }

    // Getter和Setter方法
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

public class OrderProcessingApp {

    // 程序入口点
    public static void main(String[] args) {
        SparkSession spark = SparkSession
            .builder()
            .appName("Order Processing")
            .master("local[*]")
            .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        // 假设有一组初始订单数据
        List<Order> initialOrders = Arrays.asList(
            new Order("001", 100.0, "PENDING"),
            new Order("002", 200.0, "SHIPPED"),
            new Order("003", 150.0, "COMPLETED")
        );

        JavaRDD<Order> ordersRDD = sc.parallelize(initialOrders);

        // 处理订单
        Dataset<Row> processedOrders = processOrders(ordersRDD, spark);

        // 打印处理结果
        processedOrders.show();

        sc.close();
    }

    // 订单处理函数，返回处理后的订单数据集
    public static Dataset<Row> processOrders(JavaRDD<Order> orders, SparkSession spark) {
        // 将RDD转换为DataFrame
        Dataset<Row> ordersDF = spark.createDataFrame(orders.rdd(), Order.class);

        // 过滤出状态为PENDING的订单
        Dataset<Row> pendingOrders = ordersDF.filter(functions.col("status").equalTo("PENDING"));

        // 更新PENDING订单的状态为SHIPPED
        Dataset<Row> shippedOrders = pendingOrders.withColumn("status", functions.lit("SHIPPED"));

        // 合并SHIPPED订单和已完成订单
        Dataset<Row> finalOrders = ordersDF.unionByName(shippedOrders);

        return finalOrders;
    }
}
