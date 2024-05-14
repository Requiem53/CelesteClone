package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.CelesteGame;

public class MovingPlatform extends InteractiveTile {
    public MovingPlatform(World world, TiledMap map, MapObject object) {
        super(world, map, object);
    }

    @Override
    public void onFeetContact() {

    }


    public void updatePosition(float newX, float newY) {
        // Convert newX and newY from pixels to Box2D units
        float newBox2DPosX = newX / CelesteGame.PPM;
        float newBox2DPosY = newY / CelesteGame.PPM;

        // Update the Box2D body position
        body.setTransform(newBox2DPosX, newBox2DPosY, body.getAngle());

        // Update the RectangleMapObject position
        bounds.setPosition(newX, newY);
    }

    public void update(float delta, float speed) {
        // Increase the x-coordinate based on the speed and delta time
        float newX = bounds.getX() + speed * delta;
        float newY = bounds.getY();

        updatePosition(newX, newY);
    }
}
