package com.baringproductions.celeste.Database;

import com.badlogic.gdx.math.Vector2;
import com.baringproductions.celeste.Screens.PlayScreen;
import com.baringproductions.celeste.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDatabase {
    private static String name;
    private static User user;
    private static int spawn;
    public static ArrayList<GameClass> game;
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
    public static void loadPlayer() {

    }
    public static void saveGame() {
        User user = PlayScreen.getUser();
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("SELECT x, y from tbluserberry WHERE userid = ?");
             PreparedStatement insertStatement = c.prepareStatement("INSERT INTO tbluserberry (userid, x, y) VALUES ("+user.getId()+", ?, ?)")) {

            statement.setInt(1, user.getId());
            ResultSet rs = statement.executeQuery();

            List<Vector2> alreadySavedBerries = new ArrayList<>();

            while(rs.next()){
                alreadySavedBerries.add(new Vector2(rs.getInt("x"), rs.getInt("y")));
            }

            List<Vector2> toInsertVectors = new ArrayList<>();
            for(Vector2 vector : user.getBerriesCollected()){
                if(!alreadySavedBerries.contains(vector)){
                    toInsertVectors.add(vector);
                }
            }

            Statement statement2 = c.createStatement();
            if(statement2.executeUpdate("UPDATE tbluser SET spawn = " +user.getSpawn() + " WHERE id = " + user.getId()) <= 0){
                System.out.println("Nothing saved");
            };
            statement2.close();
            if(!toInsertVectors.isEmpty()){
                for(Vector2 vector : toInsertVectors){
                    insertStatement.setInt(1, (int) vector.x);
                    insertStatement.setInt(2, (int) vector.y);
                    insertStatement.addBatch();
                }
                insertStatement.executeBatch();
            }

            System.out.println("Saved the game");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static User getNewUser(String name) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "INSERT INTO tbluser (name, spawn) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setInt(2, 0); // Assuming initial spawn point is 0

            int row = statement.executeUpdate();
            if (row > 0) {
                try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                    if (generatedKey.next()) {
                        int CURRENT_USER_ID = generatedKey.getInt(1);
                        System.out.println("CURR ID: " + CURRENT_USER_ID);
                        user = new User(CURRENT_USER_ID, name);
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<GameClass> loadGame() {
        game = new ArrayList<>();
        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement();) {
            String query = "SELECT * FROM tbluser";
            ResultSet res = statement.executeQuery(query);
            while (res.next()) {
                game.add(new GameClass(res.getString("name"), res.getInt("id")));
            }
            return game;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void saveGame(User user) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "UPDATE tbluser SET spawn=? WHERE id=? "
             )){
            statement.setInt(1, user.getSpawn());;
            statement.setInt(2, user.getId());
            int rowsUpdated = statement.executeUpdate();
            System.out.println("rowsUpdated: " + rowsUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static User loadPlayer(int id) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT * FROM tbluser WHERE id=?");
             PreparedStatement statement1 = c.prepareStatement(
                     "SELECT x, y FROM tbluserberry WHERE userid="+id
             )
             ){
            statement.setInt(1, id);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                user = new User(res.getInt("id"), res.getString("name"));
                user.setSpawn(res.getInt("spawn"));
            }
            res = statement1.executeQuery();
            while(res.next()){
                user.addBerry(new Vector2(res.getInt("x"), res.getInt("y")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    public static void deleteGame(int id){
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "DELETE from tbluser WHERE id=?"
             )){
            statement.setInt(1, id);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("rowsUpdated: " + rowsUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
