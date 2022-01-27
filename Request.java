import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

public class Request implements Serializable {
    private String type;
    private Timestamp timeStamp; // prover UID and timestamp
    private byte [] timeStampSignature;

    private Location location; //cipher with server public key
    private UUID id;
    private UUID proverID;

    private String certificate;
    private byte[] certificateSignature;

    public Request(UUID id, String type) {
        this.id = id;
        this.type = type;
    }

    public void setCertificate(String t, byte[] sig){
        this.certificate = t;
        this.certificateSignature = sig;
    }

    public String getCertificate() {
        return certificate;
    }

    public byte[] getCertificateSignature() {
        return certificateSignature;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp t) {
        this.timeStamp = t;
    }

    public void setType(String t) {
        type = t;
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

    public byte[] getTimestampSignature() {
        return timeStampSignature;
    }

    public void signTimestamp(byte[] timeStampSignature) {
        this.timeStampSignature = timeStampSignature;
    }

    public UUID getProverID(){
        return proverID;
    }

}

