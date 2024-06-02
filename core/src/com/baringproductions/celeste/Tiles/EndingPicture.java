package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.CelesteGame;

public class EndingPicture extends InteractiveTile{

    public Sprite sprite;

    public EndingPicture(World world, TiledMap map, MapObject object) {
        super(world, map, object);
        fixture.setSensor(true);
        sprite = new Sprite(new Texture("sunset/Maurice Ta.png"));
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setSize(bounds.getWidth() / CelesteGame.PPM, bounds.getHeight() / CelesteGame.PPM);
    }

    @Override
    public void onFeetContact() {

    }

    @Override
    public void onFeetLeave() {

    }
}
