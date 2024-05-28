package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Player;
import com.baringproductions.celeste.Screens.PlayScreen;

import java.awt.*;
import java.util.Iterator;

public class MovingPlatform extends InteractiveTile {
    public Sprite sprite;
    public Vector2 originalPosition;
    private float speed;
    private float range;
    private float distanceTravelled;
    private boolean goingRight;
    public boolean playerOnThisPlatform;

    public MovingPlatform(World world, TiledMap map, MapObject object) {
        super(world, map, object);
        setSpriteRegion();
        fixture.setUserData(this);
        originalPosition = new Vector2(body.getPosition());

//        range = 5;         //range based on tiles travelled
        try{
            range = (Integer) object.getProperties().get("range");
        }catch (NullPointerException e){
            range = 5f;
        }
        try{
            speed = (Integer) object.getProperties().get("speed");
        }catch (NullPointerException e){
            speed = 10f;
        }

        distanceTravelled = 0f;
        goingRight = true;
        playerOnThisPlatform = false;

    }
    @Override
    public void onFeetContact() {
        PlayScreen.player.landed();
        PlayScreen.player.onPlatform = true;
        playerOnThisPlatform = true;
    }

    @Override
    public void onFeetLeave() {
        PlayScreen.player.onPlatform = false;
        playerOnThisPlatform = false;
    }

    public void resetPosition(){
        body.setTransform(originalPosition.x, originalPosition.y, body.getAngle());
        bounds.setPosition((originalPosition.x * CelesteGame.PPM - bounds.getWidth()/2), (originalPosition.y * CelesteGame.PPM - bounds.getHeight()/2));
        sprite.setPosition(originalPosition.x - sprite.getWidth() / 2, originalPosition.y - sprite.getHeight() / 2 - 0.075f);
        goingRight = true;
        distanceTravelled = 0f;
    }

    public void updatePosition(float newX, float newY) {
        // Convert newX and newY from Body position to bounds position
        float newBoundsPosX = newX * CelesteGame.PPM;
        float newBoundsPosY = newY * CelesteGame.PPM;
        // Update the Box2D body position
        body.setTransform(newX, newY, body.getAngle());

        // Update the RectangleMapObject position
        bounds.setPosition(newBoundsPosX, newBoundsPosY);
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2 - 0.075f);
    }
    public void update(float delta) {
        // Increase the x-coordinate based on the speed and delta time
        float distance = (speed * delta);

        float newX = body.getPosition().x;
        float newY = body.getPosition().y;

        if(distanceTravelled >= range) goingRight = false;
        if(distanceTravelled <= 0) goingRight = true;

        if(!goingRight) distance *= -1;

        distanceTravelled += distance / 16 / 2;
        newX += (distance / CelesteGame.PPM / 2);

        Player player = PlayScreen.player;
        if(player.onPlatform && playerOnThisPlatform){
            player.body.setTransform(player.body.getPosition().x + (distance / CelesteGame.PPM / 2), player.body.getPosition().y, player.body.getAngle());
        }

        updatePosition(newX, newY);
    }

    private void setSpriteRegion(){
        int numTilesX = (int) (bounds.getWidth() / 16);
        int numTilesY = (int) (bounds.getHeight() / 16);
        if(numTilesY == 0) numTilesY = 1;

        int cellX = (int) (bounds.getX() / 16);
        int cellY = (int) (bounds.getY() / 16);

        Pixmap pixmap = new Pixmap(numTilesX * 16, numTilesY * 16, Pixmap.Format.RGBA8888);
        // Render each tile to the Pixmap
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get(1);
        for (int y = 0; y < numTilesY; y++) {
            for (int x = 0; x < numTilesX; x++) {
                TiledMapTileLayer.Cell cell = tileLayer.getCell( cellX + x, cellY + y);
                if (cell != null && cell.getTile() != null) {
                    TextureRegion region = cell.getTile().getTextureRegion();
                    region.getTexture().getTextureData().prepare();
                    pixmap.drawPixmap(region.getTexture().getTextureData().consumePixmap(),
                            region.getRegionX(), region.getRegionY(),
                            region.getRegionWidth(), region.getRegionHeight(),
                            x * 16, y * 16, 16, 16);
                    cell.setTile(null);
                }
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        TextureRegion textureRegion = new TextureRegion(texture, cellX, cellY, cellX + (cellX * numTilesX), cellY + (cellY * numTilesY));
        textureRegion.flip(false, true);

        sprite = new Sprite(texture);
        sprite.setRegion(0, 0, numTilesX * 16, numTilesY * 16);
        sprite.setBounds(0, 0, (numTilesX * 16) / CelesteGame.PPM, (numTilesY * 16) / CelesteGame.PPM);
    }
}