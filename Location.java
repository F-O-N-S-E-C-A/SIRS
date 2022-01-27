import java.io.Serializable;

public class Location implements Serializable{
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double distance(Location l){
        return Math.sqrt(((latitude - l.getLatitude())*(latitude - l.getLatitude())) + ((longitude - l.getLongitude())*(longitude - l.getLongitude())));
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
