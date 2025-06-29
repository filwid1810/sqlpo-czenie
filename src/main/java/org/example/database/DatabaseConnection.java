package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static java.sql.Connection connection;

    public static Connection getConnection() {
        return connection;
    }
    public void connect(String dbUrl) {
    String url = "jdbc:sqlite:" + dbUrl;
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(url);
        } else {
            System.out.println("Connection already exists");
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }

    public void disconnect() {
      if(connection != null) {
          try {
                connection.close();
          } catch (SQLException e) {
                System.out.println("Error closing connection");
            }
        }
    }
}
