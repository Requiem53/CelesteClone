package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Screens.PlayScreen;
import com.badlogic.gdx.utils.Timer;


public class CollapsingPlatform extends InteractiveTile{
    public Sprite sprite;
    private float shakeTime;
    private float shakeDuration;    //in seconds
    private float shakeAmplitude;   //in pixels
    public boolean isShaking;

    private float spriteOriginalX, spriteOriginalY;

    public boolean collapsed;
    public CollapsingPlatform(World world, TiledMap map, MapObject object) {
        super(world, map, object);

        System.out.println(bounds.x);
        fixture.setUserData(this);
        setSpriteRegion();

        shakeDuration = 1.0f;
        shakeAmplitude = 1.0f / CelesteGame.PPM;
        isShaking = false;
        spriteOriginalX = sprite.getX();
        spriteOriginalY = sprite.getY();
        collapsed = false;
    }

    @Override
    public void onFeetContact() {
        System.out.println("feet contact");
        PlayScreen.player.landed();
        startShaking();
    }

    @Override
    public void onFeetLeave() {
        System.out.println("feet left");
//        stopShaking();
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
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
    }


    public void startShaking(){
        isShaking = true;
        shakeTime = 0;
    }
    public void stopShaking(){
        isShaking = false;
        shakeTime = 0;
        sprite.setPosition(spriteOriginalX, spriteOriginalY); // Reset to original position
    }
    public void updateShaking(float dt){
        shakeTime += dt;

        if (shakeTime < shakeDuration) {
            float shakeX = MathUtils.random(-shakeAmplitude, shakeAmplitude);
            sprite.setPosition(spriteOriginalX + shakeX, spriteOriginalY);
        } else {
            stopShaking();
            setCategoryFilter(CelesteGame.DESTROYED_BIT);
            collapsed = true;

            CelesteGame.manager.get("Audio/SoundEffects/platform_collapse.wav", Sound.class).play(0.5f);

            synchronized (waitForRespawn){
                Timer.schedule(waitForRespawn, 2f);
            }
        }
    }

    public final Timer.Task waitForRespawn = new Timer.Task() {
        @Override
        public void run() {
            collapsed = false;
            setCategoryFilter(CelesteGame.DEFAULT_BIT);
            shakeTime = 0;
        }
    };
}