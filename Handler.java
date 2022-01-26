import java.io.*;
import java.net.Socket;

public class Handler implements Runnable {
    private Socket socket;
    private Server server;
    private HybridCipher hs;

    public Handler(Socket socket, Server server) throws Exception {
        this.socket = socket;
        this.server = server;

        this.hs = new HybridCipher(server.getSignPair(), server.getCipherPair(), this.socket);
    }

    public void run() {
        try {
            Request request = hs.receive();

            if (request.getType().equals("witness_proof")){
                System.out.println("witness request received");
                server.sendCertificate(0);
            } else if (request.getType().equals("request_timestamp")){
                request.setTimeStamp("10:30");
                hs.send(request);
            }else if (request.getType().equals("session_key")){
                request.setTimeStamp("10:30");
                hs.send(request);
            } else {
                System.err.println("Type not specified");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendServerKeys(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(server.getSignPublicKey()); // simulation
        objectOutputStream.writeObject(server.getCipherPublicKey()); // simulation}
    }
}
