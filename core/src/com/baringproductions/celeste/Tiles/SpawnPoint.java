package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Player;

public class SpawnPoint extends InteractiveTile {
    public SpawnPoint(World world, TiledMap map, MapObject object) {
        super(world, map, object);
        fixture.setSensor(true);
    }

    @Override
    public void onFeetContact() {

    }

    @Override
    public void onFeetLeave() {

    }

    public void respawnPlayer(Player player) {
        float x = getCenterX();
        float y = getCenterY() + 30/CelesteGame.PPM;


        if (player.isDead)
            CelesteGame.manager.get("Audio/SoundEffects/die.mp3", Sound.class).play(0.5f);


        Gdx.app.log("X", x+"");
        Gdx.app.log("Y", y+"");
        player.body.setTransform(x, y, player.body.getAngle());
        player.isDead = false;
        player.canJump = true;
        player.canDash = true;
        player.canMove = true;
        player.canLeft = true;
        player.canRight = true;

    }

}
