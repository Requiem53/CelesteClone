package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.baringproductions.celeste.Screens.PlayScreen;

public class DashGem extends InteractiveTile{

    private boolean isActive = true;

    public DashGem(World world, TiledMap map, MapObject object) {
        super(world, map, object);

        fixture.setUserData(this);
        fixture.setSensor(true);

    }

    @Override
    public void onFeetContact() {
        if(isActive) PlayScreen.player.canDash = true;
        isActive = false;
        synchronized (restoreGem){
            if(!restoreGem.isScheduled()) Timer.schedule(restoreGem, 2.5f);
        }
    }

    private final Timer.Task restoreGem = new Timer.Task() {
        @Override
        public void run() {
            System.out.println("yeah");
            isActive = true;
        }
    };

    @Override
    public void onFeetLeave() {

    }
}
