import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;

import javax.crypto.*;

public class StringCipher {
    public String digest(String plainText) throws Exception {
        byte[] plainBytes = plainText.getBytes();

        final String DIGEST_ALGO = "SHA-256";
        MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALGO);
        messageDigest.update(plainBytes);
        byte[] digestBytes = messageDigest.digest();

        return Base64.getEncoder().encodeToString(digestBytes);
    }

    public static SecretKeySpec generateSymmetricKey(int length, String algorithm)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException {

        SecureRandom rnd = new SecureRandom();
        byte [] key = new byte [length];
        rnd.nextBytes(key);
        return new SecretKeySpec(key, algorithm);
    }

    private static byte[] useCipherMode(int cipherMode, byte[] plainBytes, Key key) throws Exception {

        final String CIPHER_ALGO = "AES/ECB/PKCS5Padding";
        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
        cipher.init(cipherMode, key);
        byte[] cipherBytes = cipher.doFinal(plainBytes);

        return cipherBytes;
    }

    public static byte[] cipher(byte[] plainBytes, Key key) throws Exception {
        return useCipherMode(Cipher.ENCRYPT_MODE, plainBytes, key);
    }

    public static byte[] decipher(byte[] plainBytes, Key key) throws Exception {
        return useCipherMode(Cipher.DECRYPT_MODE, plainBytes, key);
    }
}
