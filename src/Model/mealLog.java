package Model;

import Logic.apiConnect;
import java.util.Scanner;

public class mealLog extends healthLog {
    private String foodName;
    private int calories;
    private Scanner scanner;
    private apiConnect nutritionApi;

    public mealLog() {
        scanner = new Scanner(System.in);
        nutritionApi = new apiConnect();
    }
    
    // Constructor with parameters
    public mealLog(String foodName, int calories) {
        this.foodName = foodName;
        this.calories = calories;
    }

    @Override
    public void inputLog() {
        System.out.println("\n--- Log Meal ---");
        System.out.print("Enter food name or description (e.g., '1 apple' or 'grilled chicken breast'): ");
        foodName = scanner.nextLine();
        
        // Use the Nutritionix API to get calorie information
        System.out.println("Searching for nutritional information...");
        String apiResponse = nutritionApi.searchFood(foodName);
        
        if (apiResponse != null && !apiResponse.equals("No food data found") && !apiResponse.startsWith("API Error") && !apiResponse.startsWith("Error")) {
            // Parse the response - it should be in format "food_name: calories calories"
            try {
                // Try to extract calorie information from the API response
                String[] lines = apiResponse.split("\n");
                if (lines.length > 0) {
                    String firstFood = lines[0]; // Get the first food item
                    
                    // Extract calories
                    int colonIndex = firstFood.indexOf(":");
                    if (colonIndex != -1) {
                        String calorieText = firstFood.substring(colonIndex + 1).trim();
                        int spaceIndex = calorieText.indexOf(" ");
                        if (spaceIndex != -1) {
                            String calorieValue = calorieText.substring(0, spaceIndex).trim();
                            try {
                                calories = (int) Double.parseDouble(calorieValue);
                                System.out.println("Found: " + firstFood);
                            } catch (NumberFormatException e) {
                                promptManualCalories("Could not parse calorie value: " + calorieValue);
                            }
                        } else {
                            promptManualCalories("Unexpected API response format");
                        }
                    } else {
                        promptManualCalories("Unexpected API response format");
                    }
                } else {
                    promptManualCalories("No food items found in API response");
                }
            } catch (Exception e) {
                promptManualCalories("Error processing API response: " + e.getMessage());
            }
        } else {
            promptManualCalories("Could not find nutritional information for that food");
        }
        
        System.out.println("Meal logged: " + foodName + " - " + calories + " calories");
    }
    
    private void promptManualCalories(String reason) {
        System.out.println(reason);
        System.out.print("Please enter the calories manually: ");
        try {
            calories = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Setting calories to 0.");
            calories = 0;
        }
    }

    @Override
    public void displayLog() {
        System.out.println("\n--- Meal Log Details ---");
        System.out.println("Food Name: " + foodName);
        System.out.println("Calories: " + calories + " kcal");
    }
    
    // Getter methods
    public String getFoodName() {
        return foodName;
    }
    
    public int getCalories() {
        return calories;
    }

    @Override
    public String getType() {
        return "meal";
    }
}
