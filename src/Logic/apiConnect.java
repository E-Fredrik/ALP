package Logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class apiConnect {
    private static final String APP_ID = "9a5b6d0d";
    private static final String API_KEY = "70089ac05c05135cce9dfffc453429ce";

    public String searchFood(String query) {
        try {
            URL url = new URL("https://trackapi.nutritionix.com/v2/natural/nutrients");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("x-app-id", APP_ID);
            conn.setRequestProperty("x-app-key", API_KEY);
            conn.setDoOutput(true);

            String jsonInputString = "{\"query\": \"" + query + "\"}";
            conn.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                String responseStr = response.toString();
                System.out.println("API Response Received");

                if (responseStr.contains("\"foods\"")) {
                    String foodsSection = responseStr.substring(responseStr.indexOf("\"foods\":[") + 9);
                    foodsSection = foodsSection.substring(0, foodsSection.lastIndexOf("]") + 1);

                    String[] foodItems = foodsSection.split("\\},\\{");

                    StringBuilder result = new StringBuilder();

                    for (String foodItem : foodItems) {
                        if (foodItem.startsWith("[{")) foodItem = foodItem.substring(2);
                        if (foodItem.endsWith("}]")) foodItem = foodItem.substring(0, foodItem.length() - 2);
                        if (foodItem.startsWith("{")) foodItem = foodItem.substring(1);
                        if (foodItem.endsWith("}")) foodItem = foodItem.substring(0, foodItem.length() - 1);

                        String name = extractJsonValue(foodItem, "food_name");
                        
                        // Improved calories extraction
                        double calories = 0;
                        String caloriesStr = extractJsonValue(foodItem, "nf_calories");
                        
                        if (caloriesStr != null && !caloriesStr.isEmpty()) {
                            try {
                                calories = Double.parseDouble(caloriesStr);
                            } catch (NumberFormatException e) {
                                System.out.println("Error parsing calories value: " + caloriesStr);
                                System.out.println("Food item JSON: " + foodItem);
                            }
                        } else {
                            // Try alternate field names that might contain calories
                            String[] alternateFields = {"full_nutrients", "calories", "energy"};
                            for (String field : alternateFields) {
                                String altValue = extractJsonValue(foodItem, field);
                                if (altValue != null && !altValue.isEmpty()) {
                                    try {
                                        calories = Double.parseDouble(altValue);
                                        System.out.println("Found calories in alternate field: " + field);
                                        break;
                                    } catch (NumberFormatException e) {
                                        // Continue to next field
                                    }
                                }
                            }
                        }

                        // Append food name and calories to the result
                        result.append(name).append(": ").append(calories).append(" calories\n");
                    }
                    
                    return result.toString().trim();
                } else {
                    System.out.println("Error: No food data found in the response.");
                    return "No food data found";
                }
            } else {
                System.out.println("Error: " + responseCode);
                return "API Error: " + responseCode;
            }
        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    // Replace the extractJsonValue method with this improved version:
    private String extractJsonValue(String json, String key) {
        String keySearch = "\"" + key + "\":";
        int keyIndex = json.indexOf(keySearch);

        if (keyIndex == -1) {
            return "";
        }
        
        int valueStart = keyIndex + keySearch.length();

        while (valueStart < json.length() && (json.charAt(valueStart) == ' ' || json.charAt(valueStart) == '\t')) {
            valueStart++;
        }

        if (valueStart < json.length()) {
            char firstChar = json.charAt(valueStart);

            if (firstChar == '"') {
                // Handle string value
                int valueEnd = json.indexOf('\"', valueStart + 1);
                if (valueEnd != -1) {
                    return json.substring(valueStart + 1, valueEnd);
                }
            } else if ((firstChar >= '0' && firstChar <= '9') || firstChar == '-' || firstChar == '.') {
                // Handle numeric value
                int valueEnd = valueStart;
                while (valueEnd < json.length() && 
                       (Character.isDigit(json.charAt(valueEnd)) || 
                        json.charAt(valueEnd) == '.' || 
                        json.charAt(valueEnd) == '-' ||
                        json.charAt(valueEnd) == 'e' ||  // Handle scientific notation
                        json.charAt(valueEnd) == 'E')) {
                    valueEnd++;
                }
                return json.substring(valueStart, valueEnd);
            } else if (firstChar == '[' || firstChar == '{') {
                // Handle array or object
                return ""; // Skip complex objects for now
            }
        }

        return "";
    }
}
