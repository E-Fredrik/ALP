package Logic;

import java.sql.*;
import Model.User;
import Model.waterLog;
import Model.mealLog;
import Model.exerciseLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class sqlConnect {
    private static final String DB_URL = "jdbc:mysql://localhost/healthtracker";

    public User getUserByUsername(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = DriverManager.getConnection(DB_URL, "root", "");
            String query = "SELECT * FROM users WHERE username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("dateOfBirth"),
                        resultSet.getInt("height"),
                        resultSet.getInt("weight"),
                        resultSet.getInt("calorieIntake"),
                        resultSet.getInt("calorieBurn"),
                        resultSet.getInt("waterIntake"),
                        resultSet.getDouble("sleepHours")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return user;
    }

    public User authenticateUser(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = DriverManager.getConnection(DB_URL, "root", "");


            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();


            if (resultSet.next()) {
                user = new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("dateOfBirth"),  // Changed from getInt("age")
                        resultSet.getInt("height"),
                        resultSet.getInt("weight"),
                        resultSet.getInt("calorieIntake"),
                        resultSet.getInt("calorieBurn"),
                        resultSet.getInt("waterIntake"),
                        resultSet.getDouble("sleepHours"));
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return user;
    }

    public boolean registerUser(User user) {
        Connection connection = null;
        PreparedStatement checkStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection(DB_URL, "root", "");

            
            String checkQuery = "SELECT username FROM users WHERE username = ?";
            checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, user.getUsername());
            resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {

                System.out.println("Username already exists. Please choose another one.");
                return false;
            }


            String insertQuery = "INSERT INTO users (username, password, dateOfBirth, height, weight, " +
                    "calorieIntake, calorieBurn, waterIntake, sleepHours) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, user.getUsername());
            insertStatement.setString(2, user.getPassword());
            insertStatement.setString(3, user.getDateOfBirth());  // Changed from setInt with getAge()
            insertStatement.setInt(4, user.getHeight());
            insertStatement.setInt(5, user.getWeight());
            insertStatement.setInt(6, user.getCalorieIntake());
            insertStatement.setInt(7, user.getCalorieBurn());
            insertStatement.setInt(8, user.getWaterIntake());
            insertStatement.setDouble(9, user.getSleepHours());

            int rowsAffected = insertStatement.executeUpdate();

            success = (rowsAffected > 0);
            if (success) {
                System.out.println("User registered successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
        } finally {
            closeResources(resultSet, checkStatement, null);
            closeResources(null, insertStatement, connection);
        }

        return success;
    }

    public boolean updateUserGoals(User user) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection(DB_URL, "root", "");
            
            String updateQuery = "UPDATE users SET dateOfBirth = ?, height = ?, weight = ?, calorieIntake = ?, " +
                            "calorieBurn = ?, waterIntake = ?, sleepHours = ? WHERE username = ?";
            
            statement = connection.prepareStatement(updateQuery);
            statement.setString(1, user.getDateOfBirth());  // Use dateOfBirth instead of age
            statement.setInt(2, user.getHeight());
            statement.setInt(3, user.getWeight());
            statement.setInt(4, user.getCalorieIntake());
            statement.setInt(5, user.getCalorieBurn());
            statement.setInt(6, user.getWaterIntake());
            statement.setDouble(7, user.getSleepHours());
            statement.setString(8, user.getUsername());
            
            int rowsAffected = statement.executeUpdate();
            
            success = (rowsAffected > 0);
            if (success) {
                System.out.println("User goals updated successfully!");
            } else {
                System.out.println("Failed to update user goals.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating user goals: " + e.getMessage());
        } finally {
            closeResources(null, statement, connection);
        }
        
        return success;
    }

    // Save water log to database
    public boolean saveWaterLog(String username, Date logDate, int waterIntake) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean success = false;
        
        try {
            connection = DriverManager.getConnection(DB_URL, "root", "");
            String query = "INSERT INTO water_logs (username, log_date, water_intake) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setTimestamp(2, new Timestamp(logDate.getTime()));
            statement.setInt(3, waterIntake);
            
            int rowsAffected = statement.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error saving water log: " + e.getMessage());
        } finally {
            closeResources(null, statement, connection);
        }
        
        return success;
    }

    // Save meal log to database
    public boolean saveMealLog(String username, Date logDate, String foodName, int calories) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean success = false;
        
        try {
            connection = DriverManager.getConnection(DB_URL, "root", "");
            String query = "INSERT INTO meal_logs (username, log_date, food_name, calories) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setTimestamp(2, new Timestamp(logDate.getTime()));
            statement.setString(3, foodName);
            statement.setInt(4, calories);
            
            int rowsAffected = statement.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error saving meal log: " + e.getMessage());
        } finally {
            closeResources(null, statement, connection);
        }
        
        return success;
    }

    // Save exercise log to database
    public boolean saveExerciseLog(String username, Date logDate, String exerciseType, 
                             int duration, double intensity, int caloriesBurned) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean success = false;
        
        try {
            connection = DriverManager.getConnection(DB_URL, "root", "");
            String query = "INSERT INTO exercise_logs (username, log_date, exercise_type, duration, intensity, calories_burned) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setTimestamp(2, new Timestamp(logDate.getTime()));
            statement.setString(3, exerciseType);
            statement.setInt(4, duration);
            statement.setDouble(5, intensity);
            statement.setInt(6, caloriesBurned);
            
            int rowsAffected = statement.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error saving exercise log: " + e.getMessage());
        } finally {
            closeResources(null, statement, connection);
        }
        
        return success;
    }

    // Get water logs by date range
    public List<waterLog> getWaterLogs(String username, Date startDate, Date endDate) {
        List<waterLog> logs = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DriverManager.getConnection(DB_URL, "root", "");
            String query = "SELECT * FROM water_logs WHERE username = ? AND log_date BETWEEN ? AND ? ORDER BY log_date DESC";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setTimestamp(2, new Timestamp(startDate.getTime()));
            statement.setTimestamp(3, new Timestamp(endDate.getTime()));
            
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Date logDate = resultSet.getTimestamp("log_date");
                int waterIntake = resultSet.getInt("water_intake");
                
                waterLog log = new waterLog(logDate, waterIntake);
                logs.add(log);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving water logs: " + e.getMessage());
        } finally {
            closeResources(resultSet, statement, connection);
        }
        
        return logs;
    }

    // Get meal logs by date range
    public List<mealLog> getMealLogs(String username, Date startDate, Date endDate) {
        List<mealLog> logs = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DriverManager.getConnection(DB_URL, "root", "");
            String query = "SELECT * FROM meal_logs WHERE username = ? AND log_date BETWEEN ? AND ? ORDER BY log_date DESC";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setTimestamp(2, new Timestamp(startDate.getTime()));
            statement.setTimestamp(3, new Timestamp(endDate.getTime()));
            
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Date logDate = resultSet.getTimestamp("log_date");
                String foodName = resultSet.getString("food_name");
                int calories = resultSet.getInt("calories");
                
                mealLog log = new mealLog(logDate, foodName, calories);
                logs.add(log);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving meal logs: " + e.getMessage());
        } finally {
            closeResources(resultSet, statement, connection);
        }
        
        return logs;
    }

    // Get exercise logs by date range
    public List<exerciseLog> getExerciseLogs(String username, Date startDate, Date endDate) {
        List<exerciseLog> logs = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DriverManager.getConnection(DB_URL, "root", "");
            String query = "SELECT * FROM exercise_logs WHERE username = ? AND log_date BETWEEN ? AND ? ORDER BY log_date DESC";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setTimestamp(2, new Timestamp(startDate.getTime()));
            statement.setTimestamp(3, new Timestamp(endDate.getTime()));
            
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Date logDate = resultSet.getTimestamp("log_date");
                String exerciseType = resultSet.getString("exercise_type");
                int duration = resultSet.getInt("duration");
                double intensity = resultSet.getDouble("intensity");
                int caloriesBurned = resultSet.getInt("calories_burned");
                
                // Need to get a User object for the exerciseLog constructor
                User user = getUserByUsername(username);
                exerciseLog log = new exerciseLog(logDate, user);
                // Set the values that we retrieved from the database
                log.setExerciseType(exerciseType);
                log.setDuration(duration);
                log.setIntensity(intensity);
                log.setCaloriesBurned(caloriesBurned);
                
                logs.add(log);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving exercise logs: " + e.getMessage());
        } finally {
            closeResources(resultSet, statement, connection);
        }
        
        return logs;
    }

    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }
}
