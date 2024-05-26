package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.Screens.PlayScreen;

public class Spike extends InteractiveTile {
    public Spike(World world, TiledMap map, MapObject object) {
        super(world, map, object);

        fixture.setUserData(this);
    }

    @Override
    public void onFeetContact() {
//        PlayScreen.player.isDead = true;
    }

    public void onBodyContact(){
        PlayScreen.player.isDead = true;
    }

    @Override
    public void onFeetLeave() {

    }
}
