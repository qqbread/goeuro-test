import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.Scanner;
import org.json.*;
import java.util.LinkedList;
import java.io.*;

public class CityQuery {
    private String cityName;
    private String outputFile;

	/*
     * Get the content from the API endpoint, the output is a Json String
     */  
    public String queryFromUrl(String cityName) {
        URL url = null;
        String stringCityData = new String();
        try {
            url = new URL("http://api.goeuro.com/api/v2/position/suggest/en/"+cityName);

            try {
            Scanner scan = new Scanner(url.openStream());
            while (scan.hasNext())
                stringCityData += scan.nextLine();
            scan.close();
            }
            catch(IOException e) {
                System.out.println("[-] Error: URL FAILED");
            }
        }
        catch(MalformedURLException e) {
            System.out.println("[-] Error: MALFORMED URL");
        }
        
        return stringCityData;
    }

    /*
     * Get the required fields from each Json object and make it a City object
     * Required fields: _id, name, type, latitude, longitude
     */
    public City jsonToCity(JSONObject jsonCity) {
        City city = null;
        try {
            int id = jsonCity.getInt("_id");
            String name = jsonCity.getString("name");
            String type = jsonCity.getString("type");
            JSONObject jsonGeoPosition = jsonCity.getJSONObject("geo_position");
            double latitude = jsonGeoPosition.getDouble("latitude");
            double longitude = jsonGeoPosition.getDouble("longitude");
            city = new City(id, name, type, latitude, longitude);
        }
        catch (JSONException js) {
            System.out.println("[-] Error: MISSING JSON VALUE");
        }
        return city;
    }
 

    /*
     *  Process the Json Array to a List, each element gets the required fields 
     */
    public LinkedList<City> getCityData(JSONArray jsonCityData) {
        LinkedList<City> listCity = new LinkedList<City>();
        for(int i = 0; i < jsonCityData.length(); ++i) {
            City tmpCity = jsonToCity(jsonCityData.getJSONObject(i));
            if(tmpCity == null) {
                return null;
            }
            listCity.add(tmpCity);    
        }
        return listCity;
    }

    /*
     * Output the city list to the file;
     */
    private boolean writeToFile(LinkedList<City> listCity) {
        File output = new File(outputFile);
        try (FileOutputStream fos = new FileOutputStream(output, false)) {
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fos));
            br.write("_id,name,type,latitude,longitude\n");
            for (City c : listCity ) {
                br.write(c.toCSV());
                br.write("\n");
            }
            br.flush();
            fos.flush();
        }
        catch(Exception e) {
            System.out.println("[-] Error: WRITING FILE");
            return false;
        }
        return true;
    }

    /*
    * Get the Json String from url, convert the string to a Json Array, get the required fields per city, output to a CSV file
    * Output: "CITY_NAME.csv" in the current directory
    */
    public boolean queryAndOutputCSV() {
        String stringCityData = queryFromUrl(cityName);
        if(stringCityData.length() == 0 || stringCityData.equals("[]")) {
            System.out.println("[-] Error: NO DATA");
            return false;
        }

        //Parse the Json String to a Json Array
        JSONArray jsonCityData = null;
        try {
            jsonCityData = new JSONArray(stringCityData);    
        }
        catch (JSONException js) {
            System.out.println("[-] Error: JSON PARSER");
            return false;   
        }     

        LinkedList<City> cityData = getCityData(jsonCityData);
        if(cityData == null) {
            return false;
        }

        return writeToFile(cityData);
    }

	public CityQuery(String cityName, String outputFile) {
        this.cityName = cityName;
        this.outputFile = outputFile;
    }
}