import java.io.IOException;
import java.security.*;

public class AsymmetricKeyPair {
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public AsymmetricKeyPair(String algorithm, int size) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
            keyPairGen.initialize(size);
            pair = keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
    }

    public AsymmetricKeyPair() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);
            pair = keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] sign(Object obj){
        try {
            Signature signature = Signature.getInstance("SHA256withDSA");
            signature.initSign(privateKey);
            signature.update(HybridCipher.serialize(obj));
            return signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | IOException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verifySignature(PublicKey pk, byte [] sig, Object data){
        try {
            Signature signature = Signature.getInstance("SHA256withDSA");
            signature.initVerify(pk);
            signature.update(HybridCipher.serialize(data));
            return signature.verify(sig);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public byte[] decipher(byte[] ciphered){
        return StringCipher.asymmetricDecipher(ciphered, privateKey);
    }


}
