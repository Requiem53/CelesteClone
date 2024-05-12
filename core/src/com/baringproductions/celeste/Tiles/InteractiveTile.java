package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.baringproductions.celeste.CelesteGame;

public abstract class InteractiveTile {

    World world;
    TiledMap map;
    TiledMapTile tile;
    Rectangle bounds;
    Body body;
    Fixture fixture;

    public InteractiveTile(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX()+ bounds.getWidth()/2)/ CelesteGame.PPM,
                (bounds.getY()+ bounds.getHeight()/2)/ CelesteGame.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth()/2/CelesteGame.PPM, bounds.getHeight()/2/CelesteGame.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    public abstract void onFeetContact();
}
