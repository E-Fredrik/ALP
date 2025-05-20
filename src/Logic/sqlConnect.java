package Logic;

import java.sql.*;
import Model.User;

public class sqlConnect {
    private static final String DB_URL = "jdbc:mysql://localhost/healthtracker";

    public User authenticateUser(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = DriverManager.getConnection(DB_URL, "root", "");

            // Create SQL query to check credentials
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            // If user exists, create User object
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

            // First, check if username already exists
            String checkQuery = "SELECT username FROM users WHERE username = ?";
            checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, user.getUsername());
            resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Username already exists
                System.out.println("Username already exists. Please choose another one.");
                return false;
            }

            // Username is available, insert new user
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
