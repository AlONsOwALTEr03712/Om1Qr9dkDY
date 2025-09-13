// 代码生成时间: 2025-09-13 08:22:11
import spark.*;
import java.util.*;
import static spark.Spark.*;

/**
 * Represents an item in the inventory.
 */
class InventoryItem {
    private String id;
    private String name;
    private int quantity;

    public InventoryItem(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

/**
 * Manages the inventory items.
 */
class InventoryManager {
    private Map<String, InventoryItem> items = new HashMap<>();

    /**
     * Adds a new item to the inventory.
     * 
     * @param id The unique identifier of the item.
     * @param name The name of the item.
     * @param quantity The initial quantity of the item.
     */
    public void addItem(String id, String name, int quantity) {
        if (items.containsKey(id)) {
            halt(400, "Item with ID " + id + " already exists.");
        }
        items.put(id, new InventoryItem(id, name, quantity));
    }

    /**
     * Updates the quantity of an existing item.
     * 
     * @param id The unique identifier of the item.
     * @param quantity The new quantity of the item.
     */
    public void updateQuantity(String id, int quantity) {
        if (!items.containsKey(id)) {
            halt(404, "Item with ID " + id + " not found.");
        }
        items.get(id).setQuantity(quantity);
    }

    /**
     * Retrieves an item from the inventory by ID.
     * 
     * @param id The unique identifier of the item.
     * @return The item with the specified ID.
     */
    public InventoryItem getItem(String id) {
        if (!items.containsKey(id)) {
            halt(404, "Item with ID " + id + " not found.");
        }
        return items.get(id);
    }

    /**
     * Returns the list of all items in the inventory.
     * 
     * @return A list of all inventory items.
     */
    public List<InventoryItem> getAllItems() {
        return new ArrayList<>(items.values());
    }
}

public class InventoryManagementSystem {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();

        // Define routes
        get("/items", (req, res) -> {
            return manager.getAllItems();
        });

        get("/items/:id", (req, res) -> {
            String id = req.params(":id");
            return manager.getItem(id);
        });

        post("/items", (req, res) -> {
            String id = req.queryParams("id");
            String name = req.queryParams("name");
            int quantity = Integer.parseInt(req.queryParams("quantity"));
            manager.addItem(id, name, quantity);
            return "Item added successfully.";
        });

        put("/items/:id", (req, res) -> {
            String id = req.params(":id");
            int quantity = Integer.parseInt(req.queryParams("quantity"));
            manager.updateQuantity(id, quantity);
            return "Item quantity updated successfully.";
        });
    }
}