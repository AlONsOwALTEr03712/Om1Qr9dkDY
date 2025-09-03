// 代码生成时间: 2025-09-04 01:20:33
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

/**
 * Inventory Management System using Java and Spark Framework.
 * This program demonstrates the basic functionality of a simple inventory management system.
 * It includes functionalities such as adding items, updating stock, and retrieving inventory details.
 */
public class InventoryManagement {

    // Define the schema for the inventory data
    private static StructType schema = new StructType()
        .add("id", DataTypes.IntegerType, false)
        .add("name", DataTypes.StringType, false)
        .add("quantity", DataTypes.IntegerType, false);

    public static void main(String[] args) {
        // Initialize Spark Session
        SparkSession spark = SparkSession
                .builder()
                .appName("InventoryManagement")
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        try {
            // Load inventory data from a CSV file
            Dataset<Row> inventoryData = spark
                .read()
                .schema(schema)
                .csv("path_to_inventory_data.csv");

            // Display initial inventory
            System.out.println("Initial Inventory:
" + inventoryData.show());

            // Add a new item to the inventory
            Dataset<Row> newInventory = addNewItem(inventoryData, new Item("4", "Laptop", 10));
            System.out.println("After Adding New Item:
" + newInventory.show());

            // Update the stock of an existing item
            Dataset<Row> updatedInventory = updateStock(newInventory, "2", 5);
            System.out.println("After Updating Stock:
" + updatedInventory.show());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
            spark.stop();
        }
    }

    /**
     * Adds a new item to the inventory.
     * @param inventory The current inventory data.
     * @param item The item to be added.
     * @return The updated inventory data.
     */
    private static Dataset<Row> addNewItem(Dataset<Row> inventory, Item item) {
        return inventory.union(spark.createDataFrame(java.util.Arrays.asList(item), Item.class));
    }

    /**
     * Updates the stock of an existing item in the inventory.
     * @param inventory The current inventory data.
     * @param id The ID of the item to be updated.
     * @param quantity The new quantity of the item.
     * @return The updated inventory data.
     */
    private static Dataset<Row> updateStock(Dataset<Row> inventory, String id, int quantity) {
        return inventory.withColumn("quantity", functions.when(
                functions.col("id").equalTo(id),
                functions.lit(quantity))
                .otherwise(functions.col("quantity")));
    }

    // Inner class to represent an inventory item
    public static class Item {
        private String id;
        private String name;
        private int quantity;

        public Item(String id, String name, int quantity) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
