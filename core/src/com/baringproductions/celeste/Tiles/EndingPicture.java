package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.CelesteGame;

public class EndingPicture extends InteractiveTile{

    public Sprite sprite;
    public Texture texture;

    public EndingPicture(World world, TiledMap map, MapObject object) {
        super(world, map, object);
        fixture.setSensor(true);
        sprite = new Sprite(new Texture("sunset/Maurice Ta.png"));
//        sprite.setBounds(0, 0, 14 * 16, 8 * 16);
//        sprite.setPosition((bounds.getX() - bounds.getWidth() /2) / CelesteGame.PPM,  (bounds.getY() - bounds.getHeight() / 2 / CelesteGame.PPM);
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setSize(bounds.getWidth() / CelesteGame.PPM, bounds.getHeight() / CelesteGame.PPM);
//        texture = new Texture("sunset/Maurice Ta.png");
    }

    @Override
    public void onFeetContact() {

    }

    @Override
    public void onFeetLeave() {

    }
}
