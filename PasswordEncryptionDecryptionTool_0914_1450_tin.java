// 代码生成时间: 2025-09-14 14:50:55
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncryptionDecryptionTool {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    // 生成AES密钥
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    // 加密密码
    public static String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] byteData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(byteData);
    }

    // 解密密码
    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] byteData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(byteData);
    }

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("PasswordEncryptionDecryptionTool").getOrCreate();
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        try {
            // 生成AES密钥
            SecretKey key = generateAESKey();

            // 加密示例密码
            String encryptedPassword = encrypt("password123", key);
            System.out.println("Encrypted Password: " + encryptedPassword);

            // 解密示例密码
            String decryptedPassword = decrypt(encryptedPassword, key);
            System.out.println("Decrypted Password: " + decryptedPassword);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            sc.stop();
            spark.stop();
        }
    }
}
