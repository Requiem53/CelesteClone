package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Player;
import com.baringproductions.celeste.Screens.PlayScreen;

public class Berry extends InteractiveTile{
    TiledMapTileLayer.Cell cell;
    public Berry(World world, TiledMap map, MapObject object) {
        super(world, map, object);

        setCategoryFilter(CelesteGame.BERRY_BIT);
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get("graphics");
//        cell = tileLayer.getCell((int)(bounds.getX() / 16), (int)(bounds.getX() / 16));
        cell = tileLayer.getCell((int)(body.getPosition().x * CelesteGame.PPM / 16),
                (int)(body.getPosition().y * CelesteGame.PPM / 16));

        fixture.setUserData(this);
    }

    synchronized public void onBodyContact(){
        if(fixture.getFilterData().categoryBits != CelesteGame.DESTROYED_BIT){
            setCategoryFilter(CelesteGame.DESTROYED_BIT);
            Player.collectBerry();
            cell.setTile(null);
        }
    }
    @Override
    public void onFeetContact() {
        PlayScreen.player.landed();
    }

    @Override
    public void onFeetLeave() {

    }
}