import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.Scanner;
import org.json.*;
import java.util.LinkedList;
import java.io.*;

public class GoEuroTest {
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar GoEuroTest.jar \"CITY_NAME\" ");
        }
        else {
            String cityName = args[0];
            System.out.println("[*] City Name: " + cityName);
            CityQuery cq = new CityQuery(cityName, cityName+".csv");
            boolean succeed = cq.queryAndOutputCSV();
            if (succeed) {
                System.out.println("[+] Success");
            }
        }
    }
}