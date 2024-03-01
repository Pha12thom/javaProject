import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignupForm {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/carolly";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "null";

    public static void main(String[] args) {
        // Connect to the database
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Create a prepared statement for inserting a new user
            String insertQuery = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Set the values for the prepared statement
            preparedStatement.setString(1, "JohnDoe");
            preparedStatement.setString(2, "password123");
            preparedStatement.setString(3, "johndoe@example.com");

            // Execute the query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User registered successfully!");
            } else {
                System.out.println("Failed to register user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}