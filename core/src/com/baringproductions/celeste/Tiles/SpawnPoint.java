package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Player;

public class SpawnPoint extends InteractiveTile {
    public SpawnPoint(World world, TiledMap map, MapObject object) {
        super(world, map, object);

    }

    @Override
    public void onFeetContact() {

    }

    public void respawnPlayer(Player player) {
        float x = getCenterX();
        float y = getCenterY() + 16/CelesteGame.PPM;

        Gdx.app.log("X", x+"");
        Gdx.app.log("Y", y+"");
        player.body.setTransform(x, y, player.body.getAngle());
        player.isDead = false;
        player.canJump = true;

    }

}