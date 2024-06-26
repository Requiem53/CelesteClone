package com.baringproductions.celeste.Utils;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.baringproductions.celeste.Player;
import com.baringproductions.celeste.Screens.PlayScreen;
import com.baringproductions.celeste.Tiles.*;
import com.baringproductions.celeste.User;

public class WorldCreator {

    private World world;
    private TiledMap map;

    public WorldCreator(PlayScreen screen) {
        world = screen.getWorld();
        map = screen.getMap();

        // ground
        for (RectangleMapObject object :
                map.getLayers().get("ground").getObjects().getByType(RectangleMapObject.class)) {

            new Ground(world, map, object);
        }

        // wall
        for (RectangleMapObject object :
                map.getLayers().get("walls").getObjects().getByType(RectangleMapObject.class)) {

            new Wall(world, map, object);
        }

        // spike
        for (RectangleMapObject object :
                map.getLayers().get("spikes").getObjects().getByType(RectangleMapObject.class)) {

            new Spike(world, map, object);
        }

        // spawn point
        for (RectangleMapObject object :
                map.getLayers().get("spawn").getObjects().getByType(RectangleMapObject.class)) {

            PlayScreen.spawnPoints.add(new SpawnPoint(world, map, object));
        }

        // moving platforms
        for (RectangleMapObject object :
                map.getLayers().get("moving platforms").getObjects().getByType(RectangleMapObject.class)) {

            PlayScreen.movingPlatforms.add(new MovingPlatform(world, map, object));
        }

        //dash gem
        for (RectangleMapObject object :
            map.getLayers().get("dashGem").getObjects().getByType(RectangleMapObject.class)) {

            PlayScreen.dashGems.add(new DashGem(world, map, object));
        }

        //collapsing platforms
        for (RectangleMapObject object :
                map.getLayers().get("collapsing platforms").getObjects().getByType(RectangleMapObject.class)) {

            PlayScreen.collapsingPlatforms.add(new CollapsingPlatform(world, map, object));
        }

        //berry
        for (RectangleMapObject object :
                map.getLayers().get("berry").getObjects().getByType(RectangleMapObject.class)) {
            Berry berry = new Berry(world, map, object);
            for(Vector2 vector : PlayScreen.getUser().getBerriesCollected()){
                if(berry.comparePosition(vector)){
                    berry.destroyBerry();
                }
            }
        }

        // spring
        for (RectangleMapObject object :
                map.getLayers().get("spring").getObjects().getByType(RectangleMapObject.class)) {

            PlayScreen.springs.add(new Spring(world, map, object));
        }

        //picture
        for (RectangleMapObject object :
                map.getLayers().get("picture").getObjects().getByType(RectangleMapObject.class)) {

            PlayScreen.picture = new EndingPicture(world, map, object);
        }
    }
}