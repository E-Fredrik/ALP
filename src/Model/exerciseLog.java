package Model;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import Logic.sqlConnect;

public class exerciseLog extends healthLog {
    private String exerciseType;
    private int duration; 
    private int caloriesBurned;
    private double intensity; 
    private int userWeight;
    private String username; // Store username for database lookups
    private Scanner scanner;
    
    private static final Map<String, Double> MET_VALUES = new HashMap<>();
    
    static {
        MET_VALUES.put("walking", 3.5);
        MET_VALUES.put("running", 9.8);
        MET_VALUES.put("cycling", 7.5);
        MET_VALUES.put("swimming", 8.3);
        MET_VALUES.put("yoga", 2.5);
        MET_VALUES.put("weightlifting", 5.0);
        MET_VALUES.put("dancing", 4.5);
        MET_VALUES.put("basketball", 6.5);
        MET_VALUES.put("soccer", 7.0);
        MET_VALUES.put("hiking", 5.3);
    }

    public exerciseLog(User currentUser) {
        super(); 
        scanner = new Scanner(System.in);
        exerciseType = "";
        duration = 0;
        caloriesBurned = 0;
        intensity = 1.0;
        
        // Store user data
        if (currentUser != null) {
            userWeight = currentUser.getWeight();
            username = currentUser.getUsername();
        } else {
            userWeight = 70; // Default value
            username = null;
        }
    }
    
    public exerciseLog(Date date, User currentUser) {
        super(date);
        scanner = new Scanner(System.in);
        exerciseType = "";
        duration = 0;
        caloriesBurned = 0;
        intensity = 1.0;
        
        if (currentUser != null) {
            userWeight = currentUser.getWeight();
            username = currentUser.getUsername();
        } else {
            userWeight = 70;
            username = null;
        }
    }
    
    private void loadUserDataFromDatabase() {
        if (username != null && !username.isEmpty()) {
            sqlConnect dbConnection = new sqlConnect();
            User userData = dbConnection.getUserByUsername(username);
            
            if (userData != null) {
                userWeight = userData.getWeight();
            } else {
                System.out.println("Could not load user data. Using default values.");
                userWeight = 70; 
            }
        }
    }
    
    public void estimateCaloriesBurned() {
        double met;
        
        if (MET_VALUES.containsKey(exerciseType.toLowerCase())) {
            met = MET_VALUES.get(exerciseType.toLowerCase());
        } else {
        
            met = 4.0;
        }
        
    
        met *= intensity;
        
        
        caloriesBurned = (int) Math.round(met * userWeight * (duration / 60.0));
    }

    @Override
public void inputLog() {
    // Try to load user data if needed
    if (userWeight <= 0 && username != null) {
        loadUserDataFromDatabase();
    }
    
    System.out.println("\n--- Log Exercise ---");
    
    System.out.println("Available exercise types:");
    for (String type : MET_VALUES.keySet()) {
        System.out.println("- " + capitalizeFirstLetter(type));
    }
    
    System.out.print("\nEnter exercise type: ");
    exerciseType = scanner.nextLine().toLowerCase();
    
    boolean validDuration = false;
    do {
        try {
            System.out.print("Enter duration (minutes): ");
            duration = Integer.parseInt(scanner.nextLine());
            if (duration <= 0) {
                System.out.println("Duration must be a positive number.");
            } else {
                validDuration = true;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    } while (!validDuration);
    
    // Only prompt for weight if we don't already have it
    if (userWeight <= 0) {
        boolean validWeight = false;
        do {
            try {
                System.out.print("Enter your weight (kg): ");
                userWeight = Integer.parseInt(scanner.nextLine());
                if (userWeight <= 0) {
                    System.out.println("Weight must be a positive number.");
                } else {
                    validWeight = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        } while (!validWeight);
    } else {
        System.out.println("Weight: " + userWeight + " kg");
    }
    
    System.out.println("\nSelect intensity level:");
    System.out.println("1. Light (0.5)");
    System.out.println("2. Moderate (1.0)");
    System.out.println("3. High (1.5)");
    System.out.print("Enter choice (1-3): ");
    
    int intensityChoice = 2; // Default to moderate
    try {
        intensityChoice = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
        System.out.println("Invalid choice. Setting to moderate intensity.");
    }
    
    switch (intensityChoice) {
        case 1:
            intensity = 0.5;
            break;
        case 2:
            intensity = 1.0;
            break;
        case 3:
            intensity = 1.5;
            break;
        default:
            System.out.println("Invalid choice. Setting to moderate intensity.");
            intensity = 1.0;
    }
    
    estimateCaloriesBurned();
    
    System.out.println("\nExercise logged: " + capitalizeFirstLetter(exerciseType) + 
                       " for " + duration + " minutes at " + 
                       getIntensityName(intensity) + " intensity");
    System.out.println("Estimated calories burned: " + caloriesBurned);
}

    @Override
    public void displayLog() {
        System.out.println("\n--- Exercise Log Details ---");
        System.out.println("Date: " + getFormattedDate());
        System.out.println("Exercise Type: " + capitalizeFirstLetter(exerciseType));
        System.out.println("Duration: " + duration + " minutes");
        System.out.println("Intensity: " + getIntensityName(intensity));
        System.out.println("Calories Burned: " + caloriesBurned + " kcal");
    }
    
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    private String getIntensityName(double intensity) {
        if (intensity <= 0.5) return "Light";
        if (intensity >= 1.5) return "High";
        return "Moderate";
    }
    
    // Getter methods
    public String getExerciseType() {
        return exerciseType;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public int getCaloriesBurned() {
        return caloriesBurned;
    }
    
    public double getIntensity() {
        return intensity;
    }
    
    @Override
    public String getType() {
        return "exercise";
    }
}
