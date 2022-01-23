import java.net.*;
import java.io.*;
import java.security.PublicKey;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Car {
    private Location loc;
    private AsymmetricKeyPair signingPair;
    private AsymmetricKeyPair cipherPair;
    private Socket socket;
    private PublicKey serverSignPublicKey;
    private PublicKey serverCipherPublicKey;
    private String host = "localhost";


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

    public String getHost() {
        return host;
    }


    public void requestProofOfLocation() {
        Request response = null;
        try {
            socket = new Socket("localhost", 2000);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // create Message with Request of Proof of location

            setServerKeys(objectInputStream);

            Request request = new Request("Request of proof of location");


            objectOutputStream.writeObject(request);

            response = (Request) objectInputStream.readObject();
            System.out.println("response got from server " + response.getType() + "  " + response.getTimeStamp());

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (response != null) {
            requestWitness(response);
        }

    }

    public void beWitness(int port){
        new Thread() {
            public void run(){
                ServerSocket ss = null;
                try {
                    ss = new ServerSocket(port);
                    ss.setReuseAddress(true);

                    while (true) {
                        Socket socket = ss.accept();
                        System.out.println("New client connected");
                        new Thread(new CarHandler(socket, "witness")).start();
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

    public void requestWitness(Request r){
        Simulator sim = new Simulator(this, 20);
        HashMap<Integer, Car> witnesses = sim.findWitnesses(3000);
        for(int port : witnesses.keySet()){
            String host = witnesses.get(port).getHost();
            // make socket connections
            try {
                //System.out.println("request witness");
                socket = new Socket(host, port);
                System.out.println("request witness");
                CarHandler thread = new CarHandler(socket, "prover");
                thread.setRequest(r);
                new Thread(thread).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    private void setServerKeys(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        serverSignPublicKey = (PublicKey) objectInputStream.readObject(); // simulation
        serverCipherPublicKey = (PublicKey) objectInputStream.readObject(); // simulation
    }
}