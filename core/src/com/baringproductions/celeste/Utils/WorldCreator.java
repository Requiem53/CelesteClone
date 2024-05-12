package com.baringproductions.celeste.Utils;

import com.badlogic.gdx.maps.MapLayer;
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
import com.baringproductions.celeste.Tiles.Ground;

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
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();


            new Ground(world, map, rect);

//            bdef.type = BodyDef.BodyType.StaticBody;
//            bdef.position.set((rect.getX() + rect.getWidth() / 2) / CelesteGame.PPM, (rect.getY() + rect.getHeight() / 2) / CelesteGame.PPM);
//
//            body = world.createBody(bdef);
//
//            shape.setAsBox(rect.getWidth() / 2 / CelesteGame.PPM, rect.getHeight() / 2 / CelesteGame.PPM);
//            fdef.shape = shape;
//            body.createFixture(fdef);
//
//            shape.dispose();
        }

        // wall
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(PolygonMapObject.class)){

//            Polygon polygon = ((PolygonMapObject) object).getPolygon();
//
////
//            bdef.type = BodyDef.BodyType.StaticBody;
//            bdef.position.set((polygon.getX()/CelesteGame.PPM), (polygon.getY()/CelesteGame.PPM));
//
//            polygon.setPosition(0, 0);
//            polygon.setScale(CelesteGame.PPM, CelesteGame.PPM);
//
//            shape.set(polygon.getTransformedVertices());
//            fdef.shape = shape;
//
//            world.createBody(bdef).createFixture(fdef);
//            body = world.createBody(bdef);
//
//
//            float[] vertices = polygon.getTransformedVertices();

//            _polyShape = new PolygonShape();
//            Polygon _polygon = ((PolygonMapObject) _mapObject).getPolygon();
//
//            _bdef.position.set((_polygon.getX() * MAP_SCALE / PPM),
//                    _polygon.getY() * MAP_SCALE / PPM);
//
//            _polygon.setPosition(0, 0);
//            _polygon.setScale(MAP_SCALE / PPM, MAP_SCALE / PPM);
//
//            _polyShape.set(_polygon.getTransformedVertices());
//            _fdef.shape = _polyShape;
//
//            m_world.createBody(_bdef).createFixture(_fdef);




//            shape.set(vertices);
////
//            fdef.shape = shape;
//            body.createFixture(fdef);

//            shape.dispose();
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
