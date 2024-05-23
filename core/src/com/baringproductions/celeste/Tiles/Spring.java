package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Player;
import com.baringproductions.celeste.Screens.PlayScreen;

public class Spring extends InteractiveTile {
    public Spring(World world, TiledMap map, MapObject object) {
        super(world, map, object);

        fixture.setSensor(true);
        fixture.setUserData(this);
    }



    @Override
    public void onFeetContact() {
        activateSpring();
    }

    @Override
    public void onFeetLeave() {

    }

    public void activateSpring() {
        float impulseY = 2.0f;
        Player p = PlayScreen.player;
        p.body.setLinearVelocity(new Vector2(0.0f, 0.0f));
        p.body.applyLinearImpulse(0.0f, impulseY, p.getX(), p.getY(), true);

    }

}
