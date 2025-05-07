import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Locale;

public class UsageController {
    @FXML
    private Label community_pool_label;

    @FXML
    private Label grid_portion_label;

    @FXML
    private Label community_produced_label;

    @FXML
    private Label community_used_label;

    @FXML
    private Label grid_used_label;

    @FXML
    private DatePicker start;

    @FXML
    private DatePicker end;

    @FXML
    public void fetchRefresh(javafx.event.ActionEvent actionEvent) {
        try {
            // API-URL
            URL url = new URL("http://localhost:8080/energy/current");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Anfrage erfolgreich?
            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Array in JSON-Format
                String jsonResponse = response.toString();
                org.json.JSONArray jsonArray = new org.json.JSONArray(jsonResponse);
                if (jsonArray.length() > 0) {
                    org.json.JSONObject object = jsonArray.getJSONObject(0);

                    // JSON-Werte extrahieren
                    double community_pool = object.getDouble("community_pool");
                    double grid_portion = object.getDouble("grid_portion");

                    // Labels refreshen
                    community_pool_label.setText(String.valueOf(community_pool));
                    grid_portion_label.setText(String.valueOf(grid_portion));
                }
            } else {
                System.out.println("Error: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void fetchShow(javafx.event.ActionEvent actionEvent) {
        try {
            // Start- und Enddatum auslesen
            LocalDate startDate = start.getValue();
            LocalDate endDate = end.getValue();

            if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
                System.out.println("Ungültiges Datum.");
                return;
            }

            double community_produced = 0;
            double community_used = 0;
            double grid_used = 0;

            // Datume in das ISO 8601 Format umwandeln
            String startFormatted = startDate.atStartOfDay().toString(); // z.B. "2025-05-07T00:00:00"
            String endFormatted = endDate.atStartOfDay().toString();

            // Iteration über die Stunden zwischen den beiden Zeitpunkten
            /**for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                for (int hour = 0; hour < 24; hour++) {

                    **/
                    String urlString = String.format("http://localhost:8080/energy/historical?start=%s&end=%s", startFormatted, endFormatted);
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");

                    if (connection.getResponseCode() == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // JSON-Array verarbeiten
                        String jsonResponse = response.toString();
                        org.json.JSONArray jsonArray = new org.json.JSONArray(jsonResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                            community_produced += jsonObject.getDouble("community_produced");
                            community_used += jsonObject.getDouble("community_used");
                            grid_used += jsonObject.getDouble("grid_used");
                        }
                    }
                    connection.disconnect();
               /** }
            }**/

            // Labels aktualisieren
            community_produced_label.setText(String.format(Locale.US, "%.2f", community_produced));
            community_used_label.setText(String.format(Locale.US, "%.2f", community_used));
            grid_used_label.setText(String.format(Locale.US, "%.2f", grid_used));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}