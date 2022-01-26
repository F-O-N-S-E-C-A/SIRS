import java.util.HashMap;
import java.util.LinkedList;

public class Simulator {
    public static final double max = 40.0;
    public static final double min = 0.0;
    public static final double rangeRadius = 10.0;

    private Car prover;
    private LinkedList<Car> cars;
    private HashMap <Integer, Car> witnessses;

    public Simulator(Car prover, int nCars){
        this.prover = prover;
        cars = new LinkedList<>();
        for(int i = 0; i < nCars; i++){
            Car car = new Car();
            car.setLocation(new Location(rand(), rand()));
            cars.add(car);
        }
        printCars();
    }

    public HashMap<Integer, Car> findWitnesses(int port){
        witnessses = new HashMap<>();
        for(Car c : cars){
            if(reachable(c, prover)){
                witnessses.put(port, c);
                main(new String []{Double.toString(c.getLocation().getLatitude()), Double.toString(c.getLocation().getLongitude()), Integer.toString(port)});
                port++;
            }
        }
        return witnessses; // return the ports of the witnesses
    }

    public HashMap <Integer, Car> getWitnessses(){
        return witnessses;
    }


    public static void main(String [] args){
        Car witness = new Car(new Location(Double.parseDouble(args[0]), Double.parseDouble(args[1])));
        witness.witness_receiveRequest(Integer.parseInt(args[2]));
    }

    public double rand(){
        return min + Math.random() * (max - min);
    }

    public static boolean reachable(Car c1, Car c2){
        return c1.getLocation().distance(c2.getLocation()) < rangeRadius;
    }

    private void printCars(){
        System.out.println("Prover: \uD83C\uDFCE \nWitness: \uD83D\uDE97 \n Other cars: \uD83D\uDE99");
        String [][] map = new String[(int)max][(int)max];
        for(Car c: cars){
            int lat = (int) c.getLocation().getLatitude();
            int lon = (int) c.getLocation().getLongitude();
            if (reachable(prover, c)){
                map[lat][lon] = "\uD83D\uDE97 ";
            } else {
                map[lat][lon] = "\uD83D\uDE99 ";
            }

        }
        int latProver = (int) prover.getLocation().getLatitude();
        int lonProver = (int) prover.getLocation().getLongitude();
        map[latProver][lonProver] = "\uD83C\uDFCE️ ";

        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                if(map[i][j] == null){
                    map[i][j] = "⬛️️ ";
                }
            }
        }
        System.out.print(" ");
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.print("\n ");
        }
    }

}
