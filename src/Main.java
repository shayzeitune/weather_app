import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main {
    static String targetLocation = "Jerusalem"; // Change this to filter by a different location

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        // Get current time in Israel time zone
        LocalDateTime now = LocalDateTime.now();
        turnToUnix(now);
        String timeString = ""; // Example time in Israel time (summer)
        if (!whenIsRain(getForecastData()).equals("0")) {
            // if there is rain in the upcoming days print the time when it is going to rain
            System.out.println(" it is going to rain rain in " + targetLocation + "at: " + whenIsRain(getForecastData()));
            timeString = whenIsRain(getForecastData());
        }
        else{
            System.out.println("there is no rain soon");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Parse string to LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(timeString, formatter);
        turnToUnix(dateTime);

        if(turnToUnix(dateTime)< turnToUnix(now)){
            System.out.println(" bhahahahahahahaahahahahahahahajjj");
        }

        // Convert to Israel time zone
//        long unixTimestamp = dateTime.atZone(ZoneId.of("Asia/Jerusalem")).toEpochSecond();

//        System.out.println("Unix Timestamp (Israel Time converted to UTC): " + unixTimestamp);


//        ZonedDateTime israelTime = now.atZone(ZoneId.of("Asia/Jerusalem")); // Convert to Israel time
//        ZonedDateTime utcTime = israelTime.withZoneSameInstant(ZoneOffset.UTC); // Convert to UTC
//        long unixTimestamp = utcTime.toEpochSecond();
//        System.out.println("Unix Timestamp (UTC): " + unixTimestamp);
//        System.out.println("Israel Time: " + israelTime);
//        System.out.println("UTC Time: " + utcTime);
//        String timeString = whenIsRain(getForecastData()); // Example time string
//
//        // Define the format that matches your string
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        // Parse the string into LocalDateTime
//        LocalDateTime dateTime = LocalDateTime.parse(timeString, formatter);
//        ZonedDateTime rainTime = dateTime.atZone(ZoneId.of("Asia/Jerusalem")); // Convert to Israel time
//        ZonedDateTime utcRainTime = israelTime.withZoneSameInstant(ZoneOffset.UTC); // Convert to UTC
//        long unixRainTimestamp = utcTime.toEpochSecond();






        // Convert to Unix timestamp (seconds)




/// Print the data array
//        System.out.println(" Forecasts data for " + targetLocation + ": " + getForecastData());
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
                break; // Exit the loopi once the location is found
            }
        }
        return forecastsData;

    }

    public static String whenIsRain(ArrayList<Forecast> forecastData){
        // check if there is rain during the upcoming days in the relevant location.
       for (int i = 0; i< forecastData.size(); i++)
           if (forecastData.get(i).rain > 0){
               return forecastData.get(i).time;
           }

        return "0";
    }

    public static long turnToUnix(LocalDateTime time)
            /// turn the time into unix format.
    {
        ZonedDateTime israelTime = time.atZone(ZoneId.of("Asia/Jerusalem")); // Convert to Israel time
        ZonedDateTime utcTime = israelTime.withZoneSameInstant(ZoneOffset.UTC); // Convert to UTC
        long unixTimestamp = utcTime.toEpochSecond();
       System.out.println("Unix Timestamp (UTC): " + unixTimestamp);
       System.out.println("Israel Time: " + israelTime);
       System.out.println("UTC Time: " + utcTime);
       return unixTimestamp;

    }

}