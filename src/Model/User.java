package Model;

import Logic.sqlConnect;
import java.time.LocalDate;
import java.time.Period;

public class User {
    private String username, password;
    private String dateOfBirth; // Changed from int age to String dateOfBirth (format: YYYY-MM-DD)
    private int height, weight, calorieIntake, calorieBurn, waterIntake;
    private double sleepHours;
    private int age; // Added age field

    public User(String username, String password, String dateOfBirth, int height, int weight, int calorieIntake, int calorieBurn,
            int waterIntake, double sleepHours) {
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.weight = weight;
        this.calorieIntake = calorieIntake;
        this.calorieBurn = calorieBurn;
        this.waterIntake = waterIntake;
        this.sleepHours = sleepHours;
    }

    public User login(String username, String password) {
        sqlConnect dbConnection = new sqlConnect();
        return dbConnection.authenticateUser(username, password);
    }

    public static User register(String username, String password, String dateOfBirth, int height, int weight,
            int calorieIntake, int calorieBurn, int waterIntake, double sleepHours) {
        sqlConnect dbConnection = new sqlConnect();
        User newUser = new User(username, password, dateOfBirth, height, weight, calorieIntake, calorieBurn, waterIntake,
                sleepHours);
        return dbConnection.registerUser(newUser) ? newUser : null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    // Utility method to calculate age from date of birth
    public int getAge() {
        LocalDate birthDate = LocalDate.parse(dateOfBirth);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getCalorieIntake() {
        return calorieIntake;
    }

    public int getCalorieBurn() {
        return calorieBurn;
    }

    public int getWaterIntake() {
        return waterIntake;
    }

    public double getSleepHours() {
        return sleepHours;
    }

    // Setter methods for health goals
    public void setCalorieIntake(int calorieIntake) {
        this.calorieIntake = calorieIntake;
    }

    public void setCalorieBurn(int calorieBurn) {
        this.calorieBurn = calorieBurn;
    }

    public void setWaterIntake(int waterIntake) {
        this.waterIntake = waterIntake;
    }

    public void setSleepHours(double sleepHours) {
        this.sleepHours = sleepHours;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean updateUserGoals() {
        sqlConnect dbConnection = new sqlConnect();
        return dbConnection.updateUserGoals(this);
    }
}
