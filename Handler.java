import java.io.*;
import java.net.Socket;

public class Handler implements Runnable {
    private Socket socket;
    private Server server;

    public Handler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            sendServerKeys(objectOutputStream);

            Request request = (Request) objectInputStream.readObject();

            System.out.println(request.getType());

            if (request.getType().equals("witness_proof")){
                System.out.println("witness request received");
            } else if (request.getType().equals("request_timestamp")){
                request.setTimeStamp("10:30");
                objectOutputStream.writeObject(request);
            } else {
                System.err.println("Type not specified");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                    socket.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendServerKeys(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(server.getSignPublicKey()); // simulation
        objectOutputStream.writeObject(server.getCipherPublicKey()); // simulation}
    }


}
