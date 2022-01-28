import java.net.*;
import java.io.*;
import java.security.Key;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.*;

public class Server {
    private AsymmetricKeyPair signPair;
    private AsymmetricKeyPair cipherPair;
    private ServerSocket serverSocket;
    private UUID id;
    private HashMap<UUID, LinkedList<MillenniumFalcon>> witnessReport;

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

    public synchronized LinkedList<MillenniumFalcon> getRequestsFromProver(UUID proverID) {
        LinkedList<MillenniumFalcon> requests = witnessReport.get(proverID);
        return requests;
    }

    public synchronized void addWitnessReport(UUID proverID, MillenniumFalcon r) {
        if (witnessReport.containsKey(proverID)) {
            LinkedList<MillenniumFalcon> lst = witnessReport.get(proverID);
            lst.add(r);
            witnessReport.put(proverID, lst);
        } else {
            LinkedList<MillenniumFalcon> requests = new LinkedList<>();
            requests.add(r);
            witnessReport.put(proverID, requests);
        }

    }

    public PublicKey getSignPublicKey() {
        return signPair.getPublicKey();
    }

    public PublicKey getCipherPublicKey() {
        return cipherPair.getPublicKey();
    }

    public void sendCertificate(UUID id, Boolean valid, Location loc) {
        try {
            Socket socket = new Socket("localhost", Car.incomingPort);

            Key[] carKeys = Simulator.readPublicKeys(id);
            HybridCipher hs = new HybridCipher(signPair, cipherPair, carKeys[0], carKeys[1], socket);
            MillenniumFalcon request = new MillenniumFalcon(this.id, "Certificate");
            if (valid) {
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                String certificate = "CERTIFICATE_TIME:" + ts + "_LOC:" + loc.toString() + "_TO:" + carKeys[0].toString() + "_APPROVED_BY:" + signPair.getPublicKey();
                request.setCertificate(certificate, signPair.sign(certificate));
            } else {
                request.setType("Not approved");
            }
            hs.send(request);
            hs.closeSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serve() {
        try {
            serverSocket = new ServerSocket(serverPort);
            serverSocket.setReuseAddress(true);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new Handler(socket, this)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
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

    public UUID getID() {
        return id;
    }
}
