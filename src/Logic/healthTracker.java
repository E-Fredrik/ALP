package Logic;

import java.util.ArrayList;
import java.util.Scanner;
import Model.User;
import Model.healthLog;
import Model.mealLog;
import Model.waterLog;
import Model.exerciseLog;

public class healthTracker {
    private Scanner scanner;
    private User currentUser;
    private ArrayList<healthLog> logs;
    
    public healthTracker() {
        scanner = new Scanner(System.in);
        currentUser = null;
        logs = new ArrayList<>();
    }
    
    public void start() {
        System.out.println("======================================");
        System.out.println("|       HEALTH TRACKER SYSTEM       |");
        System.out.println("======================================");
        
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    private void showLoginMenu() {
        System.out.println("\n--- Login/Register Menu ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        
        int choice = getUserChoice(1, 3);
        
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                System.out.println("Thank you for using Health Tracker. Goodbye!");
                System.exit(0);
                break;
        }
    }
    
    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        User user = new User("", "", "", 0, 0, 0, 0, 0, 0.0); // Updated constructor call
        currentUser = user.login(username, password);
        
        if (currentUser != null) {
            System.out.println("Login successful! Welcome, " + currentUser.getUsername() + "!");
        } else {
            System.out.println("Login failed! Incorrect username or password.");
        }
    }
    
    private void register() {
        System.out.println("\n--- Register ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        String password;
        boolean validPassword = false;
        
        do {
            System.out.print("Password (min 8 characters with at least 1 uppercase): ");
            password = scanner.nextLine();
            validPassword = validatePassword(password);
        } while (!validPassword);
        
        String dateOfBirth;
        boolean validDate = false;
        
        do {
            System.out.print("Date of Birth (YYYY-MM-DD): ");
            dateOfBirth = scanner.nextLine();
            validDate = validateDateFormat(dateOfBirth);
        } while (!validDate);
        
        int height = 0;
        boolean validHeight = false;
        do {
            try {
                System.out.print("Height (cm): ");
                String heightInput = scanner.nextLine();
                height = Integer.parseInt(heightInput);
                validHeight = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for height.");
            }
        } while (!validHeight);
        
        int weight = 0;
        boolean validWeight = false;
        do {
            try {
                System.out.print("Weight (kg): ");
                String weightInput = scanner.nextLine();
                weight = Integer.parseInt(weightInput);
                validWeight = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for weight.");
            }
        } while (!validWeight);
        
        int calorieIntake = 0;
        boolean validCalorieIntake = false;
        do {
            try {
                System.out.print("Daily Calorie Intake Goal: ");
                String calorieInput = scanner.nextLine();
                calorieIntake = Integer.parseInt(calorieInput);
                validCalorieIntake = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for calorie intake.");
            }
        } while (!validCalorieIntake);
        
        int calorieBurn = 0;
        boolean validCalorieBurn = false;
        do {
            try {
                System.out.print("Daily Calorie Burn Goal: ");
                String burnInput = scanner.nextLine();
                calorieBurn = Integer.parseInt(burnInput);
                validCalorieBurn = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for calorie burn.");
            }
        } while (!validCalorieBurn);
        
        int waterIntake = 0;
        boolean validWaterIntake = false;
        do {
            try {
                System.out.print("Daily Water Intake Goal (ml): ");
                String waterInput = scanner.nextLine();
                waterIntake = Integer.parseInt(waterInput);
                validWaterIntake = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for water intake.");
            }
        } while (!validWaterIntake);
        
        double sleepHours = 0.0;
        boolean validSleepHours = false;
        do {
            try {
                System.out.print("Daily Sleep Hours Goal: ");
                String sleepInput = scanner.nextLine();
                sleepHours = Double.parseDouble(sleepInput);
                validSleepHours = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for sleep hours.");
            }
        } while (!validSleepHours);
        
        currentUser = User.register(username, password, dateOfBirth, height, weight, 
                                   calorieIntake, calorieBurn, waterIntake, sleepHours);
        
        if (currentUser != null) {
            System.out.println("Registration successful! Welcome, " + currentUser.getUsername() + "!");
        } else {
            System.out.println("Registration failed! Username might already be taken.");
        }
    }
    
    private void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. View Profile");
        System.out.println("2. Log Management");
        System.out.println("3. Update Health Goals");  // New option
        System.out.println("4. View Health Stats");
        System.out.println("5. Log Out");  // Moved to option 5
        System.out.print("Choose an option: ");
        
        int choice = getUserChoice(1, 5);  // Updated max value
        
        switch (choice) {
            case 1:
                viewProfile();
                break;
            case 2:
                showLogMenu();
                break;
            case 3:
                updateHealthGoals();  // New method
                break;
            case 4:
                viewHealthStats();
                break;
            case 5:
                currentUser = null;
                System.out.println("Logged out successfully!");
                break;
        }
    }
    
