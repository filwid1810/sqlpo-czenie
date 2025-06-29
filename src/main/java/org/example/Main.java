package org.example;

import org.example.database.DatabaseConnection;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {

        DatabaseConnection db = new DatabaseConnection();
        String dbUrl = "database.db";
        db.connect(dbUrl);
       Connection con = db.getConnection();

        try {
            Statement  stmt = con.createStatement();

        {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT)");

            stmt.execute("INSERT INTO users (name) VALUES ('Anna')");
            stmt.execute("INSERT INTO users (name) VALUES ('Jan')");
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            System.out.println("Użytkownicy:");

            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("name"));
            }

        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
        }
       db.disconnect();
//UZYJ TEGO, JAK BEDZIESZ MUSIAŁ ZROBIC BAZE DANYCH
    }
}