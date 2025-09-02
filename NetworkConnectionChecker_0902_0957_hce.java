// 代码生成时间: 2025-09-02 09:57:57
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;

public class NetworkConnectionChecker {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("NetworkConnectionChecker");
        JavaStreamingContext ssc = new JavaStreamingContext(conf, 1000);

        // Check for valid input arguments
        if (args.length < 2) {
            System.err.println("Usage: NetworkConnectionChecker <hostname> <port>");
            System.exit(1);
        }
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        // Create a DStream that connects to hostname:port, like target ip:port thrift server
        JavaDStream<String> lines = ssc.socketTextStream(hostname, port);

        // Check network connection status
        JavaDStream<String> networkStatus = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                try {
                    new Socket(hostname, port);
                    return Arrays.asList("Network connection is active").iterator();
                } catch (IOException e) {
                    return Arrays.asList("Network connection failed").iterator();
                }
            }
        });

        // Print the network connection status
        networkStatus.print();

        // Start the computation
        ssc.start();
        ssc.awaitTermination();
    }
}
