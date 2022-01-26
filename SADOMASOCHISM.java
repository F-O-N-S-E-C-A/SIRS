import java.io.IOException;
import java.security.Key;
import java.util.HashMap;
import java.util.UUID;

public class SADOMASOCHISM {
    public static void main(String []args) throws IOException, ClassNotFoundException {
        HashMap<UUID, Key[]> hm = (HashMap<UUID, Key[]>) Simulator.readPublicKeysHashMap();
    }
}
