import java.io.*;
import java.net.Socket;

public class CarHandler implements Runnable {
    private Socket socket;
    private String type;
    private Request request;
    private Car car;

    public CarHandler(Car car, Socket socket, String type) {
        this.socket = socket;
        this.type = type;
        this.car = car;
    }

    public void run() {
        System.out.println("type: " + type);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            System.out.println("car handler");

            if(type.equals("witness")){
                System.out.println("car handler - witness");
                run_witness(inputStream);
            } else if(type.equals("prover")){
                System.out.println("car handler - prover");
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
        System.out.println("request received in witness" + request.getType());
        car.addRequest(request);
        car.witness_sendProofs();
        System.out.println("request received in witness" + request.getType());
    }

    public void run_prover(OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        request.setType("broadcast to witnesses");
        objectOutputStream.writeObject(request);
        System.out.println("request broad casted to witness");
    }

}
