import java.net.Socket;
import java.sql.Timestamp;

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
                System.out.println(request.getLocation());
                server.addWitnessReport(request.getProverID(), request);


            } else if (request.getType().equals("request_timestamp")){
                Location proverLoc = request.getLocation(); //TODO
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
