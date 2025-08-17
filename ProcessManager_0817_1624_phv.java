// 代码生成时间: 2025-08-17 16:24:03
import spark.Spark;
import spark.template.freemarker.FreeMarkerTemplateEngine;
import java.util.HashMap;
import java.util.Map;

public class ProcessManager {

    private static final Map<String, Process> processes = new HashMap<>();
    private static final String PROCESSES_ENDPOINT = "/processes";
    private static final String START_PROCESS_ENDPOINT = "/processes/start/:processName";
    private static final String STOP_PROCESS_ENDPOINT = "/processes/stop/:processName";
    private static final String PROCESS_NAME_PARAMETER = ":processName";

    // Simulated process class
    private static class Process {
        private String name;
        private boolean running;

        public Process(String name) {
            this.name = name;
            this.running = false;
        }

        public String getName() {
            return name;
        }

        public boolean isRunning() {
            return running;
        }

        public void start() {
            running = true;
        }

        public void stop() {
            running = false;
        }
    }

    public static void main(String[] args) {
        // Set up Spark routes
        setupRoutes();
    }

    private static void setupRoutes() {
        // Set up FreeMarker template engine for HTML rendering
        Spark.templateEngine(new FreeMarkerTemplateEngine());

        // List all processes
        Spark.get(PROCESSES_ENDPOINT, (request, response) -> {
            return processes;
        }, new FreeMarkerTemplateEngine());

        // Start a process
        Spark.get(START_PROCESS_ENDPOINT, (request, response) -> {
            String processName = request.params(PROCESS_NAME_PARAMETER);
            Process process = processes.get(processName);
            if (process == null) {
                throw new IllegalArgumentException("Process not found: " + processName);
            }
            process.start();
            return "Process ' " + processName + " ' started.";
        });

        // Stop a process
        Spark.get(STOP_PROCESS_ENDPOINT, (request, response) -> {
            String processName = request.params(PROCESS_NAME_PARAMETER);
            Process process = processes.get(processName);
            if (process == null) {
                throw new IllegalArgumentException("Process not found: " + processName);
            }
            process.stop();
            return "Process ' " + processName + " ' stopped.";
        });

        // Add a new process
        Spark.post("/processes/add", (request, response) -> {
            String processName = request.queryParams("name");
            if (processName == null || processName.isEmpty()) {
                throw new IllegalArgumentException("Process name cannot be empty.");
            }
            if (processes.containsKey(processName)) {
                throw new IllegalArgumentException("Process already exists: " + processName);
            }
            processes.put(processName, new Process(processName));
            return "Process ' " + processName + " ' added.";
        });
    }
}
