// 代码生成时间: 2025-09-08 04:19:27
import static spark.Spark.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncryptionDecryptionApp {
    // Define the algorithm for encryption and decryption
    private static final String ALGORITHM = "AES";

    public static void main(String[] args) {
        // Initialize Spark web server
        port(4567);
        get("/encrypt/:password", (req, res) -> encrypt(req.params(":password")));
        get("/decrypt/:password", (req, res) -> decrypt(req.params(":password")));
    }

    // Function to encrypt a password
    private static String encrypt(String plainText) {
        try {
            // Generate a key for encryption
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(128); // Use 128-bit AES key
            SecretKey secretKey = keyGen.generateKey();

            // Convert the secret key to bytes
            byte[] keyBytes = secretKey.getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);

            // Encrypt the plain text
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            // Encode the encrypted bytes to base64 and return
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            // Handle any exceptions that occur during encryption
            e.printStackTrace();
            return null;
        }
    }

    // Function to decrypt a password
    private static String decrypt(String encryptedText) {
        try {
            // Convert the base64 encoded encrypted text to bytes
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

            // Recreate the secret key from the original key bytes used for encryption
            byte[] keyBytes = encryptedText.getBytes(); // This is just for demonstration purposes
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);

            // Decrypt the encrypted bytes
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            // Convert the decrypted bytes back to a string
            return new String(cipher.doFinal(encryptedBytes));
        } catch (Exception e) {
            // Handle any exceptions that occur during decryption
            e.printStackTrace();
            return null;
        }
    }
}
