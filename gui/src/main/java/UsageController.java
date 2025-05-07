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
            URL url = new URL("http://localhost:8080/energy/current");
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

                String jsonResponse = response.toString();
                org.json.JSONObject object = new org.json.JSONObject(jsonResponse);

                // JSON-Werte extrahieren
                double community_pool = object.getDouble("community_pool");
                double grid_portion = object.getDouble("grid_portion");

                // Labels refreshen
                community_pool_label.setText(String.valueOf(community_pool));
                grid_portion_label.setText(String.valueOf(grid_portion));

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

                String jsonResponse = response.toString();

                org.json.JSONObject jsonObject = new org.json.JSONObject(jsonResponse);
                org.json.JSONArray keys = jsonObject.names();
                for (int i = 0; i < keys.length(); i++) {
                    String key = keys.getString(i);
                    org.json.JSONObject inner = jsonObject.getJSONObject(key);
                    community_produced += inner.getDouble("community_produced");
                    community_used += inner.getDouble("community_used");
                    grid_used += inner.getDouble("grid_used");
                }
            }
            connection.disconnect();

            // Labels aktualisieren
            community_produced_label.setText(String.format(Locale.US, "%.2f", community_produced));
            community_used_label.setText(String.format(Locale.US, "%.2f", community_used));
            grid_used_label.setText(String.format(Locale.US, "%.2f", grid_used));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}