import java.io.*;
import java.net.Socket;
public class Handler implements Runnable{
    private Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            Message request = (Message) objectInputStream.readObject();
            System.out.println(request.message);

            objectOutputStream.writeObject(new Message("adeus"));

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
}
