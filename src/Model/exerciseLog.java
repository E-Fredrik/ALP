package Model;

public class exerciseLog extends healthLog {
    private String exerciseType;
    private int duration; // in minutes
    private int caloriesBurned;

    public void estimateCaloriesBurned() {
        // Simple estimation based on exercise type and duration
        if (exerciseType.equalsIgnoreCase("running")) {
            caloriesBurned = duration * 10; // Example: 10 calories per minute
        } else if (exerciseType.equalsIgnoreCase("cycling")) {
            caloriesBurned = duration * 8; // Example: 8 calories per minute
        } else {
            caloriesBurned = duration * 5; // Default for other exercises
        }
    }

    @Override
    public void inputLog() {
        // Logic to input exercise log
        // For example, using Scanner to get user input
        // exerciseType = scanner.nextLine();
        // duration = scanner.nextInt();
        estimateCaloriesBurned();
    }

    @Override
    public void displayLog() {
        // Logic to display exercise log
        // For example, printing the exercise details
        System.out.println("Exercise Type: " + exerciseType);
        System.out.println("Duration: " + duration + " minutes");
        System.out.println("Calories Burned: " + caloriesBurned + " kcal");
    }

    @Override
    public String getType() {
        return "exercise";
    }
}
