package Logic;

import java.util.*;
import java.time.*;
import Model.User;
import Model.healthLog;
import Model.mealLog;
import Model.waterLog;
import Model.exerciseLog;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class healthTracker {
    private Scanner scanner;
    private User currentUser;
    
    public healthTracker() {
        scanner = new Scanner(System.in);
        currentUser = null;
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
        System.out.println("3. Update Health Goals"); 
        System.out.println("4. View Goal Progress");
        System.out.println("5. Log Out");  
        System.out.print("Choose an option: ");

        int choice = getUserChoice(1, 5);
        switch (choice) {
            case 1:
                viewProfile();
                break;
            case 2:
                showLogMenu();
                break;
            case 3:
                updateHealthGoals();
                break;
            case 4:
                viewGoalProgress();
                break;
            case 5:
                currentUser = null;
                System.out.println("Logged out successfully!");
                break;
        }
    }
    // Add this method to filter logs by today's date
    private boolean isToday(Date date) {
        if (date == null) return false;
        
        Calendar today = Calendar.getInstance();
        Calendar logDate = Calendar.getInstance();
        logDate.setTime(date);
        
        return today.get(Calendar.YEAR) == logDate.get(Calendar.YEAR) &&
               today.get(Calendar.DAY_OF_YEAR) == logDate.get(Calendar.DAY_OF_YEAR);
    }

    private void viewGoalProgress() {
        System.out.println("\n--- Goal Progress ---");
        LocalDate today = LocalDate.now();
        String todayStr = today.toString();
        System.out.println("Date: " + todayStr);
        
        int totalCaloriesConsumed = 0;
        int totalCaloriesBurned = 0;
        int totalWaterIntake = 0;
        
        // Get today's logs from database
        sqlConnect dbConnection = new sqlConnect();
        
        // Create date range for today
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = cal.getTime();
        
        // Fetch logs from the database for today
        ArrayList<waterLog> waterLogs = (ArrayList<waterLog>) dbConnection.getWaterLogs(
                currentUser.getUsername(), startDate, endDate);
        ArrayList<mealLog> mealLogs = (ArrayList<mealLog>) dbConnection.getMealLogs(
                currentUser.getUsername(), startDate, endDate);
        ArrayList<exerciseLog> exerciseLogs = (ArrayList<exerciseLog>) dbConnection.getExerciseLogs(
                currentUser.getUsername(), startDate, endDate);
        
        // Calculate totals
        for (mealLog meal : mealLogs) {
            totalCaloriesConsumed += meal.getCalories();
        }
        
        for (exerciseLog exercise : exerciseLogs) {
            totalCaloriesBurned += exercise.getCaloriesBurned();
        }
        
        for (waterLog water : waterLogs) {
            totalWaterIntake += water.getWaterIntake();
        }
        
        int calorieIntakeGoal = currentUser.getCalorieIntake();
        int calorieBurnGoal = currentUser.getCalorieBurn();
        int waterIntakeGoal = currentUser.getWaterIntake();
        
        
        double calorieIntakePercentage = (calorieIntakeGoal > 0) ? 
            Math.min(100, (totalCaloriesConsumed * 100.0 / calorieIntakeGoal)) : 0;
        double calorieBurnPercentage = (calorieBurnGoal > 0) ?
            Math.min(100, (totalCaloriesBurned * 100.0 / calorieBurnGoal)) : 0;
        double waterIntakePercentage = (waterIntakeGoal > 0) ?
            Math.min(100, (totalWaterIntake * 100.0 / waterIntakeGoal)) : 0;
        
        
        System.out.println("\n🏆 TODAY'S PROGRESS SUMMARY 🏆");
        
    
        System.out.println("\n📊 CALORIE INTAKE:");
        System.out.println("Goal: " + calorieIntakeGoal + " calories");
        System.out.println("Consumed: " + totalCaloriesConsumed + " calories");
        System.out.println("Progress: " + String.format("%.1f", calorieIntakePercentage) + "%");
        displayProgressBar(calorieIntakePercentage);
        

        if (totalCaloriesConsumed <= calorieIntakeGoal) {
            if (totalCaloriesConsumed == 0) {
                System.out.println("You haven't logged any calories today. Make sure to log your meals!");
            } else if (totalCaloriesConsumed < calorieIntakeGoal * 0.5) {
                System.out.println("You're eating significantly below your goal. Make sure you're getting enough nutrition!");
            } else {
                System.out.println("Great job! You're staying within your calorie intake goal.");
            }
        } else {
            int excess = totalCaloriesConsumed - calorieIntakeGoal;
            System.out.println("You've consumed " + excess + " calories over your goal today.");
        }
        

        System.out.println("\nCALORIE BURN:");
        System.out.println("Goal: " + calorieBurnGoal + " calories");
        System.out.println("Burned: " + totalCaloriesBurned + " calories");
        System.out.println("Progress: " + String.format("%.1f", calorieBurnPercentage) + "%");
        displayProgressBar(calorieBurnPercentage);
        

        if (totalCaloriesBurned >= calorieBurnGoal) {
            System.out.println("Excellent! You've exceeded your calorie burn goal by " + 
                              (totalCaloriesBurned - calorieBurnGoal) + " calories.");
        } else if (totalCaloriesBurned == 0) {
            System.out.println("You haven't logged any exercise today. Try to stay active!");
        } else {
            int remaining = calorieBurnGoal - totalCaloriesBurned;
            System.out.println("You still need to burn " + remaining + " more calories to reach your goal.");
        }
     
        System.out.println("\nWATER INTAKE:");
        System.out.println("Goal: " + waterIntakeGoal + " ml");
        System.out.println("Consumed: " + totalWaterIntake + " ml");
        System.out.println("Progress: " + String.format("%.1f", waterIntakePercentage) + "%");
        displayProgressBar(waterIntakePercentage);
        
       
        if (totalWaterIntake >= waterIntakeGoal) {
            System.out.println("Excellent! You've met your water intake goal.");
            if (totalWaterIntake >= waterIntakeGoal * 1.5) {
                System.out.println("You're super hydrated today!");
            }
        } else if (totalWaterIntake == 0) {
            System.out.println("You haven't logged any water today. Remember to stay hydrated!");
        } else {
            int remaining = waterIntakeGoal - totalWaterIntake;
            System.out.println("You still need to drink " + remaining + " ml more water to reach your goal.");
        }
        
        // Overall assessment
        System.out.println("\n📝 OVERALL ASSESSMENT:");
        int goalsAchieved = 0;
        int totalGoals = 3;
        
        if (totalCaloriesConsumed <= calorieIntakeGoal && totalCaloriesConsumed > 0) goalsAchieved++;
        if (totalCaloriesBurned >= calorieBurnGoal) goalsAchieved++;
        if (totalWaterIntake >= waterIntakeGoal) goalsAchieved++;
        
        if (goalsAchieved == totalGoals) {
            System.out.println("🎉 CONGRATULATIONS! You've achieved all your health goals for today!");
        } else if (goalsAchieved > 0) {
            System.out.println("You've achieved " + goalsAchieved + " out of " + totalGoals + " goals today. Keep going!");
        } else {
            System.out.println("Keep working towards your goals. You can do it!");
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }


    private void displayProgressBar(double percentage) {
        final int BAR_LENGTH = 20;
        int filledBars = (int) (percentage * BAR_LENGTH / 100);
        
        System.out.print("[");
        for (int i = 0; i < BAR_LENGTH; i++) {
            if (i < filledBars) {
                System.out.print("█");
            } else {
                System.out.print(" ");
            }
        }
        System.out.println("] " + String.format("%.1f", percentage) + "%");
    }
    
    private void showLogMenu() {
        boolean inLogMenu = true;
        
        while (inLogMenu) {
            System.out.println("\n--- Log Management ---");
            System.out.println("1. Add Logs");
            System.out.println("2. View Logs");
            System.out.println("3. Return to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = getUserChoice(1, 3);
            
            switch (choice) {
                case 1:
                    addLogs();
                    break;
                case 2:
                    viewLogs();
                    break;
                case 3:
                    inLogMenu = false;
                    break;
            }
        }

    }

    private void addLogs() {
        System.out.println("\n--- Add Logs ---");
        System.out.println("1. Add Water Intake Log");
        System.out.println("2. Add Meal Log");
        System.out.println("3. Add Exercise Log");
        System.out.println("4. Return to Main Menu");
        System.out.print("Choose an option: ");

        int choice = getUserChoice(1, 4);

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
                return;
        }
    }

    private void viewLogs() {
        System.out.println("\n--- View Logs ---");
        System.out.println("1. View All Logs");
        System.out.println("2. View Water Intake Logs");
        System.out.println("3. View Meal Logs");
        System.out.println("4. View Exercise Logs");
        System.out.println("5. Filter Logs by Date");  // New option
        System.out.println("6. Return to Main Menu");
        System.out.print("Choose an option: ");
        
        int choice = getUserChoice(1, 6);
        
        switch (choice) {
            case 1:
                viewAllLogs();
                break;
            case 2:
                viewLogsByType("water");
                break;
            case 3:
                viewLogsByType("meal");
                break;
            case 4:
                viewLogsByType("exercise");
                break;
            case 5:
                viewLogsByDate();  // Call the new method
                break;
            case 6:
                return;
        }
    }
    
    private void addWaterIntakeLog() {
        System.out.println("\n--- Add Water Intake Log ---");
        waterLog log = new waterLog();
        log.inputLog();
        
        // Save to database
        sqlConnect dbConnection = new sqlConnect();
        boolean saved = dbConnection.saveWaterLog(currentUser.getUsername(), log.getDate(), log.getWaterIntake());
        
        if (saved) {
            System.out.println("Water intake logged successfully!");
        } else {
            System.out.println("Failed to save water intake to database.");
        }
        
        log.displayLog();
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void addMealLog() {
        System.out.println("\n--- Add Meal Log ---");
        mealLog log = new mealLog();
        log.inputLog();
        
        // Add database save
        sqlConnect dbConnection = new sqlConnect();
        boolean saved = dbConnection.saveMealLog(currentUser.getUsername(), log.getDate(), 
                                            log.getFoodName(), log.getCalories());
    
        if (saved) {
            System.out.println("Meal logged successfully!");
        } else {
            System.out.println("Failed to save meal log to database.");
        }
    
        log.displayLog();
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void addExerciseLog() {
        System.out.println("\n--- Add Exercise Log ---");
        exerciseLog log = new exerciseLog(currentUser);
        log.inputLog();
        
        // Add database save
        sqlConnect dbConnection = new sqlConnect();
        boolean saved = dbConnection.saveExerciseLog(currentUser.getUsername(), log.getDate(), 
                                                log.getExerciseType(), log.getDuration(), 
                                                log.getIntensity(), log.getCaloriesBurned());
    
        if (saved) {
            System.out.println("Exercise logged successfully!");
        } else {
            System.out.println("Failed to save exercise log to database.");
        }
    
        log.displayLog();
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void viewAllLogs() {
        System.out.println("\n--- All Health Logs ---");
        
        sqlConnect dbConnection = new sqlConnect();
        
        // Get today's date range
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = cal.getTime();
        
        // Fetch all logs from the database
        ArrayList<waterLog> waterLogs = (ArrayList<waterLog>) dbConnection.getWaterLogs(
                currentUser.getUsername(), startDate, endDate);
        ArrayList<mealLog> mealLogs = (ArrayList<mealLog>) dbConnection.getMealLogs(
                currentUser.getUsername(), startDate, endDate);
        ArrayList<exerciseLog> exerciseLogs = (ArrayList<exerciseLog>) dbConnection.getExerciseLogs(
                currentUser.getUsername(), startDate, endDate);
        
        if (waterLogs.isEmpty() && mealLogs.isEmpty() && exerciseLogs.isEmpty()) {
            System.out.println("No logs found.");
        } else {
            if (!waterLogs.isEmpty()) {
                for (waterLog log : waterLogs) {
                    log.displayLog();
                    System.out.println("-------------------------");
                }
            }
            
            if (!mealLogs.isEmpty()) {
                for (mealLog log : mealLogs) {
                    log.displayLog();
                    System.out.println("-------------------------");
                }
            }
            
            if (!exerciseLogs.isEmpty()) {
                for (exerciseLog log : exerciseLogs) {
                    log.displayLog();
                    System.out.println("-------------------------");
                }
            }
        }
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    private void viewLogsByType(String type) {
        System.out.println("\n--- " + capitalizeFirstLetter(type) + " Logs ---");
        
        sqlConnect dbConnection = new sqlConnect();
        
        // Get today's date range
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = cal.getTime();
        
        boolean logsFound = false;
        
        if (type.equalsIgnoreCase("water")) {
            ArrayList<waterLog> logs = (ArrayList<waterLog>) dbConnection.getWaterLogs(
                    currentUser.getUsername(), startDate, endDate);
            
            if (logs.isEmpty()) {
                System.out.println("No water logs found.");
            } else {
                for (waterLog log : logs) {
                    log.displayLog();
                    System.out.println("-------------------------");
                    logsFound = true;
                }
            }
        } else if (type.equalsIgnoreCase("meal")) {
            ArrayList<mealLog> logs = (ArrayList<mealLog>) dbConnection.getMealLogs(
                    currentUser.getUsername(), startDate, endDate);
            
            if (logs.isEmpty()) {
                System.out.println("No meal logs found.");
            } else {
                for (mealLog log : logs) {
                    log.displayLog();
                    System.out.println("-------------------------");
                    logsFound = true;
                }
            }
        } else if (type.equalsIgnoreCase("exercise")) {
            ArrayList<exerciseLog> logs = (ArrayList<exerciseLog>) dbConnection.getExerciseLogs(
                    currentUser.getUsername(), startDate, endDate);
            
            if (logs.isEmpty()) {
                System.out.println("No exercise logs found.");
            } else {
                for (exerciseLog log : logs) {
                    log.displayLog();
                    System.out.println("-------------------------");
                    logsFound = true;
                }
            }
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

    private void viewLogsByDate() {
        System.out.println("\n--- Filter Logs by Date ---");
        System.out.println("1. View Today's Logs");
        System.out.println("2. View Yesterday's Logs");
        System.out.println("3. View Logs from Last 7 Days");
        System.out.println("4. View Logs from Last 30 Days");
        System.out.println("5. View Logs from Custom Date Range");
        System.out.println("6. Return to Previous Menu");
        System.out.print("Choose an option: ");
        
        int choice = getUserChoice(1, 6);
        
        Date startDate = null;
        Date endDate = new Date(); // End date is always current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        Calendar cal = Calendar.getInstance();
        
        switch (choice) {
            case 1: // Today
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startDate = cal.getTime();
                break;
            case 2: // Yesterday
                cal.add(Calendar.DAY_OF_MONTH, -1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startDate = cal.getTime();
                cal.add(Calendar.DAY_OF_MONTH, 1);
                endDate = cal.getTime();
                break;
            case 3: // Last 7 days
                cal.add(Calendar.DAY_OF_MONTH, -7);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startDate = cal.getTime();
                break;
            case 4: // Last 30 days
                cal.add(Calendar.DAY_OF_MONTH, -30);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startDate = cal.getTime();
                break;
            case 5: // Custom date range
                boolean validStartDate = false;
                while (!validStartDate) {
                    System.out.print("Enter start date (YYYY-MM-DD): ");
                    String startDateStr = scanner.nextLine();
                    try {
                        startDate = sdf.parse(startDateStr);
                        validStartDate = true;
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                    }
                }
                break;
            case 6:
                return;
        }
        
        // Retrieve logs from database using the date range
        sqlConnect dbConnection = new sqlConnect();
        
        List<waterLog> waterLogs = dbConnection.getWaterLogs(currentUser.getUsername(), startDate, endDate);
        List<mealLog> mealLogs = dbConnection.getMealLogs(currentUser.getUsername(), startDate, endDate);
        List<exerciseLog> exerciseLogs = dbConnection.getExerciseLogs(currentUser.getUsername(), startDate, endDate);
        
        // Display the logs
        System.out.println("\n--- Logs from " + sdf.format(startDate) + " to " + sdf.format(endDate) + " ---");
        
        if (waterLogs.isEmpty() && mealLogs.isEmpty() && exerciseLogs.isEmpty()) {
            System.out.println("No logs found for this date range.");
        } else {
            if (!waterLogs.isEmpty()) {
                System.out.println("\nWater Intake Logs:");
                for (waterLog log : waterLogs) {
                    log.displayLog();
                    System.out.println("-------------------------");
                }
            }
            
            if (!mealLogs.isEmpty()) {
                System.out.println("\nMeal Logs:");
                for (mealLog log : mealLogs) {
                    log.displayLog();
                    System.out.println("-------------------------");
                }
            }
            
            if (!exerciseLogs.isEmpty()) {
                System.out.println("\nExercise Logs:");
                for (exerciseLog log : exerciseLogs) {
                    log.displayLog();
                    System.out.println("-------------------------");
                }
            }
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
