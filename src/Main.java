import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Main {
    static String targetLocation = "Jerusalem"; // Change this to filter by a different location

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        LocalDate localDate = LocalDate.now();
        LocalTime now = LocalTime.now();
        System.out.println(now);


/// Print the rain data array
        System.out.println("Rain data for " + targetLocation + ": " + getRainData());
///   if there is a rain in somewhere in the future in the location chosen
        if (isRain(getRainData())){

        }

    }

    public static Document getParsedDoc() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse("https://ims.gov.il/sites/default/files/ims_data/xml_files/isr_cities_1week_6hr_forecast.xml");
        doc.getDocumentElement().normalize();
        return doc;
    }


    public static ArrayList<Double> getRainData() throws IOException, ParserConfigurationException, SAXException {
        Document doc = getParsedDoc();
        ArrayList<Double> rainData = new ArrayList<>(); // To store rain data

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

                    // Extract the <Rain> element value and add to the array
                    String rainValue = forecast.getElementsByTagName("Rain").item(0).getTextContent();
                    rainData.add(Double.parseDouble(rainValue));
                }
                break; // Exit the loop once the location is found
            }
        }
        return rainData;

    }

    public static Boolean isRain(ArrayList<Double> rainData){
       for (int i = 0; i< rainData.size(); i++)
           if (rainData.get(i) > 0) {
               return true;
           }

        return false;
    }

}
