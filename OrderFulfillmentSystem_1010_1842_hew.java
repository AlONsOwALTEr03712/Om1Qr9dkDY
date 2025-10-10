// 代码生成时间: 2025-10-10 18:42:03
import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;

public class OrderFulfillmentSystem {
    // Map to simulate a database of orders
    private static final Map<Integer, Order> orderDatabase = new HashMap<>();
    
    public static void main(String[] args) {
        port(4567); // Set the port for the Spark server
        
        // Route to accept new order
        get("/orders", (req, res) -> {
            return getAllOrders();
        });
        
        // Route to create a new order
        post("/orders", (req, res) -> {
            String orderDetails = req.body();
            try {
                Order newOrder = new Order(orderDetails);
                orderDatabase.put(newOrder.getId(), newOrder);
                res.status(201);
                return "Order created with ID: " + newOrder.getId();
            } catch (Exception e) {
                res.status(400);
                return "Error creating order: " + e.getMessage();
            }
        });
    }
    
    // Method to get all orders
    private static String getAllOrders() {
        // Convert the order map to a JSON string for the response
        return orderDatabase.values().toString();
    }
}

/**
 * Order.java
 * 
 * This class represents an order in the system.
 */
class Order {
    private int id;
    private String details;
    
    public Order(String details) {
        this.id = generateUniqueId();
        this.details = details;
    }
    
    private static int idCounter = 1;
    
    // Generate a unique ID for each order
    private static int generateUniqueId() {
        return idCounter++;
    }
    
    public int getId() {
        return id;
    }
    
    public String getDetails() {
        return details;
    }
    
    @Override
    public String toString() {
        return "Order{"id": " + id + ", "details": " + details + "}";
    }
}