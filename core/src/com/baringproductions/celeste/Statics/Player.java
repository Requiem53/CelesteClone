package com.baringproductions.celeste.Statics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

import static com.baringproductions.celeste.Statics.Constants.PPMScaled;

public class Player {

    public Body body;

    public Player(World world){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(PPMScaled((((float) Gdx.graphics.getDisplayMode().width / 2) - (float) 64 /2)), 10);

        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        CircleShape circle = new CircleShape();
        circle.setRadius(PPMScaled(16F));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;

        Fixture fixture = body.createFixture(fixtureDef);

        circle.dispose();
    }
}
