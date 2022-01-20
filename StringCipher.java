import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.*;

public class StringCipher {
    private static byte[] readFile(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        byte[] content = new byte[fis.available()];
        fis.read(content);
        fis.close();
        return content;
    }

    public static PublicKey readPublicKey(String publicKeyPath) throws Exception {
        byte[] pubEncoded = readFile(publicKeyPath);
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubEncoded);
        KeyFactory keyFacPub = KeyFactory.getInstance("RSA");
        return keyFacPub.generatePublic(pubSpec);
    }

    public static PrivateKey readPrivateKey(String privateKeyPath) throws Exception {
        byte[] privEncoded = readFile(privateKeyPath);
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privEncoded);
        KeyFactory keyFacPriv = KeyFactory.getInstance("RSA");
        return keyFacPriv.generatePrivate(privSpec);
    }

    public static Key readSecretKey(String secretKeyPath) throws IOException {
        byte[] encoded = readFile(secretKeyPath);
        return new SecretKeySpec(encoded, "AES");
    }

    public String digest(String plainText, String keyPath) throws Exception {
        Key key = readSecretKey(keyPath);
        byte[] plainBytes = plainText.getBytes();

        final String DIGEST_ALGO = "SHA-256";
        MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALGO);
        messageDigest.update(plainBytes);
        byte[] digestBytes = messageDigest.digest();

        return Base64.getEncoder().encodeToString(digestBytes);
    }

    //  public String cipher(String plainText, String keyPath, Function<String, ? extends Key> keyFunc) throws Exception {
    public String cipher(String plainText, Key key) throws Exception {
//      Key key = keyFunc.apply(keyPath);
        byte[] plainBytes = plainText.getBytes();

        final String CIPHER_ALGO = "AES/ECB/PKCS5Padding";
        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherBytes = cipher.doFinal(plainBytes);

        return Base64.getEncoder().encodeToString(cipherBytes);
    }
}
