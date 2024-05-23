package com.baringproductions.celeste.Utils;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.baringproductions.celeste.Player;
import com.baringproductions.celeste.Screens.PlayScreen;
import com.baringproductions.celeste.Tiles.*;

public class WorldCreator {

    World world;
    TiledMap map;

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

            new DashGem(world, map, object);
        }

        //collapsing platforms
        for (RectangleMapObject object :
                map.getLayers().get("collapsing platforms").getObjects().getByType(RectangleMapObject.class)) {

            PlayScreen.collapsingPlatforms.add(new CollapsingPlatform(world, map, object));
        }

        //berry
        for (RectangleMapObject object :
                map.getLayers().get("berry").getObjects().getByType(RectangleMapObject.class)) {

            new Berry(world, map, object);
        }

        // spring
        for (RectangleMapObject object :
                map.getLayers().get("spring").getObjects().getByType(RectangleMapObject.class)) {

            new Spring(world, map, object);
        }
    }
}
