package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
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

    MapObject object;
    public static boolean isNewASensor = false;

    public InteractiveTile(World world, TiledMap map, MapObject object) {
        this.object = object;
        this.world = world;
        this.map = map;
        this.bounds = ((RectangleMapObject) object).getRectangle();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        if(isNewASensor){
            fdef.isSensor = true;
            isNewASensor = false;
        }
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
    
    public static void setNewSensor(){
        isNewASensor = true;
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
