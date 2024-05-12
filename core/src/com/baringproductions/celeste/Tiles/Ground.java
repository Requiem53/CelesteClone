package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Ground extends InteractiveTile {

    public Ground(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);

        fixture.setUserData(this);
    }

    @Override
    public void onFeetContact() {
        Gdx.app.log("GROUND", "PLAYER CONTACTED");
    }
}
