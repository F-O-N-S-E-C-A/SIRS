import javax.crypto.spec.SecretKeySpec;
import java.security.*;

import javax.crypto.*;

public class Cipher {
    /*public String digest(String plainText) throws Exception {
        byte[] plainBytes = plainText.getBytes();

        final String DIGEST_ALGO = "SHA-256";
        MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALGO);
        messageDigest.update(plainBytes);
        byte[] digestBytes = messageDigest.digest();

        return Base64.getEncoder().encodeToString(digestBytes);
    }*/

    public static SecretKeySpec generateSymmetricKey(int length, String algorithm) {
        SecureRandom rnd = new SecureRandom();
        byte [] key = new byte [length];
        rnd.nextBytes(key);
        return new SecretKeySpec(key, algorithm);
    }

    private static byte[] useCipherMode(int cipherMode, byte[] plainBytes, Key key) throws Exception {

        final String CIPHER_ALGO = "AES/ECB/PKCS5Padding";
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER_ALGO);
        cipher.init(cipherMode, key);
        byte[] cipherBytes = cipher.doFinal(plainBytes);

        return cipherBytes;
    }

    public static byte[] cipher(byte[] plainBytes, Key key){
        try {
            return useCipherMode(javax.crypto.Cipher.ENCRYPT_MODE, plainBytes, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decipher(byte[] plainBytes, Key key) {
        try {
            return useCipherMode(javax.crypto.Cipher.DECRYPT_MODE, plainBytes, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] asymmetricCipher(byte[] plainBytes, Key key){
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
            cipher.update(plainBytes);
            return cipher.doFinal();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] asymmetricDecipher(byte[] ciphered, Key key){
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(ciphered);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}
