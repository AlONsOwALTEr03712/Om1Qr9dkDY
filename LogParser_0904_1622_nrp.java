// 代码生成时间: 2025-09-04 16:22:31
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class LogParser {

    public static void main(String[] args) {
        // 检查命令行参数
        if (args.length < 1) {
            System.err.println("Usage: LogParser <log file path> <output path>");
            System.exit(1);
        }

        String logFilePath = args[0];
        String outputFilePath = args[1];

        // 初始化SparkSession
        SparkSession spark = SparkSession
                .builder()
                .appName("Log Parser")
                .master("local[*]")
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        try {
            // 读取日志文件
            JavaRDD<String> logData = sc.textFile(logFilePath);

            // 解析日志文件
            JavaRDD<String> parsedLogs = parseLogs(logData);

            // 保存到文件系统
            parsedLogs.saveAsTextFile(outputFilePath);
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 关闭SparkContext
            sc.close();
        }
    }

    /**
     * 解析日志记录的方法
     *
     * @param logData 包含日志记录的JavaRDD
     * @return 一个包含解析后的日志记录的JavaRDD
     */
    private static JavaRDD<String> parseLogs(JavaRDD<String> logData) {
        // 定义解析函数
        return logData
                .flatMap(line -> Arrays.asList(line.split("\s+")).iterator())
                .map(token -> token + ",");
    }
}
