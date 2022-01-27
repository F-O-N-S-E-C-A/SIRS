import javax.naming.SizeLimitExceededException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.UUID;

public class WaitForWitnesses implements Runnable{
    UUID proverID;
    Server server;
    Location proverLoc;
    Timestamp sentTime;

    public static final int TIMEOUT = 5000;

    public WaitForWitnesses(UUID proverID, Server server, Location proverLoc, Timestamp t){
        this.proverID = proverID;
        this.server = server;
        this.proverLoc = proverLoc;
        sentTime = t;
    }

    public void run() {
        try {
            Thread.sleep(TIMEOUT);

            System.out.println("-------crnv3bvubvo2boiv");

            LinkedList<Request> reports = server.getRequestsFromProver(proverID);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
