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
            System.out.println(request.getId());

            if (request.getType().equals("witness_proof")){
                System.out.println("witness request received");
                //TODO
                server.sendCertificate(request.getProverID());
            } else if (request.getType().equals("request_timestamp")){
                request.setTimeStamp("10:30");
                request.setId(server.getID());
                hs.send(request);
            }else if (request.getType().equals("session_key")){
                request.setTimeStamp("10:30");
                request.setId(server.getID());
                hs.send(request);
            } else {
                System.err.println("Type not specified");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
