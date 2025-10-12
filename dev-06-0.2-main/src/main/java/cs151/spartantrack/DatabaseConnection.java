package cs151.spartantrack;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        loadDatabaseProperties();
    }

    private static void loadDatabaseProperties() {
        Properties props = new Properties();

        try (InputStream input = DatabaseConnection.class.getResourceAsStream("/database.properties")) {
            if (input != null) {
                props.load(input);
                URL = props.getProperty("database.url");
                USER = props.getProperty("database.username");
                PASSWORD = props.getProperty("database.password");

                System.out.println("Loaded database configuration from properties file");
            } else {
                throw new IOException("database.properties file not found");
            }
        } catch (IOException e) {
            // Fallback to default values
            System.out.println("Could not load database.properties, using defaults: " + e.getMessage());
            URL = "jdbc:mysql://localhost:3306/spartantrack";
            USER = "root";
            PASSWORD = "root";
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed:");
            System.err.println("URL: " + URL);
            System.err.println("User: " + USER);
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }
}