import java.net.*;
import java.io.*;
import java.security.Key;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import static java.lang.Thread.*;
import static java.lang.Thread.sleep;



public class Car {
    private Location loc;
    private AsymmetricKeyPair signingPair;
    private AsymmetricKeyPair cipherPair;
    private Socket socket;
    private Key serverSignPublicKey;
    private Key serverCipherPublicKey;
    private String host = "localhost";
    private HybridCipher hs;
    private Car prover;

    private UUID id;


    public static int incomingPort = 4000;

    private LinkedList<Request> witness_requests = new LinkedList<>();


    public Car() throws IOException, ClassNotFoundException {
        signingPair = new AsymmetricKeyPair("DSA", 2048);
        cipherPair = new AsymmetricKeyPair("RSA", 2048);
        id = UUID.randomUUID();

        Simulator.writePublicKeysToFile(id, signingPair.getPublicKey(), cipherPair.getPublicKey());
    }

    public Car(Location loc) throws IOException, ClassNotFoundException {
        signingPair = new AsymmetricKeyPair("DSA", 2048);
        cipherPair = new AsymmetricKeyPair("RSA", 2048);
        this.loc = loc;
        id = UUID.randomUUID();

        Simulator.writePublicKeysToFile(id, signingPair.getPublicKey(), cipherPair.getPublicKey());
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }

    public Location getLocation() {
        return loc;
    }

    public String getHost() {
        return host;
    }

    public void requestProofOfLocation() throws Exception {
        socket = new Socket(Server.serverHost, Server.serverPort);

        Key[] serverKeys = Simulator.readPublicKeys(UUID.fromString(Simulator.serverID));
        serverSignPublicKey = serverKeys[0];
        serverCipherPublicKey = serverKeys[1];

        Request request = new Request(id, "request_timestamp");
        request.setProverID(id);

        hs = new HybridCipher(signingPair, cipherPair, serverSignPublicKey, serverCipherPublicKey, socket);

        hs.send(request);
        Request response = hs.receive();
        hs.closeSocket();
        System.out.println(response.getType());

        if (response != null) {
            requestWitness(response);
            waitForCertificate();
        }
    }

    public void waitForCertificate(){
        new Thread() {
            public void run(){
                ServerSocket ss = null;
                try {
                    ss = new ServerSocket(incomingPort);
                    ss.setReuseAddress(true);

                    while (true) {
                        Socket socket = ss.accept();
                        hs = new HybridCipher(signingPair, cipherPair, serverSignPublicKey, serverCipherPublicKey, socket);
                        Request request = hs.receive();
                        System.out.println(request.getType());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (ss != null) {
                        try {
                            ss.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }




    public void witness_sendProofs(){
        try {
            socket = new Socket(Server.serverHost, Server.serverPort);

            Key[] serverKeys = Simulator.readPublicKeys(UUID.fromString(Simulator.serverID));
            serverSignPublicKey = serverKeys[0];
            serverCipherPublicKey = serverKeys[1];

            hs = new HybridCipher(signingPair, cipherPair, serverSignPublicKey, serverCipherPublicKey, socket);

            System.out.println("send proofs " + witness_requests.size());
            Request request = witness_requests.pop();
            request.setSender(id, "witness_proof");
            request.setWitnessLocation(getLocation().getStringLoc());
            hs.send(request);
            hs.closeSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void witness_receiveRequest(int port){
        Car c = this;
        new Thread() {
            public void run(){
                ServerSocket ss = null;
                try {
                    ss = new ServerSocket(port);
                    ss.setReuseAddress(true);

                    while (true) {
                        Socket socket = ss.accept();
                        System.out.println("New client connected");
                        CarHandler handler = new CarHandler(c, socket, "witness");
                        handler.setReceiver(prover);
                        new Thread(handler).start();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if (ss != null) {
                        try {
                            ss.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }
    public void addRequest(Request r){
        witness_requests.add(r);
    }

    public void requestWitness(Request r) throws IOException, ClassNotFoundException {
        Simulator sim = new Simulator(this, 20);
        HashMap<Integer, Car> witnesses = sim.findWitnesses(3000);
        for(int port : witnesses.keySet()){
            String host = witnesses.get(port).getHost();
            // make socket connections
            try {
                //System.out.println("request witness");
                boolean connected = false;
                while(!connected){
                    try {
                        socket = new Socket(host, port);
                        connected = true;
                    } catch (ConnectException e){
                        Thread.sleep(100);
                        connected = false;
                    }
                }

                System.out.println("request witness");
                CarHandler thread = new CarHandler(this, socket, "prover");
                thread.setRequest(r);
                thread.setReceiver(witnesses.get(port));
                new Thread(thread).start();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public UUID getID(){
        return this.id;
    }

    public AsymmetricKeyPair getSignPair() {
        return signingPair;
    }

    public AsymmetricKeyPair getCipherPair() {
        return cipherPair;
    }

    public void setProver(Car p){
        prover = p;
    }
}