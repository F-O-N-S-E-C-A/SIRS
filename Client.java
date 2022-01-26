import java.security.*;
public class Client {
    private PublicKey signPublicKey;
    private PublicKey cipherPublicKey;
    private Location lastLocation;
    private int id;
    private String host;

    public Client(PublicKey signPublicKey, PublicKey cipherPublicKey) {
        this.signPublicKey = signPublicKey;
        this.cipherPublicKey = cipherPublicKey;
    }

    public PublicKey getSignPublicKey() {
        return signPublicKey;
    }

    public void setSignPublicKey(PublicKey signPublicKey) {
        this.signPublicKey = signPublicKey;
    }

    public PublicKey getCipherPublicKey() {
        return cipherPublicKey;
    }

    public void setCipherPublicKey(PublicKey cipherPublicKey) {
        this.cipherPublicKey = cipherPublicKey;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
