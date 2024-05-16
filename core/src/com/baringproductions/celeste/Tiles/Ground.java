package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Screens.PlayScreen;

public class Ground extends InteractiveTile {

    public Ground(World world, TiledMap map, MapObject object) {
        super(world, map, object);

        fixture.setUserData(this);
    }

    @Override
    public void onFeetContact() {
        PlayScreen.player.canJump = true;
        PlayScreen.player.canDash = true;
        PlayScreen.player.onGround = true;
    }

}
