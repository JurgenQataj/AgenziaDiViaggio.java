package model.dao;

import model.Role;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static Connection connection;

    private ConnectionFactory() {
    }

    static {
        try (InputStream fileInput = new FileInputStream("resources/db.properties")) {
            Properties properties = new Properties();
            properties.load(fileInput);

            String connectionUrl = properties.getProperty("CONNECTION_URL");
            String username = properties.getProperty("LOGIN_USER");
            String password = properties.getProperty("LOGIN_PASS");

            connection = DriverManager.getConnection(connectionUrl, username, password);
        } catch (IOException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void changeRole(Role role) throws SQLException {
        connection.close();

        try (InputStream fileInput = new FileInputStream("resources/db.properties")) {
            Properties properties = new Properties();
            properties.load(fileInput);

            String connectionUrl = properties.getProperty("CONNECTION_URL");
            String username = properties.getProperty(String.format("%s_USER", role.name()));
            String password = properties.getProperty(String.format("%s_PASS", role.name()));

            connection = DriverManager.getConnection(connectionUrl, username, password);
        } catch (IOException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
