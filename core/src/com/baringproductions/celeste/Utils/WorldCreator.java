package com.baringproductions.celeste.Utils;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
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
                map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {

            new Ground(world, map, object);
        }

        // wall
        for (RectangleMapObject object :
                map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {

            new Wall(world, map, object);
        }

        // spike
        for (RectangleMapObject object :
                map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {

            new Spike(world, map, object);
        }

        // spawn point
        for (RectangleMapObject object :
                map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {

            PlayScreen.spawnPoints.add(new SpawnPoint(world, map, object));
        }

        // platforms
        for (RectangleMapObject object :
                map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {

            PlayScreen.platforms.add(new MovingPlatform(world, map, object));
        }

        //dash gem
        for (RectangleMapObject object :
            map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {

            new DashGem(world, map, object);
        }

    }
}
