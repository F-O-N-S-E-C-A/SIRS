import java.net.Socket;
import java.sql.Timestamp;

public class Handler implements Runnable {
    private Socket socket;
    private Server server;
    private HybridCipher hs;

    public Handler(Socket socket, Server server){
        this.socket = socket;
        this.server = server;

        this.hs = new HybridCipher(server.getSignPair(), server.getCipherPair(), this.socket);
    }

    public void run() {
        try {
            Request request = hs.receive();

            if (request.getType().equals("witness_proof")){
                System.out.println("Server Handler - witness report received");
                server.addWitnessReport(request.getProverID(), request);

            } else if (request.getType().equals("request_timestamp")){
                System.out.println("Server Handler - request timestamp received from prover");
                Location proverLoc = request.getLocation();
                request.setLocation(null);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                request.setTimeStamp(ts);
                request.signTimestamp(server.getSignPair().sign(ts));
                request.setId(server.getID());
                hs.send(request);
                new Thread(new WaitForWitnesses(request.getProverID(), server, proverLoc, ts)).start();

            } else {
                System.err.println("Type not specified");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
