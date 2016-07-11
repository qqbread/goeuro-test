
//_id, name, type, latitude, longitude

public class City {
    private int id;
    private String name;
    private String type;
    private double latitude;
    private double longitude;

    public String toCSV() {
        StringBuilder sb = new StringBuilder(50);
        
        sb.append(id)
          .append(",")
          .append(name)
          .append(",")
          .append(type)
          .append(",")
          .append(latitude)
          .append(",")
          .append(longitude);
        return sb.toString();
    }

    public City(int id, String name, String type, double latitude, double longitude) {
        this.id         = id;
        this.name       = name;
        this.type       = type;
        this.latitude   = latitude;
        this.longitude  = longitude;
    } 
}