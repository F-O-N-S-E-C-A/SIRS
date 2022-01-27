public class MainClient {
    public static void main(String []args) throws Exception {
        System.out.println("ClientSide");
        Car prover = new Car(new Location(20.0, 20.0));
        Simulator sim = new Simulator(prover, 40);
        prover.requestProofOfLocation(sim);
    }
}
