import java.net.*;
import java.io.*;
import java.util.LinkedList;

//https://www.geeksforgeeks.org/multithreaded-servers-in-java/

public class Server {
    private AsymmetricKeyPair signingPair;
    private AsymmetricKeyPair cipherPair;
    private LinkedList<Client> clients;
    private ServerSocket serverSocket;

    public Server() {
        signingPair = new AsymmetricKeyPair("DSA", 2048);
        cipherPair = new AsymmetricKeyPair("RSA", 2048);
    }

    public void serve(){
        try {
            serverSocket = new ServerSocket(2000);
            serverSocket.setReuseAddress(true);

            while (true) {
                Socket socket = serverSocket.accept();
                //System.out.println("New client connected"+ client.getInetAddress().getHostAddress());
                new Thread(new Handler(socket)).start();
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
