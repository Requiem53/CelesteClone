package com.baringproductions.celeste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends Sprite {
    public World world;
    public Body body;

    public Player(World world) {
        this.world = world;

        init();
    }

    public void init() {
        BodyDef bdef = new BodyDef();

        bdef.position.set(32/CelesteGame.PPM, 32/CelesteGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5/CelesteGame.PPM);

        fdef.shape = shape;
        body.createFixture(fdef);
    }

    public void handleInput(float dt) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            body.applyLinearImpulse(new Vector2(0, 2), body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.D) && body.getLinearVelocity().x <= 2)
            body.applyLinearImpulse(new Vector2(0.1f, 0), body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.A) && body.getLinearVelocity().x >= -2)
            body.applyLinearImpulse(new Vector2(-0.1f, 0), body.getWorldCenter(), true);
    }
}
