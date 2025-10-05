// 代码生成时间: 2025-10-05 21:41:50
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple task assignment system using Java and Spark framework.
 * This system simulates the assignment of tasks to workers in a distributed manner.
 */
public class TaskAssignmentSystem {

    /**
     * Main method to run the task assignment system.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("TaskAssignmentSystem").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        try {
            // Simulate task data
            List<String> tasks = Arrays.asList("Task1", "Task2", "Task3", "Task4");
            List<String> workers = Arrays.asList("Worker1", "Worker2", "Worker3");

            // Create RDDs from task and worker data
            JavaRDD<String> taskRDD = sc.parallelize(tasks);
            JavaRDD<String> workerRDD = sc.parallelize(workers);

            // Assign tasks to workers
            JavaRDD<String> assignedTasks = assignTasks(taskRDD, workerRDD);

            // Collect and print the assigned tasks
            assignedTasks.collect().forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Error in task assignment system: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (sc != null) {
                sc.stop();
            }
        }
    }

    /**
     * Assign tasks to workers in a round-robin fashion.
     * @param tasksRDD The RDD containing task data.
     * @param workersRDD The RDD containing worker data.
     * @return An RDD containing assigned tasks.
     */
    public static JavaRDD<String> assignTasks(JavaRDD<String> tasksRDD, JavaRDD<String> workersRDD) {
        return tasksRDD.zipWithIndex().mapToPair(task -> new AbstractPair<>(task._1, task._2))
                .join(workersRDD.zipWithIndex().mapToPair(worker -> new AbstractPair<>(worker._1, worker._2)))
                .map(joinResult -> "Task: " + joinResult._1 + ", Assigned to: " + joinResult._2()._1)
                .filter(taskInfo -> taskInfo != null);
    }
}

/**
 * A simple pair class to hold two values.
 */
class AbstractPair<T1, T2> implements Pair<T1, T2> {
    private T1 first;
    private T2 second;

    public AbstractPair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public T1 _1() {
        return first;
    }

    @Override
    public T2 _2() {
        return second;
    }
}

/**
 * A simple interface for a pair of values.
 */
interface Pair<T1, T2> {
    T1 _1();
    T2 _2();
}