import java.io.Serializable;
import java.security.PublicKey;

public class Request implements Serializable{
    private String type;
    private String timeStamp;
    private PublicKey carCipherPublicKey;
    private PublicKey carSignPublicKey;

    public Request(String type) {
        this.type = type;
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

    public String getType(){
        return type;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String t) {
        this.timeStamp = t;
    }

    public void setType(String t){
        type = t;
    }

}

