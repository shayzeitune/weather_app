import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Main {
    static String targetLocation = "Jerusalem"; // Change this to filter by a different location

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        LocalDate localDate = LocalDate.now();
        LocalTime now = LocalTime.now();
        System.out.println(now);
        System.out.println(isRain(getForecastData()));


/// Print the data array
        System.out.println(" Forecasts data for " + targetLocation + ": " + getForecastData());
///   if there is a rain in somewhere in the future in the location chosen
//        if (isRain(getForecastData())){
//            System.out.println("hi");
//
//        }

    }

    public static Document getParsedDoc() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse("https://ims.gov.il/sites/default/files/ims_data/xml_files/isr_cities_1week_6hr_forecast.xml");
        doc.getDocumentElement().normalize();
        return doc;
    }


    public static ArrayList<Forecast> getForecastData() throws IOException, ParserConfigurationException, SAXException {
        Document doc = getParsedDoc();
//        ArrayList<Double> rainData = new ArrayList<>(); // To store rain data
        ArrayList<Forecast> forecastsData = new ArrayList<>(); //to store all the data about a forecast-time, rain, temp

        NodeList locations = doc.getElementsByTagName("Location"); // Get all <Location> elements
        for (int i = 0; i < locations.getLength(); i++) {
            Element location = (Element) locations.item(i);

            // Get the <LocationNameEng> element and check its value
            String locationName = location.getElementsByTagName("LocationNameEng").item(0).getTextContent();
            if (targetLocation.equalsIgnoreCase(locationName)) {
                // We found the target location, get <Forecast> elements within it
                NodeList forecasts = location.getElementsByTagName("Forecast");
                for (int j = 0; j < forecasts.getLength(); j++) {
                    Element forecast = (Element) forecasts.item(j);
                    // Extract the elements values
                    String rainValue = forecast.getElementsByTagName("Rain").item(0).getTextContent();
                    String time = forecast.getElementsByTagName("ForecastTime").item(0).getTextContent();
                    String temperature = forecast.getElementsByTagName("Temperature").item(0).getTextContent();
                    String humidity = forecast.getElementsByTagName("RelativeHumidity").item(0).getTextContent();
                    // create a forecast object and add to the array
                    Forecast forecast1 = new Forecast(time,Double.parseDouble(rainValue), Double.parseDouble(temperature), humidity);
                    forecastsData.add(forecast1);
//                    rainData.add(Double.parseDouble(rainValue));
                }
                break; // Exit the loop once the location is found
            }
        }
        return forecastsData;

    }

    public static Boolean isRain(ArrayList<Forecast> forecastData){
       for (int i = 0; i< forecastData.size(); i++)
           if (forecastData.get(i).rain > 0){
               return true;
           }

        return false;
    }

}
