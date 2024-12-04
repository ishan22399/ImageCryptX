import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Scanner;

public class AdvancedAESImageEncryption {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 12;   // Recommended IV size for GCM mode
    private static final int TAG_SIZE = 128; // Authentication tag size
    private static SecretKey key;

    public static void main(String[] args) {
        try {
            // Generate AES key once, to use across encrypt and decrypt options
            key = generateAESKey(KEY_SIZE);

            // Menu-driven interface
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\nMenu:");
                System.out.println("1. Encrypt Image");
                System.out.println("2. Decrypt Image");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter path of the image to encrypt: ");
                        String inputImagePath = scanner.nextLine();
                        System.out.print("Enter path to save the encrypted image (e.g., image.enc): ");
                        String encryptedImagePath = scanner.nextLine();
                        encryptImageOption(inputImagePath, encryptedImagePath);
                        break;
                    case 2:
                        System.out.print("Enter path of the encrypted image to decrypt: ");
                        String encryptedFilePath = scanner.nextLine();
                        System.out.print("Enter path to save the decrypted image (e.g., decrypted_image.jpg): ");
                        String decryptedImagePath = scanner.nextLine();
                        decryptImageOption(encryptedFilePath, decryptedImagePath);
                        break;
                    case 3:
                        System.out.println("Exiting program. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 3);

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SecretKey generateAESKey(int keySize) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(keySize);
        return keyGen.generateKey();
    }

    private static void encryptImageOption(String inputImagePath, String outputImagePath) {
        try {
            byte[] iv = encryptImage(inputImagePath, outputImagePath, key);
            System.out.println("Image encrypted successfully! IV (Initialization Vector) saved for decryption.");
        } catch (Exception e) {
            System.out.println("Error during encryption: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void decryptImageOption(String encryptedImagePath, String outputImagePath) {
        try {
            byte[] iv = readIVFromEncryptedFile(encryptedImagePath);
            decryptImage(encryptedImagePath, outputImagePath, key, iv);
            System.out.println("Image decrypted successfully!");
        } catch (Exception e) {
            System.out.println("Error during decryption: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static byte[] encryptImage(String inputImagePath, String outputImagePath, SecretKey key) throws Exception {
        // Generate random IV
        byte[] iv = new byte[IV_SIZE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_SIZE, iv);

        // Initialize Cipher for encryption
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

        // Read image bytes
        byte[] inputBytes = readFileToBytes(inputImagePath);

        // Encrypt image bytes
        byte[] encryptedBytes = cipher.doFinal(inputBytes);

        // Write encrypted image bytes to file
        try (FileOutputStream outputStream = new FileOutputStream(outputImagePath)) {
            outputStream.write(iv);  // Store IV at the beginning of the file
            outputStream.write(encryptedBytes);
        }

        return iv;  // Return IV for use in decryption
    }

    private static void decryptImage(String encryptedImagePath, String outputImagePath, SecretKey key, byte[] iv) throws Exception {
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_SIZE, iv);

        // Initialize Cipher for decryption
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

        // Read encrypted image bytes (IV is already read separately)
        byte[] encryptedBytes = readFileToBytes(encryptedImagePath);

        // Decrypt image bytes
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes, IV_SIZE, encryptedBytes.length - IV_SIZE);

        // Write decrypted image bytes to file
        try (FileOutputStream outputStream = new FileOutputStream(outputImagePath)) {
            outputStream.write(decryptedBytes);
        }
    }

    private static byte[] readIVFromEncryptedFile(String encryptedFilePath) throws Exception {
        try (FileInputStream inputStream = new FileInputStream(encryptedFilePath)) {
            byte[] iv = new byte[IV_SIZE];
            inputStream.read(iv); // Read IV from the start of the file
            return iv;
        }
    }

    private static byte[] readFileToBytes(String filePath) throws Exception {
        File file = new File(filePath);
        byte[] fileData = new byte[(int) file.length()];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(fileData);
        }
        return fileData;
    }
}
