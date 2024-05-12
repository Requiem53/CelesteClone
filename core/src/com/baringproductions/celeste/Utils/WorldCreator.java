package com.baringproductions.celeste.Utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Screens.PlayScreen;

public class WorldCreator {

    World world;
    TiledMap map;


    public WorldCreator(PlayScreen screen) {
        world = screen.getWorld();
        map = screen.getMap();
        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // ground
        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / CelesteGame.PPM, (rect.getY() + rect.getHeight() / 2) / CelesteGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / CelesteGame.PPM, rect.getHeight() / 2 / CelesteGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // wall
        for(MapObject object : map.getLayers().get(2).getObjects()){

            Shape s;
            if (object instanceof PolylineMapObject)
                s = createPoly((PolylineMapObject) object);
            else
                continue;

            bdef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bdef);
            body.createFixture(s, 1.0f);

            s.dispose();
//            bdef.position.set((rect.getX() + rect.getWidth() / 2) / CelesteGame.PPM, (rect.getY() + rect.getHeight() / 2) / CelesteGame.PPM);
//
//
//            shape.setAsBox(rect.getWidth() / 2 / CelesteGame.PPM, rect.getHeight() / 2 / CelesteGame.PPM);
//            fdef.shape = shape;
        }
    }

    ChainShape createPoly(PolylineMapObject poly) {
        float[] vertices = poly.getPolyline().getTransformedVertices();
        Vector2[] worldVerts = new Vector2[vertices.length/2];

        for (int i = 0; i < worldVerts.length; i++) {
            worldVerts[i] = new Vector2(vertices[i*2]/CelesteGame.PPM,
                    vertices[i*2+1]/CelesteGame.PPM);
        }

        ChainShape cs = new ChainShape();
        cs.createChain(worldVerts);
        return cs;
    }
}
