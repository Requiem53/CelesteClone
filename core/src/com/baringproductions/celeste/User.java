package com.baringproductions.celeste;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private int spawn;
    private List<Vector2> berriesCollected;

    private static int latestID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpawn() {
        return spawn;
    }

    public void setSpawn(int spawn) {
        this.spawn = spawn;
    }

    public List<Vector2> getBerriesCollected() {
        return berriesCollected;
    }

    public void setBerriesCollected(List<Vector2> berriesCollected) {
        this.berriesCollected = berriesCollected;
    }

    public static int getLatestID() {
        return latestID;
    }

    public static void setLatestID(int latestID) {
        User.latestID = latestID;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.spawn = 0;
        this.berriesCollected = new ArrayList<>();
    }

    public void addBerry(Vector2 vector){
        berriesCollected.add(vector);
        System.out.println("Berries collected: " + berriesCollected.size());
    }
    public void updateSpawn(int spawn){
        this.spawn = spawn;
        System.out.println("New spawn: " + spawn);
    }
}