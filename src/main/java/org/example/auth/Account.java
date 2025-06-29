package org.example.auth;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.example.database.DatabaseConnection;

import javax.naming.AuthenticationException;

public class Account {
    private final String username;
    private  final int id;


    public Account(String username, int id) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return  "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    public static class Persistence {
        public static void init() {
            try {
                String createSQLTable = "CREATE TABLE IF NOT EXISTS account( " +
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT NOT NULL," +
                        "password TEXT NOT NULL)";
                PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(createSQLTable);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

}
public static int register(String username, String password) {
        String  hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        try {
            String insertSQL = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(insertSQL);

            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next())
                return resultSet.getInt(1);
            else throw new SQLException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Account authenticate(String username, String password) throws AuthenticationException {
        String sql = "SELECT id, username, password FROM account WHERE username = ?";

        PreparedStatement statement = null;
        try {
            statement = DatabaseConnection.getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement = DatabaseConnection.getConnection().prepareStatement(sql);

        statement.setString(1, username);

   if(statement.execute())throw new RuntimeException("Statement execute failed");
   ResultSet result = statement.getResultSet();

   if(!result.next())throw new AuthenticationException("User not found");
   String hashedPassword = result.getString(3);

   boolean okay = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword.toCharArray()).verified;

   if(!okay)throw new AuthenticationException("Invalid password");

   return new Account(
           username,
           result.getInt(1)
    );



    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    }




}

