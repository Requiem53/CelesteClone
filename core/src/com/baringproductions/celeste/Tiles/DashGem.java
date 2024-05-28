package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Screens.PlayScreen;

public class DashGem extends InteractiveTile{

    public boolean isActive = true;
    public Sprite sprite;

    public DashGem(World world, TiledMap map, MapObject object) {
        super(world, map, object);

        fixture.setUserData(this);
        fixture.setSensor(true);

        TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get("graphics");
        TiledMapTileLayer.Cell cell = tileLayer.getCell((int)(body.getPosition().x * CelesteGame.PPM / 16),
                (int)(body.getPosition().y * CelesteGame.PPM / 16));
        if(cell != null){
            sprite = new Sprite(cell.getTile().getTextureRegion());
            cell.setTile(null);
            sprite.setBounds(0, 0, 16 / CelesteGame.PPM, 16 / CelesteGame.PPM);
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
        }
    }

    @Override
    public void onFeetContact() {
        if (isActive) {
            PlayScreen.player.canDash = true;
            CelesteGame.manager.get("Audio/SoundEffects/dashgem_get.wav", Sound.class).play(0.2f);
        }

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

    public void forceRespawn(){
        isActive = true;
        synchronized (restoreGem) {
            if(restoreGem.isScheduled()){
                restoreGem.cancel();
            }
        }
    }

    @Override
    public void onFeetLeave() {

    }
}
