import java.net.*;
import java.io.*;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Base64;

//https://www.geeksforgeeks.org/multithreaded-servers-in-java/

public class Server {
    private AsymmetricKeyPair signPair;
    private AsymmetricKeyPair cipherPair;
    private HashMap<Integer, String> cars;
    private ServerSocket serverSocket;

    private int lastID = 0;

    public static final int serverPort = 2000;
    public static final String serverHost = "localhost";

    public Server() {
        signPair = new AsymmetricKeyPair("DSA", 2048);
        cipherPair = new AsymmetricKeyPair("RSA", 2048);
    }

    public PublicKey getSignPublicKey() {
        return signPair.getPublicKey();
    }

    public PublicKey getCipherPublicKey() {
        return cipherPair.getPublicKey();
    }

    public int addCar(String s){
        cars.put(lastID+1, s);
        return lastID;
    }

    public void sendCertificate(int id){
        try {
            Socket socket = new Socket("localhost", Car.incomingPort);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            // create Message with Request of Proof of location

            Request request = new Request("certificate");

            objectOutputStream.writeObject(request);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serve(){
        try {
            serverSocket = new ServerSocket(serverPort);
            serverSocket.setReuseAddress(true);

            while (true) {
                Socket socket = serverSocket.accept();
                //System.out.println("New client connected"+ client.getInetAddress().getHostAddress());
                new Thread(new Handler(socket, this)).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
