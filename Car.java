import java.net.*;
import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class Car {
    private Location loc;
    private AsymmetricKeyPair signingPair;
    private AsymmetricKeyPair cipherPair;
    private Socket socket;
    private PublicKey serverSignPublicKey;
    private PublicKey serverCipherPublicKey;


    public Car() {
        signingPair = new AsymmetricKeyPair("DSA", 2048);
        cipherPair = new AsymmetricKeyPair("RSA", 2048);
    }

    public Car(Location loc) {
        signingPair = new AsymmetricKeyPair("DSA", 2048);
        cipherPair = new AsymmetricKeyPair("RSA", 2048);
        this.loc = loc;
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }

    public Location getLocation() {
        return loc;
    }

    public void requestProofOfLocation() {
        try {
            socket = new Socket("localhost", 2000);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // create Message with Request of Proof of location

            setServerKeys(objectInputStream);

            ServerRequest request = new ServerRequest("Request of proof of location");


            objectOutputStream.writeObject(request);

            ServerRequest response = (ServerRequest) objectInputStream.readObject();
            System.out.println(response.getType() + "  " + response.getTimeStamp());

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*private void getServerKeysFromFile() {
        try {
            File file = new File("serverPublicKeys.txt");
            Scanner reader = new Scanner(file);
            //serverSignPublicKey = KeyFactory.getInstance("DSA").generatePublic(Base64.getDecoder().decode(reader.nextLine()));
            serverCipherPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(reader.nextLine())));
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }*/
    private void setServerKeys(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        serverSignPublicKey = (PublicKey) objectInputStream.readObject(); // simulation
        serverCipherPublicKey = (PublicKey) objectInputStream.readObject(); // simulation
    }
}