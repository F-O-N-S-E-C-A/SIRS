import java.net.Socket;
import java.security.*;
public class Client {
    private PublicKey publicKey;
    private Location lastLocation;
    private Socket socket;

    public Client(Socket socket, PublicKey publicKey, Location loc){
        this.socket = socket;
        this.publicKey = publicKey;
        this.lastLocation = loc;
    }

    public Socket getSocket(){
        return socket;
    }

    public Location getLocation(){
        return lastLocation;
    }

    public PublicKey getpublicKey(){
        return publicKey;
    }

}
