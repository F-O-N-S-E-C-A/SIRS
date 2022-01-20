import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Car {
    private Location loc;
    private AsymmetricKeyPair signingPair;
    private AsymmetricKeyPair cipherPair;
    private Socket socket;

    public Car() {
        signingPair = new AsymmetricKeyPair("DSA", 2048);
        cipherPair = new AsymmetricKeyPair("RSA", 2048);
    }

    public Car(Location loc) {
        signingPair = new AsymmetricKeyPair("DSA", 2048);
        cipherPair = new AsymmetricKeyPair("RSA", 2048);
        this.loc = loc;
    }

    public void setLocation(Location loc){
        this.loc = loc;
    }

    public Location getLocation(){
        return loc;
    }

    public void requestProofOfLocation(){
        Message request = new Message("ola");
        try {
            socket = new Socket("localhost", 2000);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            objectOutputStream.writeObject(request);

            Message response = (Message) objectInputStream.readObject();
            System.out.println(response.message);

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
