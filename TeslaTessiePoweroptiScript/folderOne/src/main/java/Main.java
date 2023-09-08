package folderOne.target.classes.src.main.java;
import java.util.Timer;
import java.util.TimerTask;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Main {
    private static final int MIN_POWER_THRESHOLD = 1150; // Minimale Leistung in Watt für das Laden
    private static final String Authorization = "Authorization";  // Fügen Sie Ihr Authentifizierungstoken hier ein
    private static final String IHR_AUTHENTIFIZIERUNGSTOKEN = "Ihr_Authentifizierungstoken";  // Fügen Sie Ihr Authentifizierungstoken hier ein
    private static final String VRN = "5YJXCAE43LF123456";  // Example, Fügen Sie Ihr Authentifizierungstoken hier ein
    private static final String BEARER = "1234567890ABCDEF1234567890ABCDEF" // Fügen Sie Ihr Authentifizierungstoken hier ein
    private static final String HOMEADDRESS = "Musterstraße 1, 12345 Musterstadt"; // Fügen Sie Ihre Adresse hier ein. Parke Tesla davor zu Hause um in App zu schauen, wie Adresse angezeigt wird.


    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkCharging();
            }
        }, 0, 60000);
    }

    public static void checkCharging() {
        if (isTeslaAtHome()) {
            int currentPower = getCurrentPower();
            if (currentPower >= MIN_POWER_THRESHOLD) {
                startCharging();
            } else {
                stopCharging();
            }
        }
    }

    public static boolean isTeslaAtHome() {
        // Tessie API-Aufruf, um den Standort des Tesla zu überprüfen
        // URL: https://api.tessie.com/{vin}/location
          OkHttpClient client = new OkHttpClient();
    boolean isAtHome = false;

    Request request = new Request.Builder()
        .url("https://api.tessie.com/"+VRN+"/location")
        .get()
        .addHeader("accept", "application/json")
        .addHeader("authorization", "Bearer "+BEARER)
        .build();

    try (Response response = client.newCall(request).execute()) {
        String responseBody = response.body().string();
        if (responseBody.contains(HOMEADDRESS)) {
            isAtHome = true;
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return isAtHome;
}


    
    public static int getCurrentPower() {
        String apiUrl = "https://backend.powerfox.energy/api/2.0/my/main/current";
        int currentPower = 0;
    
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty(Authorization, Ihr_Authentifizierungstoken);
    
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
    
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
    
                // JSON-Antwort parsen
                JSONObject jsonResponse = new JSONObject(response.toString());
                currentPower = jsonResponse.getInt("Watt");
            } else {
                System.out.println("GET-Anfrage fehlgeschlagen. Antwortcode: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return currentPower;
    }
    

    public static void startCharging() {
        // Tessie API-Aufruf, um das Laden zu starten
        // URL: https://api.tessie.com/{vin}/command/start_charging
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url("https://api.tessie.com/"+VRN+"/command/start_charging")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("authorization", "Bearer "+BEARER)
            .build();
    
        try (Response response = client.newCall(request).execute()) {
            // Do something with the response if needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopCharging() {
        // Tessie API-Aufruf, um das Laden zu stoppen
        // URL: https://api.tessie.com/{vin}/command/stop_charging
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url("https://api.tessie.com/"+VRN+"/command/stop_charging?retry_duration=40&wait_for_completion=true")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("authorization", "Bearer "+BEARER)
            .build();
    
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Stop charging.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}