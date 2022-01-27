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
            hs.setReceiverPubKeys(Simulator.readPublicKeys(receiver.getID()));
            if (type.equals("witness")) {
                run_witness();
            } else if (type.equals("prover")) {
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
        System.out.println("Witness - Request of validation received");
        car.addRequest(request);
        car.witness_sendProofs();
    }

    public void run_prover() throws IOException {
        request.setSender(car.getID(), "broadcast to witnesses");
        hs.send(request);
        hs.closeSocket();
    }

}
