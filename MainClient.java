import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
public class MainClient {
    public static void main(String []args) throws Exception {
        System.out.println("ClientSide");
        new Car(new Location(20.0, 20.0)).requestProofOfLocation();
    }
}
