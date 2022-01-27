import java.io.*;
import java.security.Key;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class Simulator {
    public static final double max = 40.0;
    public static final double min = 0.0;
    public static final double rangeRadius = 10.0;

    public static final String serverID = "123e4567-e89b-42d3-a456-556642440000";

    private Car prover;
    private LinkedList<Car> cars;

    public Simulator(Car prover, int nCars) throws IOException, ClassNotFoundException {
        this.prover = prover;
        cars = new LinkedList<>();
        for(int i = 0; i < nCars; i++){
            Car car = new Car();
            car.setProver(prover);
            car.setLocation(new Location(rand(), rand()));
            cars.add(car);
        }
        printCars();
    }

    public HashMap<Integer, Car> findWitnesses(int port) {
        HashMap<Integer, Car> witnessses = new HashMap<>();
        for(Car c : cars){
            if(reachable(c, prover)){
                witnessses.put(port, c);
                c.witness_receiveRequest(port);
                port++;
            }
        }
        return witnessses; // return the ports of the witnesses
    }


    public double rand(){
        return min + Math.random() * (max - min);
    }

    public static boolean reachable(Car c1, Car c2){
        return c1.getLocation().distance(c2.getLocation()) < rangeRadius;
    }

    private void printCars(){
        try {
            System.out.println("Prover: \uD83C\uDFCE \nWitness: \uD83D\uDE97 \n Other cars: \uD83D\uDE99");
            String[][] map = new String[(int) max][(int) max];
            for (Car c : cars) {
                int lat = (int) c.getLocation().getLatitude();
                int lon = (int) c.getLocation().getLongitude();
                if (reachable(prover, c)) {
                    map[lat][lon] = "\uD83D\uDE97 ";
                } else {
                    map[lat][lon] = "\uD83D\uDE99 ";
                }

            }
            int latProver = (int) prover.getLocation().getLatitude();
            int lonProver = (int) prover.getLocation().getLongitude();
            map[latProver][lonProver] = "\uD83C\uDFCE️ ";

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (map[i][j] == null) {
                        map[i][j] = "⬛️️ ";
                    }
                }
            }
            System.out.print(" ");
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    System.out.print(map[i][j]);
                }
                System.out.print("\n ");
            }
        } catch (ArrayIndexOutOfBoundsException e){
            return;
        }
    }

    public synchronized static void writePublicKeysToFile(HashMap<UUID, Key[]> hm, UUID id, Key sp, Key cp) throws IOException {
        File public_keys = new File("public_keys");
        if (!public_keys.createNewFile()){
            public_keys.delete();
        }

        hm.put(id, new Key[]{sp, cp});

        FileOutputStream fileOutputStream = new FileOutputStream(public_keys);
        fileOutputStream.write(HybridCipher.serialize(hm));
    }

    public synchronized static void writePublicKeysToFile(UUID id, Key sp, Key cp) throws IOException, ClassNotFoundException {
        File public_keys = new File("public_keys");

        FileInputStream fileInputStream = new FileInputStream(public_keys);
        HashMap hm = (HashMap) HybridCipher.deserialize(fileInputStream.readAllBytes());

        hm.put(id, new Key[]{sp, cp});

        FileOutputStream fileOutputStream = new FileOutputStream(public_keys);
        fileOutputStream.write(HybridCipher.serialize(hm));
    }

    public static Key[] readPublicKeys(UUID id){
        File public_keys = new File("public_keys");
        try {
            FileInputStream fileInputStream = new FileInputStream(public_keys);
            HashMap hm = (HashMap) HybridCipher.deserialize(fileInputStream.readAllBytes());
            return (Key[]) hm.get(id);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<UUID, Key[]> readPublicKeysHashMap() throws IOException, ClassNotFoundException {
        File public_keys = new File("public_keys");

        FileInputStream fileInputStream = new FileInputStream(public_keys);
        return (HashMap<UUID, Key[]>) HybridCipher.deserialize(fileInputStream.readAllBytes());
    }
}
