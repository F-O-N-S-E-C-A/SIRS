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
}
