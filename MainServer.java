import java.io.IOException;

public class MainServer {
    public static void main(String []args) throws IOException, ClassNotFoundException {
        System.out.println("ServerSide");
        new Server().serve();
    }
}
