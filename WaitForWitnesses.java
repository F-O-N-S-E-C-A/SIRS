import javax.naming.SizeLimitExceededException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.UUID;

public class WaitForWitnesses implements Runnable{
    private UUID proverID;
    private Server server;
    private Location proverLoc;
    private Timestamp sentTime;

    public static final int TIMEOUT = 5000;
    public static final int TIME_LIMIT = TIMEOUT + 1000;
    public static final double minimumDistance = Simulator.rangeRadius;

    public WaitForWitnesses(UUID proverID, Server server, Location proverLoc, Timestamp t){
        this.proverID = proverID;
        this.server = server;
        this.proverLoc = proverLoc;
        sentTime = t;
    }

    public void run() {
        try {
            Thread.sleep(TIMEOUT);

            LinkedList<Request> reports = server.getRequestsFromProver(proverID);
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            int minWitnesses = (reports.size() / 2) + 1;
            int  validWitnesses = 0;
            for(Request r: reports){
                if(AsymmetricKeyPair.verifySignature(server.getSignPublicKey(), r.getTimestampSignature(), r.getTimeStamp())){
                    if(currentTime.getTime() - r.getTimeStamp().getTime() <= TIME_LIMIT){
                        if(r.getLocation().distance(proverLoc) <= minimumDistance){
                            validWitnesses++;
                        }
                    }
                } else {
                    System.err.println("Invalid timestamp signature");
                }
            }
            if (validWitnesses >= minWitnesses && minWitnesses >= 2){
                server.sendCertificate(proverID);
                System.out.println("*** LOCATION APPROVED! ***");
            } else {
                System.err.println("Not enough witnesses!");
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
