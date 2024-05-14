package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Wall extends InteractiveTile {

    public Wall(World world, TiledMap map, MapObject object) {
        super(world, map, object);
    }

    @Override
    public void onFeetContact() {

    }
}
