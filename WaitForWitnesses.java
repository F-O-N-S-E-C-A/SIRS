import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.UUID;

public class WaitForWitnesses implements Runnable{
    private UUID proverID;
    private Server server;
    private Location proverLoc;

    public static final int TIMEOUT = 5000;
    public static final int TIME_LIMIT = TIMEOUT + 1000;
    public static final double minimumDistance = Simulator.rangeRadius;

    public WaitForWitnesses(UUID proverID, Server server, Location proverLoc){
        this.proverID = proverID;
        this.server = server;
        this.proverLoc = proverLoc;
    }

    public void run() {
        try {
            System.out.println("Server - Begin timeout: " + TIMEOUT/1000 + "s");
            Thread.sleep(TIMEOUT);
            System.out.println("Server - End of timeout, processing request");
            LinkedList<MillenniumFalcon> reports = server.getRequestsFromProver(proverID);
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if(reports != null) {
                int minWitnesses = (reports.size() / 2) + 1;
                int validWitnesses = 0;
                for (MillenniumFalcon r : reports) {
                    if (AsymmetricKeyPair.verifySignature(server.getSignPublicKey(), r.getTimestampSignature(), r.getTimeStamp())) {
                        if (currentTime.getTime() - r.getTimeStamp().getTime() <= TIME_LIMIT) {
                            if (r.getLocation().distance(proverLoc) <= minimumDistance) {
                                validWitnesses++;
                            }
                        }
                    } else {
                        System.err.println("Invalid timestamp signature");
                    }
                }
                if (validWitnesses >= minWitnesses && minWitnesses >= 2) {
                    server.sendCertificate(proverID, true, proverLoc);
                    System.out.println("\u001B[42m" + "*** LOCATION APPROVED! ***" + "\u001B[0m");
                } else {
                    System.err.println("Not enough witnesses!");
                    server.sendCertificate(proverID, false, proverLoc);
                }
            } else {
                System.err.println("No witnesses can vouch for prover");
                server.sendCertificate(proverID, false, proverLoc);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