    private void showLogMenu() {
        boolean inLogMenu = true;
        
        while (inLogMenu) {
            System.out.println("\n--- Log Management ---");
            System.out.println("1. Add Water Intake Log");
            System.out.println("2. Add Meal Log");
            System.out.println("3. Add Exercise Log");
            System.out.println("4. View All Logs");
            System.out.println("5. View Water Intake Logs");
            System.out.println("6. View Meal Logs"); 
            System.out.println("7. View Exercise Logs");
            System.out.println("8. Return to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = getUserChoice(1, 8);
            
            switch (choice) {
                case 1:
                    addWaterIntakeLog();
                    break;
                case 2:
                    addMealLog();
                    break;
                case 3:
                    addExerciseLog();
                    break;
                case 4:
                    viewAllLogs();
                    break;
                case 5:
                    viewLogsByType("water");
                    break;
                case 6:
                    viewLogsByType("meal");
                    break;
                case 7:
                    viewLogsByType("exercise");
                    break;
                case 8:
                    inLogMenu = false;
                    break;
            }
        }
    }
    
    private void addWaterIntakeLog() {
        System.out.println("\n--- Add Water Intake Log ---");
        waterLog log = new waterLog();
        log.inputLog();
        logs.add(log); // Add to unified logs collection
        log.displayLog();
        System.out.println("Water intake logged successfully!");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void addMealLog() {
        System.out.println("\n--- Add Meal Log ---");
        mealLog log = new mealLog();
        log.inputLog();
        logs.add(log); // Add to unified logs collection
        log.displayLog();
        System.out.println("Meal logged successfully!");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void addExerciseLog() {
        System.out.println("\n--- Add Exercise Log ---");
        exerciseLog log = new exerciseLog();
        log.inputLog();
        logs.add(log); // Add to unified logs collection
        log.displayLog();
        System.out.println("Exercise logged successfully!");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void viewAllLogs() {
        System.out.println("\n--- All Health Logs ---");
        
        if (logs.isEmpty()) {
            System.out.println("No logs found.");
        } else {
            for (healthLog log : logs) {
                log.displayLog();
                System.out.println("-------------------------");
            }
        }
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    private void viewLogsByType(String type) {
        System.out.println("\n--- " + capitalizeFirstLetter(type) + " Logs ---");
        
        boolean logsFound = false;
        for (healthLog log : logs) {
            if (log.getType().equalsIgnoreCase(type)) {
                log.displayLog();
                System.out.println("-------------------------");
                logsFound = true;
            }
        }
        
        if (!logsFound) {
            System.out.println("No " + type + " logs found.");
        }
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    private void viewProfile() {
        System.out.println("\n--- User Profile ---");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Age: " + currentUser.getAge());
        System.out.println("Height: " + currentUser.getHeight() + " cm");
        System.out.println("Weight: " + currentUser.getWeight() + " kg");
        System.out.println("Daily Calorie Intake Goal: " + currentUser.getCalorieIntake() + " calories");
        System.out.println("Daily Calorie Burn Goal: " + currentUser.getCalorieBurn() + " calories");
        System.out.println("Daily Water Intake Goal: " + currentUser.getWaterIntake() + " ml");
        System.out.println("Daily Sleep Hours Goal: " + currentUser.getSleepHours() + " hours");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void viewHealthStats() {
        System.out.println("\n--- Health Statistics ---");
        System.out.println("Feature coming soon!");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void updateHealthGoals() {
        System.out.println("\n--- Update Health Goals ---");
        
        try {
            System.out.print("New height in cm (current: " + currentUser.getHeight() + "): ");
            String heightInput = scanner.nextLine();
            if (!heightInput.isEmpty()) {
                currentUser.setHeight(Integer.parseInt(heightInput));
            }
            
            System.out.print("New weight in kg (current: " + currentUser.getWeight() + "): ");
            String weightInput = scanner.nextLine();
            if (!weightInput.isEmpty()) {
                currentUser.setWeight(Integer.parseInt(weightInput));
            }
            
            System.out.print("New daily calorie intake goal (current: " + currentUser.getCalorieIntake() + "): ");
            String calorieIntakeInput = scanner.nextLine();
            if (!calorieIntakeInput.isEmpty()) {
                currentUser.setCalorieIntake(Integer.parseInt(calorieIntakeInput));
            }
            
            System.out.print("New daily calorie burn goal (current: " + currentUser.getCalorieBurn() + "): ");
            String calorieBurnInput = scanner.nextLine();
            if (!calorieBurnInput.isEmpty()) {
                currentUser.setCalorieBurn(Integer.parseInt(calorieBurnInput));
            }
            
            System.out.print("New daily water intake goal in ml (current: " + currentUser.getWaterIntake() + "): ");
            String waterIntakeInput = scanner.nextLine();
            if (!waterIntakeInput.isEmpty()) {
                currentUser.setWaterIntake(Integer.parseInt(waterIntakeInput));
            }
            
            System.out.print("New daily sleep hours goal (current: " + currentUser.getSleepHours() + "): ");
            String sleepHoursInput = scanner.nextLine();
            if (!sleepHoursInput.isEmpty()) {
                currentUser.setSleepHours(Double.parseDouble(sleepHoursInput));
            }
            
            // Save the updated goals to the database
            boolean success = currentUser.updateUserGoals();
            
            if (success) {
                System.out.println("Health goals updated successfully!");
            } else {
                System.out.println("Failed to update health goals.");
            }
            
        } catch (Exception e) {
            System.out.println("Invalid input. Please check your format.");
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private boolean validatePassword(String password) {
        // Check length is at least 8 characters
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }
        
        // Check for at least one uppercase letter
        boolean hasUppercase = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
                break;
            }
        }
        
        if (!hasUppercase) {
            System.out.println("Password must contain at least one uppercase letter.");
            return false;
        }
        
        return true;
    }
    
    private boolean validateDateFormat(String date) {
        // Check if the format matches YYYY-MM-DD
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD format (e.g., 2006-01-15).");
            return false;
        }
        
        try {
            // Try to parse as LocalDate to confirm it's a valid date
            java.time.LocalDate.parse(date);
            return true;
        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("Invalid date. Please enter a valid date in YYYY-MM-DD format.");
            return false;
        }
    }
    
    private int getUserChoice(int min, int max) {
        int choice = -1;
        while (choice < min || choice > max) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < min || choice > max) {
                    System.out.print("Invalid choice. Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
        return choice;
    }
    
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
