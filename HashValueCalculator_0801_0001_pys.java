// 代码生成时间: 2025-08-01 00:01:37
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HashValueCalculator {

    /**
     * Calculates the hash value for a given input string using SHA-256 algorithm.
     * 
     * @param input The input string for which the hash is to be calculated.
     * @return A map containing the original input and its hash value.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
    public Map<String, String> calculateHash(String input) throws NoSuchAlgorithmException {
        // Initialize a map to store the input and its hash value.
        Map<String, String> result = new HashMap<>();

        // Get the MessageDigest instance for SHA-256 algorithm.
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Compute hash of the input string.
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        // Convert the bytes to a hexadecimal string.
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        // Store the input and its hash value in the map.
        result.put("input", input);
        result.put("hash", hexString.toString());

        // Return the map with input and hash value.
        return result;
    }

    /**
     * Main method to run the hash calculation.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            HashValueCalculator calculator = new HashValueCalculator();
            Map<String, String> hashResult = calculator.calculateHash("Hello, World!");
            System.out.println("Original Input: " + hashResult.get("input"));
            System.out.println("Hash Value: " + hashResult.get("hash"));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: SHA-256 algorithm not found.");
        }
    }
}
