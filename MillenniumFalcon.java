import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

public class MillenniumFalcon implements Serializable {
    private String type;
    private Timestamp timeStamp; // prover UID and timestamp
    private byte [] timeStampSignature;

    private Location location; //cipher with server public key
    private UUID id;
    private byte[] proverID;

    private String certificate;
    private byte[] certificateSignature;

    public MillenniumFalcon(UUID id, String type) {
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

    public byte[] getProverID(){
        return proverID;
    }
    public void setProverID(byte[] id) {
        this.proverID = id;
    }

}

