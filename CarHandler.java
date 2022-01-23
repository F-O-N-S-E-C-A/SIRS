import java.io.*;
import java.net.Socket;

public class CarHandler implements Runnable {
    private Socket socket;
    private String type;
    private Request request;

    public CarHandler(Socket socket, String type) {
        this.socket = socket;
        this.type = type;
    }

    public void run() {
        System.out.println("type: " + type);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            System.out.println("Request sent to witness");

            if(type.equals("witness")){
                run_witness(inputStream);
            } else if(type.equals("prover")){
                run_prover(outputStream);
            }

        } catch (IOException | ClassNotFoundException e) {
        //} catch (IOException e) {
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

    public void setRequest(Request request){
        this.request = request;
    }

    public void run_witness(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Request request = (Request) objectInputStream.readObject();
        System.out.println("request received" + request.getType());
    }

    public void run_prover(OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        request.setType("broadcast to witnesses");
        objectOutputStream.writeObject(request);
    }

}
