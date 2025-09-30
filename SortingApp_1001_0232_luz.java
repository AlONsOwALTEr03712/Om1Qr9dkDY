// 代码生成时间: 2025-10-01 02:32:22
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import scala.reflect.ClassTag;

public class SortingApp {

    public static void main(String[] args) {
        // Set up Spark configuration and context
        SparkConf conf = new SparkConf().setAppName("SortingApp").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Handle program arguments
        if (args.length < 1) {
            System.err.println("Usage: SortingApp <file>");
            System.exit(1);
        }

        String inputFile = args[0];

        try {
            // Load data into RDD
            JavaRDD<String> lines = sc.textFile(inputFile);

            // Split into words (for demonstration, assuming each line is in format "key value")
            JavaRDD<String> keyValuePairs = lines.map(line -> line.split(" "));

            // Sort the RDD based on the values
            JavaRDD<String[]> sortedPairs = keyValuePairs.mapToPair(pair -> new Tuple2<>(pair[0], Integer.parseInt(pair[1])))
                    .sortByKey(new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    })
                    .values()
                    .map(pair -> pair._2);

            // Collect and print sorted results
            List<Integer> sortedList = sortedPairs.collect();
            sortedList.forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Error during sorting: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.stop();
        }
    }

    // Inner class for tuples since Java does not have a built-in Tuple class
    public static class Tuple2<T1, T2> implements ClassTag<Tuple2<T1, T2>> {
        public T1 _1;
        public T2 _2;

        public Tuple2(T1 _1, T2 _2) {
            this._1 = _1;
            this._2 = _2;
        }
    }
}
