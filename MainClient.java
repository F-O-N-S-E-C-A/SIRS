public class MainClient {
    public static void main(String []args){
        System.out.println("ClientSide");
        new Car(new Location(20.0, 20.0)).requestProofOfLocation();
    }
}
