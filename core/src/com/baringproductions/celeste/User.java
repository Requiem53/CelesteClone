package com.baringproductions.celeste;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private int spawn;
    private List<Vector2> berriesCollected;
    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.spawn = 0;
        this.berriesCollected = new ArrayList<>();
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }
    public int getSpawn() {
        return spawn;
    }
    public List<Vector2> getBerriesCollected() {
        return berriesCollected;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setSpawn(int spawn) {
        this.spawn = spawn;
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