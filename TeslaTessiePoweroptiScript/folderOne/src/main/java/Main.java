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
    private static final int MIN_POWER_THRESHOLD = 1149; // Minimum power in Watts for charging (1150 Watts = 5 Ampere @ 230V)
    private static final String AUTHORIZATION = "Authorization";  // Insert your Authorization token of Poweropti here
    private static final String YOUR_AUTHENTICATION_TOKEN = "Your_Authentication_Token";  // Insert your authentication token of Poweropti here
    private static final String VRN = "5YPXLAK43LF123456";  // Example-value, insert your Vehicle Registration Number (see in your Tesla App) here
    private static final String BEARER = "1234567890ABCDEF1234567890ABCDEF"; // Insert your Bearer token (see in Tessie) here
    private static final String HOME_ADDRESS = "Sample Street 1, 12345 Sample City"; // Insert your address (where your solar panel is) here. Park Tesla in front of home to check how the address is displayed in the Tessie app.

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkCharging();
            }
        }, 0, 60000);    // 60000 = 60 seconds.
    }

    public static void checkCharging() {
    // check if Tesla is home, if power is enough to charge and starts / stops charging accordingly.
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
        // Tessie API call to check Tesla's location
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
            if (responseBody.contains(HOME_ADDRESS)) {
                isAtHome = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isAtHome;
    }

    public static int getCurrentPower() {
        // Powerfox / Poweropti API call to get power
        // https://www.powerfox.energy/wp-content/uploads/2020/05/powerfox-Kunden-API.pdf
        String apiUrl = "https://backend.powerfox.energy/api/2.0/my/main/current";
        int currentPower = 0;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty(AUTHORIZATION, YOUR_AUTHENTICATION_TOKEN);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                currentPower = jsonResponse.getInt("Watt");
            } else {
                System.out.println("GET request failed. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentPower;
    }

    public static void startCharging() {
        // Tessie API call to start charging
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
        // Tessie API call to stop charging
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
