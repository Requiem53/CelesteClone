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
    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.spawn = 0;
        this.berriesCollected = new ArrayList<>();
    }

    public void addBerry(Vector2 vector){
        berriesCollected.add(vector);
    }
    public void updateSpawn(int spawn){
        this.spawn = spawn;
    }
}