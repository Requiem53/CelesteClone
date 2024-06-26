package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Player;
import com.baringproductions.celeste.Screens.PlayScreen;

public class Berry extends InteractiveTile{
    private TiledMapTileLayer.Cell cell;
    public Berry(World world, TiledMap map, MapObject object) {
        super(world, map, object);

        fixture.setSensor(true);
        setCategoryFilter(CelesteGame.BERRY_BIT);
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get("graphics");
        cell = tileLayer.getCell((int)(body.getPosition().x * CelesteGame.PPM / 16),
                (int)(body.getPosition().y * CelesteGame.PPM / 16));

        fixture.setUserData(this);
    }

    public void onBodyContact(){
        if(fixture.getFilterData().categoryBits != CelesteGame.DESTROYED_BIT){
            System.out.println("Berry position: "+ (body.getPosition().x * 4) + ", " + (body.getPosition().y * 4));
            Player.collectBerry(new Vector2((int)(body.getPosition().x * 4), ((int)(body.getPosition().y * 4))));
            destroyBerry();
        }
    }

    public void destroyBerry(){
        setCategoryFilter(CelesteGame.DESTROYED_BIT);
        cell.setTile(null);
    }

    public boolean comparePosition(Vector2 vector){
        return vector.x == (int)(body.getPosition().x * 4) && vector.y == (int)(body.getPosition().y * 4);
    }
    @Override
    public void onFeetContact() {
    }

    @Override
    public void onFeetLeave() {

    }
}