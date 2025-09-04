// 代码生成时间: 2025-09-05 02:45:04
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.SparkSession;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class PasswordEncryptionDecryptionTool {

    private static final String ALGORITHM = "AES";
    private static final String ALGORITHM_CIPHER = ALGORITHM + "/ECB/PKCS5Padding";
    private static final int AES_KEY_SIZE = 128;

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("PasswordEncryptionDecryptionTool");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        // Example usage of the tool
        JavaRDD<String> passwords = sc.parallelize(Arrays.asList("password1", "password2", "password3"));
        passwords.foreachPartition(new VoidFunction<String[]>() {
            @Override
            public void call(String[] partition) throws Exception {
                SecretKey key = generateKey();
                for (String password : partition) {
                    String encryptedPassword = encryptPassword(password, key);
                    String decryptedPassword = decryptPassword(encryptedPassword, key);
                    System.out.println("Encrypted: " + encryptedPassword);
                    System.out.println("Decrypted: " + decryptedPassword);
                }
            }
        });

        sc.close();
        spark.stop();
    }

    // Generate a secret key for AES encryption
    private static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(AES_KEY_SIZE, new SecureRandom());
        return keyGen.generateKey();
    }

    // Encrypt a password using AES
    private static String encryptPassword(String password, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt a password using AES
    private static String decryptPassword(String encryptedPassword, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedPassword);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
