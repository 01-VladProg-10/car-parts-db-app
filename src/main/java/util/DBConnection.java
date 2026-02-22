package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class responsible for database connection management.
 */
public class DBConnection {

    private static Connection connection;

    /**
     * Returns active database connection.
     *
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {

        if (connection == null || connection.isClosed()) {

            String url = Config.get("db.url");
            String user = Config.get("db.user");
            String password = Config.get("db.password");

            if (url == null || user == null || password == null) {
                throw new RuntimeException("Database configuration is missing in application.properties");
            }

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection established successfully.");
        }

        return connection;
    }
}