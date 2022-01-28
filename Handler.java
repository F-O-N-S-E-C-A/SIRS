import java.net.Socket;
import java.sql.Timestamp;
import java.util.UUID;

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
            MillenniumFalcon payload = hs.receive();

            if (payload.getType().equals("witness_proof")){
                System.out.println("Server Handler - witness report received");

                UUID proverID = (UUID) HybridCipher.deserialize(server.getCipherPair().decipher(payload.getProverID()));
                server.addWitnessReport(proverID, payload);

            } else if (payload.getType().equals("request_timestamp")){
                System.out.println("Server Handler - request timestamp received from prover");
                Location proverLoc = payload.getLocation();
                payload.setLocation(null);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                payload.setTimeStamp(ts);
                payload.signTimestamp(server.getSignPair().sign(ts));
                UUID proverID = payload.getId();
                byte[] cipheredPorverID = Cipher.asymmetricCipher(HybridCipher.serialize(proverID), server.getCipherPublicKey());
                payload.setProverID(cipheredPorverID);
                payload.setId(server.getID());
                hs.send(payload);
                new Thread(new WaitForWitnesses(proverID, server, proverLoc)).start();

            } else {
                System.err.println("Type not specified");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
