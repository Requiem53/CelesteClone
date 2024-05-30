package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.baringproductions.celeste.CelesteGame;

public abstract class InteractiveTile {

    private World world;
    public Body body;

    protected Rectangle bounds;
    protected Fixture fixture;

    protected MapObject object;
    protected TiledMap map;

    public InteractiveTile(World world, TiledMap map, MapObject object) {
        this.object = object;
        this.world = world;
        this.map = map;
        this.bounds = ((RectangleMapObject) object).getRectangle();

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

        setCategoryFilter(CelesteGame.DEFAULT_BIT);
    }
    
    public Rectangle getBounds() {
        return bounds;
    }

    public float getCenterX() {
        return (bounds.getX() + bounds.getWidth() / 2) / CelesteGame.PPM;
    }

    public float getCenterY() {
        return (bounds.getY() + bounds.getHeight() / 2) / CelesteGame.PPM;
    }

    public abstract void onFeetContact();
    public abstract void onFeetLeave();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
}
