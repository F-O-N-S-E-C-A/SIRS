import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.security.Key;
import java.security.PublicKey;
import java.util.*;

//https://www.geeksforgeeks.org/multithreaded-servers-in-java/

public class Server {
    private AsymmetricKeyPair signPair;
    private AsymmetricKeyPair cipherPair;
    private HashMap<Integer, Client> cars;
    private ServerSocket serverSocket;
    private UUID id;

    private HashMap<UUID, LinkedList<Request>> witnessReport;

    private int lastID = 0;

    public static final int serverPort = 2000;
    public static final String serverHost = "localhost";

    public Server() throws IOException, ClassNotFoundException {
        signPair = new AsymmetricKeyPair("DSA", 2048);
        cipherPair = new AsymmetricKeyPair("RSA", 2048);

        id = UUID.fromString(Simulator.serverID);
        HashMap<UUID, Key[]> pubKeys = new HashMap<>();

        Simulator.writePublicKeysToFile(pubKeys, id, signPair.getPublicKey(), cipherPair.getPublicKey());

        witnessReport = new HashMap<>();
    }

    public synchronized LinkedList<Request> getRequestsFromProver(UUID proverID){
        LinkedList <Request> requests = witnessReport.get(proverID);
        return requests;
    }

    public synchronized void addWitnessReport(UUID proverID, Request r){

        if (witnessReport.containsKey(proverID)){
            System.out.println(witnessReport);
            LinkedList <Request> lst = witnessReport.get(proverID);
            lst.add(r);
            witnessReport.put(proverID, lst);
        } else {
            LinkedList <Request> requests = new LinkedList<>();
            requests.add(r);
            witnessReport.put(proverID,requests);
        }

    }

    public PublicKey getSignPublicKey() {
        return signPair.getPublicKey();
    }

    public PublicKey getCipherPublicKey() {
        return cipherPair.getPublicKey();
    }


    public void sendCertificate(UUID id){
        try {
            Socket socket = new Socket("localhost", Car.incomingPort);
            Request request = new Request(this.id, "certificate");
            Key[] carKeys = Simulator.readPublicKeys(id);
            HybridCipher hs = new HybridCipher(signPair, cipherPair, carKeys[0], carKeys[1], socket);
            hs.send(request);
            hs.closeSocket();
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

    public AsymmetricKeyPair getSignPair() {
        return signPair;
    }

    public AsymmetricKeyPair getCipherPair() {
        return cipherPair;
    }

    public UUID getID(){
        return id;
    }
}
