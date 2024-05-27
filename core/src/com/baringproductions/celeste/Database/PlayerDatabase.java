package com.baringproductions.celeste.Database;

import com.baringproductions.celeste.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDatabase {
    private static String name;
    private static User user;
    private static int spawn;
    public static void loadPlayer() {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS users (" +
                             "id INT AUTO_INCREMENT PRIMARY KEY," +
                             "name VARCHAR(50) NOT NULL," +
                             "email VARCHAR(100) NOT NULL," +
                             "password VARCHAR(50) )");) {
            statement.addBatch();
            // error
            System.out.println("TABLE created successfully");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveGame() {

    }
    public static User getNewUser(String name) {
//        try (Connection c = MySQLConnection.getConnection();
//             PreparedStatement statement = c.prepareStatement(
//                     "INSERT INTO users (name) VALUES (?)");) {
//            statement.setString(1, name);
//            int CURRENT_USER_ID = 0;
//            int row = statement.executeUpdate();
//            if (row > 0) {
//                try (ResultSet generatedKey = statement.getGeneratedKeys()) {
//                    if (generatedKey.next()) {
//                        CURRENT_USER_ID = generatedKey.getInt(1);
//
//
//                        System.out.println("CURR ID: " + CURRENT_USER_ID);
//                        user = new User(CURRENT_USER_ID, name);
//                        return user;
//                    }
//                    return null;
//                }
//            }
//        }catch (SQLException e) {
//            e.printStackTrace();
//        }
     return null;
    }
}
