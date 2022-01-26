import javax.crypto.NoSuchPaddingException;
import java.awt.desktop.UserSessionEvent;
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

    private Socket socket;

    public HybridCipher(AsymmetricKeyPair sign, AsymmetricKeyPair cipher, Key sp, Key cp , Socket s) throws Exception {
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

    public HybridCipher(AsymmetricKeyPair sign, AsymmetricKeyPair cipher, Socket s) throws Exception {
        // Pub, Priv
        signingPair = sign;
        cipherPair = cipher;

        // Session
        sessionKey = null;

        // Socket
        socket = s;
    }

    public void send(Request request) throws Exception {
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

        if (sessionKey == null){
            try {
                generateKey();
                //Encrypt key kpub recv TODO
                CipheredObject cipheredKey = new CipheredObject(serialize(sessionKey));

                outputStream.writeObject(cipheredKey);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            byte[] cipheredBytes = StringCipher.cipher(serialize(request), sessionKey);

            outputStream.writeObject(new CipheredObject(cipheredBytes));

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Request receive() throws Exception{
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        if (sessionKey == null){
            try {
                //Decrypt TODO
                CipheredObject received = (CipheredObject) inputStream.readObject();

                sessionKey = (Key) deserialize(received.getCipheredBytes());

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            CipheredObject cipheredObject = (CipheredObject) inputStream.readObject();

            byte[] decipheredBytes = StringCipher.decipher(cipheredObject.getCipheredBytes(), sessionKey);

            return (Request) deserialize(decipheredBytes);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void generateKey() throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
        sessionKey = StringCipher.generateSymmetricKey(32, "AES");
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
}
