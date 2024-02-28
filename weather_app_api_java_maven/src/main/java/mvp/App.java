package mvp;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   

import java.util.Map;



public class App {

    public static void getdayData(String nextDate, String data, String userVariables) {
        Dag nyDag = new Dag();

        nyDag.etapper[0].setTimeStart(0);
        nyDag.etapper[0].setTimeEnd(8);
        nyDag.etapper[1].setTimeStart(8);
        nyDag.etapper[1].setTimeEnd(12);
        nyDag.etapper[2].setTimeStart(12);
        nyDag.etapper[2].setTimeEnd(18);
        nyDag.etapper[3].setTimeStart(18);
        nyDag.etapper[3].setTimeEnd(24);



        nyDag.setBrukerVariabler(userVariables);
        nyDag.setDato(nextDate);
        // System.out.println(LocalDate.parse(nextDate).getDay());
       //  LocalDate today = LocalDate.now();
        // System.out.println(today);
        // System.out.println(LocalDate.parse(nextDate).getDayOfWeek().getValue());

        // -------------------------------

        
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject)jsonParser.parse(data);
        // System.out.println(response.body());
        
        JsonObject properties = jo.get("properties").getAsJsonObject();
        // JsonArray timeSeries = properties.get("timeseries").getAsJsonArray();
        JsonArray timeSeries = properties.getAsJsonArray("timeseries");

        // System.out.println(timeSeries.get(1));
        // System.out.println(timeSeries);

        

        for (JsonElement ts : timeSeries) {
            // System.out.println(ts.getAsJsonObject().get("time"));

            String tempDato = ts.getAsJsonObject().get("time").toString();
            String checkDate = tempDato.substring(1,11);
            
            if ( nextDate.equals(checkDate)) {
                // System.out.println(nextDate + "|" + checkDate);


                JsonObject dataTemp = ts.getAsJsonObject().get("data").getAsJsonObject();
                JsonObject instantTemp = dataTemp.get("instant").getAsJsonObject();
                JsonObject detailsTemp = instantTemp.get("details").getAsJsonObject();
                
                JsonObject nextOneHours;
                JsonObject nextSixHours;


                String regn = "0"; 
                if (dataTemp.get("next_1_hours") != null) {
                    nextOneHours = dataTemp.get("next_1_hours").getAsJsonObject();
                    JsonObject localDetailsTemp = nextOneHours.get("details").getAsJsonObject();
                    regn = localDetailsTemp.get("precipitation_amount").toString();
                } else if (dataTemp.get("next_6_hours") != null) {
                    // nextOneHours = dataTemp.get("next_6_hours").getAsJsonObject();
                    nextSixHours = dataTemp.get("next_6_hours").getAsJsonObject();                
                    JsonObject localDetailsTemp = nextSixHours.get("details").getAsJsonObject();
                    regn = localDetailsTemp.get("precipitation_amount").toString();
                }
                
                
                Integer tid = Integer.parseInt(tempDato.substring(12,14));
                String airTemperature = detailsTemp.get("air_temperature").toString();
                String wind_speed = detailsTemp.get("wind_speed").toString();
                
                nyDag.setTotalRegn(Float.parseFloat(regn));
                nyDag.setTotalTemp(Float.parseFloat(airTemperature));
                nyDag.setTotalVind(Float.parseFloat(wind_speed));
                nyDag.setCount();


                for (Etappe etappe : nyDag.etapper) {

                    if ( tid >= etappe.getTimeStart() && tid < etappe.getTimeEnd()){
                        etappe.setHighLow(Float.parseFloat(airTemperature));
                        etappe.setCount();
                        etappe.setRegn(Float.parseFloat(regn));
                        etappe.setTemp(Float.parseFloat(airTemperature));
                        etappe.setVind(Float.parseFloat(wind_speed));
                    }

                }
                
                
                // float temperatur = parseFloat(ts.getAsJsonObject().get("data").getAsJsonObject().get("instant").getAsJsonObject().get("details").getAsJsonObject().get("air_temperature"));
                // System.out.println(tid);
                // System.out.println(wind_speed);



            }

        }


        nyDag.display();
    }

    public static void getWeek(String data, String userVariables){
        LocalDateTime dato = LocalDateTime.now(); 
        DateTimeFormatter datoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
        // SimpleDateFormat datoFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        String newDate = datoFormat.format(dato);
        // System.out.println(datoFormat.format(dato).getClass());
        // System.out.println(dato);
        // System.out.println(LocalDate.parse(newDate).plusDays(1));

        for (Integer i = 1; i < 8; i++) {
            String nextDate = LocalDate.parse(newDate).plusDays(i).toString();
            
            getdayData(nextDate, data, userVariables);
        }
    }


    public static String getApiData(String cityLocation) {

        try {
           //  System.out.println(cityLocation);
            
            String formattedUri = String.format("https://api.met.no/weatherapi/locationforecast/2.0/compact?%s", cityLocation);

            // System.out.println(formattedUri);


            
            String contactInfo = CustomVariables.contactInfo();
            String formattedHeader = String.format("weather app practice, email: %s", contactInfo);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(formattedUri))
            .header("User-Agent", formattedHeader)
            .timeout(Duration.of(10, ChronoUnit.SECONDS))
            .GET()
            .build();
            
            

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());


            return response.body();

        } catch (Exception e) {
            System.out.println(e); 

        }
        return null;

    }

    public static String cityCheck(String city) {
        HashMap<String, String> cityList = new HashMap<String, String>();
        cityList.put("oslo", "lat=59.9333&lon=10.7166");
        cityList.put("trondheim", "lat=60.3894&lon=5.33");
        cityList.put("bergen", "lat=63.4308&lon=10.4034");
        cityList.put("tromsø", "lat=69.6827&lon=18.9427");
        cityList.put("vardø", "lat=70.3705&lon=31.0241");
        // System.out.println(cityList.get(city));
    
        return cityList.get(city.toLowerCase());

    }

    public static HashMap getUserInput() {

        HashMap<String, String> userInput = new HashMap<String, String>();

        Scanner scannerObject = new Scanner(System.in); 



        String cityLocation = null;
        String city = null;

        while (cityLocation == null){

            System.out.println("Hvilken by nsker du aa vite om? ");
            city = scannerObject.nextLine();
            cityLocation = cityCheck(city);
        }


        System.out.println("Hva vil du vite? (regn, temperatur, vind) ");
        String userVariables = scannerObject.nextLine();


        userInput.put("city", city);
        userInput.put("cityLocation", cityLocation);
        userInput.put("userVariables", userVariables);

        scannerObject.close();

        return userInput;
    };

    public static void main(String[] args) throws Exception {
        
        Map<String, String> env = System.getenv();
        System.out.println(" env " + env.get("CONTACT_INFO"));

        HashMap userInput = getUserInput();

        // System.out.println(userInput.get("cityLocation").getClass());

        String cityLocation = userInput.get("cityLocation").toString();
        String apiData = getApiData(cityLocation);

        getWeek(apiData, userInput.get("userVariables").toString());
    }
}
