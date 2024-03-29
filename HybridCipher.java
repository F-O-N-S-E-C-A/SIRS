import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.security.Key;
import java.security.NoSuchAlgorithmException;


public class HybridCipher {
    private AsymmetricKeyPair signingPair;
    private AsymmetricKeyPair cipherPair;
    private Key recv_cipher_pub;
    private Key recv_sign_pub;
    private Key sessionKey;

    private byte[] sessionKeySignature;

    private Socket socket;

    public HybridCipher(AsymmetricKeyPair sign, AsymmetricKeyPair cipher, Key sp, Key cp , Socket s) {
        // Pub, Priv
        signingPair = sign;
        cipherPair = cipher;

        // Dest pub keys
        recv_cipher_pub = cp;
        recv_sign_pub = sp;

        // Session
        sessionKey = null;

        // Socket
        socket = s;
    }

    public HybridCipher(AsymmetricKeyPair sign, AsymmetricKeyPair cipher, Socket s){
        // Pub, Priv
        signingPair = sign;
        cipherPair = cipher;

        // Session
        sessionKey = null;

        // Socket
        socket = s;
    }

    public void send(MillenniumFalcon request) {

        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sessionKey == null){
            try {
                generateKey();
                byte[] signature = signingPair.sign(sessionKey);
                byte[] cipheredSK = Cipher.asymmetricCipher(serialize(sessionKey), recv_cipher_pub);
                CipheredObject cipheredKey = new CipheredObject(cipheredSK);
                cipheredKey.setSignature(signature);
                outputStream.writeObject(cipheredKey);

            } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        try {
            byte[] cipheredBytes = Cipher.cipher(serialize(request), sessionKey);
            CipheredObject ciphered = new CipheredObject(cipheredBytes);
            ciphered.setSignature(signingPair.sign(request));
            outputStream.writeObject(ciphered);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MillenniumFalcon receive(){
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sessionKey == null){
            try {
                CipheredObject received = (CipheredObject) inputStream.readObject();
                sessionKey = (Key) deserialize(cipherPair.decipher(received.getCipheredBytes()));
                sessionKeySignature = received.getSignature();
                if(recv_sign_pub != null){
                    verifySKSignature();
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            CipheredObject cipheredObject = (CipheredObject) inputStream.readObject();

            byte[] decipheredBytes = Cipher.decipher(cipheredObject.getCipheredBytes(), sessionKey);

            MillenniumFalcon r = (MillenniumFalcon) deserialize(decipheredBytes);

            if(recv_sign_pub == null){
                setReceiverPubKeys(Simulator.readPublicKeys(r.getId()));
                verifySKSignature();
            }

            if (!AsymmetricKeyPair.verifySignature(recv_sign_pub, cipheredObject.getSignature(), r)){
                System.err.println("invalid signature");
            }

            return r;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void generateKey() throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
        sessionKey = Cipher.generateSymmetricKey(32, "AES"); // 32 bytes * 8 bits = 256 bits
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public void closeSocket(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReceiverPubKeys(Key[] k){
        recv_sign_pub = k[0];
        recv_cipher_pub = k[1];

    }

    private void verifySKSignature(){
        if (!AsymmetricKeyPair.verifySignature(recv_sign_pub, sessionKeySignature, sessionKey)){
            System.err.println("invalid signature");
        }
    }
}
