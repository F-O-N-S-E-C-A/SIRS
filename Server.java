import java.net.*;
import java.io.*;
import java.security.PublicKey;
import java.util.LinkedList;
import java.util.Base64;

//https://www.geeksforgeeks.org/multithreaded-servers-in-java/

public class Server {
    private AsymmetricKeyPair signPair;
    private AsymmetricKeyPair cipherPair;
    private LinkedList<Client> clients;
    private ServerSocket serverSocket;

    public Server() {
        signPair = new AsymmetricKeyPair("DSA", 2048);
        cipherPair = new AsymmetricKeyPair("RSA", 2048);
        //publicKeysToFile(new PublicKey[]{signPair.getPublicKey(), cipherPair.getPublicKey()});
    }

    public PublicKey getSignPublicKey() {
        return signPair.getPublicKey();
    }

    public PublicKey getCipherPublicKey() {
        return cipherPair.getPublicKey();
    }

    public void serve(){
        try {
            serverSocket = new ServerSocket(2000);
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

    /*private void publicKeysToFile(PublicKey [] pks){
        try {
            FileWriter writer = new FileWriter("serverPublicKeys.txt");
            for(PublicKey pk : pks){
                writer.write(Base64.getEncoder().encodeToString(pk.getEncoded())+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
