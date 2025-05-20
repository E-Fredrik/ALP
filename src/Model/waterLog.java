package Model;

public class waterLog extends healthLog{
    private int waterIntake; // in milliliters

    @Override
    public void inputLog() {
        // Logic to input water intake log
        // For example, using Scanner to get user input
        // waterIntake = scanner.nextInt();
    }

    @Override
    public void displayLog() {
        // Logic to display water intake log
        // For example, printing the water intake value
        System.out.println("Water Intake: " + waterIntake + " ml");
    }

    @Override
    public String getType() {
        return "water";
    }
}
