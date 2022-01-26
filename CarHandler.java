import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class CarHandler implements Runnable {
    private Socket socket;
    private String type;
    private Request request;
    private Car car;
    private Car receiver;
    private HybridCipher hs;

    public CarHandler(Car car, Socket socket, String type) {
        this.socket = socket;
        this.type = type;
        this.car = car;
        try {
            this.hs = new HybridCipher(car.getSignPair(), car.getCipherPair(), this.socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            if (type.equals("witness")) {
                hs.setReceiverPubKeys(Simulator.readPublicKeys(UUID.fromString(Simulator.serverID)));
                System.out.println("car handler - witness");
                run_witness();
            } else if (type.equals("prover")) {
                hs.setReceiverPubKeys(Simulator.readPublicKeys(receiver.getID()));
                System.out.println("car handler - prover");
                run_prover();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setReceiver(Car c) {
        this.receiver = c;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void run_witness() throws IOException, ClassNotFoundException {
        Request request = hs.receive();
        System.out.println("request received in witness" + request.getType());
        car.addRequest(request);
        car.witness_sendProofs();
        System.out.println("request received in witness" + request.getType());
    }

    public void run_prover() throws IOException {
        request.setSender(car.getID(), "broadcast to witnesses");
        hs.send(request);
        hs.closeSocket();
        System.out.println("request broad casted to witness");
    }

}
