package com.baringproductions.celeste.Database;

import com.baringproductions.celeste.User;

import java.sql.*;

public class PlayerDatabase {
    private static String name;
    private static User user;
    private static int spawn;
    public static void initTables() {
        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {

            c.setAutoCommit(false);

            statement.addBatch("CREATE TABLE IF NOT EXISTS tbluser (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(20) NOT NULL," +
                    "spawn INT(100) NOT NULL)");
            statement.addBatch("CREATE TABLE IF NOT EXISTS tbluserberry (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "userid INT NOT NULL," +
                    "x INT(10) NOT NULL," +
                    "y INT(10) NOT NULL," +
                    "FOREIGN KEY (userid) REFERENCES tbluser(id) ON UPDATE CASCADE ON DELETE CASCADE)");

            statement.executeBatch();
            System.out.println("TABLE created successfully");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveGame() {

    }
    public static User getNewUser(String name) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "INSERT INTO users (name) VALUES (?)");) {
            statement.setString(1, name);
            int CURRENT_USER_ID = 0;
            int row = statement.executeUpdate();
            if (row > 0) {
                try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                    if (generatedKey.next()) {
                        CURRENT_USER_ID = generatedKey.getInt(1);


                        System.out.println("CURR ID: " + CURRENT_USER_ID);
                        user = new User(CURRENT_USER_ID, name);
                        return user;
                    }
                    return null;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
