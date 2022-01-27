import java.io.*;

public class CipheredObject implements Serializable {
    private byte[] cipheredBytes;
    private byte[] signature;

    public CipheredObject(byte[] b) {
        cipheredBytes = b;
    }

    public byte[] getCipheredBytes() {
        return cipheredBytes;
    }

    public void setSignature(byte [] s){
        this.signature = s;
    }

    public byte[] getSignature() {
        return signature;
    }
}
