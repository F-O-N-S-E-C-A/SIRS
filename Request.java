import java.io.Serializable;
import java.security.PublicKey;
import java.util.UUID;

public class Request implements Serializable {
    private String type;
    private String timeStamp; // prover UID and timestamp
    private PublicKey carCipherPublicKey;
    private PublicKey carSignPublicKey;
    private String witnessLocation; //cipher with server public key
    private String proverLocation; //cipher with server public key
    private UUID id;
    private UUID proverID;

    public Request(UUID id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getWitnessLocation() {
        return witnessLocation;
    }

    public String getProverLocation() {
        return proverLocation;
    }

    public void setProverLocation(String proverLocation) {
        this.proverLocation = proverLocation;
    }


    public PublicKey getCarCipherPublicKey() {
        return carCipherPublicKey;
    }

    public PublicKey getCarSignPublicKey() {
        return carSignPublicKey;
    }

    public void setCarCipherPublicKey(PublicKey carCipherPublicKey) {
        this.carCipherPublicKey = carCipherPublicKey;
    }

    public void setCarSignPublicKey(PublicKey carCipherPublicKey) {
        this.carSignPublicKey = carCipherPublicKey;
    }

    public String getType() {
        return type;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String t) {
        this.timeStamp = t;
    }

    public void setType(String t) {
        type = t;
    }

    public void setWitnessLocation(String l) {
        this.witnessLocation = l;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setProverID(UUID id) {
        this.proverID = id;
    }

    public void setSender(UUID id, String type){
        this.id = id;
        this.type = type;
    }

    public UUID getProverID(){
        return proverID;
    }

}

