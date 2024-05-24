package com.baringproductions.celeste.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Player;
import com.baringproductions.celeste.Screens.PlayScreen;

public class Spring extends InteractiveTile {

    private static final int FRAME_COLS = 6, FRAME_ROWS = 1;
    private final Animation<TextureRegion> springAnimation;
    Texture springSheet;
    private float stateTime;

    boolean isActive;
    private float activeDuration;
    private float activeTimer;


    public Spring(World world, TiledMap map, MapObject object) {
        super(world, map, object);

        fixture.setSensor(true);
        fixture.setUserData(this);


        springSheet = new Texture("spring_spritesheet.png");
        TextureRegion[][] tmp = TextureRegion.split(springSheet,
                springSheet.getWidth() / FRAME_COLS,
                springSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        springAnimation = new Animation<>(0.1f, walkFrames);

        stateTime = 0f;
        isActive = false;
        activeDuration = 0f;
        activeTimer = 0f;
    }

    public void update(float dt) {

        if (isActive) {
            activeTimer += dt;
            if (activeTimer >= activeDuration) {
                isActive = false;
                stateTime = 0f;
            }
            stateTime += dt;
        }
    }

    public TextureRegion getFrame() {
        return springAnimation.getKeyFrame(stateTime, true);
    }

    public void drawSpring(SpriteBatch batch) {

        TextureRegion currentFrame = getFrame();

        float width = currentFrame.getRegionWidth() / CelesteGame.PPM;
        float height = currentFrame.getRegionHeight() / CelesteGame.PPM;

        float yPosOffset = 0.09f;

        batch.draw(
                currentFrame,
                body.getPosition().x - width / 2,
                body.getPosition().y+yPosOffset - height / 2,
                width, height
        );
    }

    @Override
    public void onFeetContact() {
        activateSpring();
    }

    @Override
    public void onFeetLeave() {

    }

    public void activateSpring() {

        isActive = true;
        activeDuration = 0.5f;
        activeTimer = 0f;


        float impulse = 2.5f;
        Player p = PlayScreen.player;

        if(p.isDashing){
            p.body.setLinearVelocity(new Vector2(-p.body.getLinearVelocity().x, -p.body.getLinearVelocity().y));
        }

        p.body.setLinearVelocity(new Vector2(0.0f, 0.0f));
        p.body.applyLinearImpulse(0.0f, impulse, p.getX(), p.getY(), true);

        p.canDash = true;
    }

}
