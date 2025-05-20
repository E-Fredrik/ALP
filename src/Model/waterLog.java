package Model;
import java.util.Scanner;
import java.util.Date;

public class waterLog extends healthLog {
    private int waterIntake; // in milliliters
    private Scanner scanner = new Scanner(System.in);
    
    // Default constructor
    public waterLog() {
        super(); // Call parent constructor to initialize date
        waterIntake = 0;
    }
    
    // Constructor with specified water intake
    public waterLog(int waterIntake) {
        super(); // Initialize with current date
        this.waterIntake = waterIntake;
    }
    
    // Constructor with specified date and water intake
    public waterLog(Date date, int waterIntake) {
        super(date); // Use the specified date
        this.waterIntake = waterIntake;
    }

    @Override
    public void inputLog() {
        System.out.println("\n--- Log Water Intake ---");
        System.out.print("Enter water intake in milliliters: ");
        try {
            waterIntake = scanner.nextInt();
            if (waterIntake < 0) {
                System.out.println("Water intake cannot be negative. Please enter a valid amount.");
                inputLog(); 
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); 
            inputLog(); 
        }
    }

    public void setWaterIntake(int waterIntake) {
        this.waterIntake = waterIntake;
    }

    public int getWaterIntake() {
        return waterIntake;
    }
    
    @Override
    public void displayLog() {
        System.out.println("\n--- Water Intake Log ---");
        System.out.println("Date: " + getFormattedDate());
        System.out.println("Water Intake: " + waterIntake + " ml");
    }

    @Override
    public String getType() {
        return "water";
    }
}
