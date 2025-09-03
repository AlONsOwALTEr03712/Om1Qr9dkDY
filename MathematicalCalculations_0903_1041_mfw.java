// 代码生成时间: 2025-09-03 10:41:37
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MathematicalCalculations {
    // Main method to run the Spark job
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("MathematicalCalculations").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Example data: pairs of numbers to perform calculations on
        List<Tuple2<Integer, Integer>> data = Arrays.asList(
                new Tuple2<>(1, 2),
                new Tuple2<>(3, 4),
                new Tuple2<>(5, 6)
        );

        JavaRDD<Tuple2<Integer, Integer>> rdd = sc.parallelize(data);

        // Perform calculations
        JavaRDD<Tuple2<Integer, Double>> sumRdd = performSum(rdd);
        JavaRDD<Tuple2<Integer, Double>> productRdd = performProduct(rdd);
        JavaRDD<Tuple2<Integer, Double>> differenceRdd = performDifference(rdd);
        JavaRDD<Tuple2<Integer, Double>> quotientRdd = performQuotient(rdd);

        // Collect and print the results
        System.out.println("Sum: " + sumRdd.collect().stream().map(tuple -> tuple._1() + ": " + tuple._2()).collect(Collectors.toList()));
        System.out.println("Product: " + productRdd.collect().stream().map(tuple -> tuple._1() + ": " + tuple._2()).collect(Collectors.toList()));
        System.out.println("Difference: " + differenceRdd.collect().stream().map(tuple -> tuple._1() + ": " + tuple._2()).collect(Collectors.toList()));
        System.out.println("Quotient: " + quotientRdd.collect().stream().map(tuple -> tuple._1() + ": " + tuple._2()).collect(Collectors.toList()));

        // Stop the Spark context
        sc.stop();
    }

    // Function to calculate the sum of pairs
    private static JavaRDD<Tuple2<Integer, Double>> performSum(JavaRDD<Tuple2<Integer, Integer>> rdd) {
        return rdd.map(tuple -> new Tuple2<>(tuple._1(), tuple._1() + tuple._2()));
    }

    // Function to calculate the product of pairs
    private static JavaRDD<Tuple2<Integer, Double>> performProduct(JavaRDD<Tuple2<Integer, Integer>> rdd) {
        return rdd.map(tuple -> new Tuple2<>(tuple._1(), tuple._1() * tuple._2()));
    }

    // Function to calculate the difference of pairs
    private static JavaRDD<Tuple2<Integer, Double>> performDifference(JavaRDD<Tuple2<Integer, Integer>> rdd) {
        return rdd.map(tuple -> new Tuple2<>(tuple._1(), tuple._1() - tuple._2()));
    }

    // Function to calculate the quotient of pairs
    private static JavaRDD<Tuple2<Integer, Double>> performQuotient(JavaRDD<Tuple2<Integer, Integer>> rdd) {
        return rdd.map(tuple -> {
            if (tuple._2() == 0) {
                throw new ArithmeticException("Cannot divide by zero");
            } else {
                return new Tuple2<>(tuple._1(), (double) tuple._1() / tuple._2());
            }
        });
    }
}
