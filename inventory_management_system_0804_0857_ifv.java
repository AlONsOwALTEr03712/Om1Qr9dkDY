// 代码生成时间: 2025-08-04 08:57:18
import spark.*;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryManagementSystem {
    // In-memory storage for the inventory items
    private static Map<String, Integer> inventory = new HashMap<>();

    /**
     * Initializes the inventory with some sample data.
     */
    public static void initializeInventory() {
        inventory.put("item1", 100);
        inventory.put("item2", 50);
        inventory.put("item3", 200);
    }

    /**
     * Adds or updates an item in the inventory.
     *
     * @param itemId The ID of the item.
     * @param quantity The quantity of the item to add or update.
     */
    public static void addItem(String itemId, int quantity) {
        if (itemId == null || itemId.isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty.");
        }

        inventory.merge(itemId, quantity, Integer::sum);
    }

    /**
     * Removes an item from the inventory.
     *
     * @param itemId The ID of the item to remove.
     * @param quantity The quantity of the item to remove.
     */
    public static void removeItem(String itemId, int quantity) {
        if (itemId == null || itemId.isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty.");
        }

        if (!inventory.containsKey(itemId)) {
            throw new IllegalArgumentException("Item does not exist in inventory.");
        }

        int currentQuantity = inventory.get(itemId);
        if (quantity > currentQuantity) {
            throw new IllegalArgumentException("Cannot remove more items than are available.");
        }

        inventory.put(itemId, currentQuantity - quantity);
    }

    /**
     * Retrieves the quantity of a specific item in the inventory.
     *
     * @param itemId The ID of the item.
     * @return The quantity of the item.
     */
    public static int getItemQuantity(String itemId) {
        if (!inventory.containsKey(itemId)) {
            throw new IllegalArgumentException("Item does not exist in inventory.");
        }

        return inventory.get(itemId);
    }

    /**
     * Main method to start the Spark application.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        initializeInventory();

        // Set up Spark routes
        get("/items", (req, res) -> {
            Map<String, Integer> items = new HashMap<>(inventory);
            return items;
        }, new JavaMapTransformer<>());

        get("/items/:itemId", (req, res) -> {
            String itemId = req.params(":itemId");
            return getItemQuantity(itemId);
        });

        post("/items/:itemId/:quantity", (req, res) -> {
            String itemId = req.params(":itemId");
            int quantity = Integer.parseInt(req.params(":quantity"));
            addItem(itemId, quantity);
            return "Item added successfully";
        });

        delete("/items/:itemId/:quantity", (req, res) -> {
            String itemId = req.params(":itemId");
            int quantity = Integer.parseInt(req.params(":quantity"));
            removeItem(itemId, quantity);
            return "Item removed successfully";
        });
    }
}
