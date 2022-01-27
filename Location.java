import java.io.Serializable;

public class Location implements Serializable{
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(String loc){
        String [] splt = loc.split("-");
        this.latitude = Double.parseDouble(splt[0]);
        this.longitude = Double.parseDouble(splt[1]);
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

    public String getStringLoc(){
        return Double.toString(latitude) + "-" + Double.toString(longitude);
    }

}
