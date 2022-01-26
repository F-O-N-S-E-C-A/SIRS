import java.io.*;

public class CipheredObject implements Serializable {
    private byte[] cipheredBytes;

    public CipheredObject(byte[] b) {
        cipheredBytes = b;
    }

    public byte[] getCipheredBytes() {
        return cipheredBytes;
    }
}
