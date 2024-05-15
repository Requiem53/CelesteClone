package com.baringproductions.celeste.Statics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.baringproductions.celeste.Statics.Constants.PPMScaled;

public class Player {

    public Body body;
    public Boolean canJump;
    public boolean canMove;

    float runSpeed = 6f;
    float jumpHeight = 10f;

    public Player(World world){
        canJump = false;

        //Body Def and Position
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(PPMScaled((((float) Gdx.graphics.getDisplayMode().width / 2) - (float) 64 /2)), 10);

        //Body Creation
        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        //First Part Of Fixture as Box
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(16f / Constants.PPM,16f / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 1f;

        //Second Part as Circle
        CircleShape circle = new CircleShape();
        circle.setRadius(16f/Constants.PPM);
        circle.setPosition(new Vector2(0, -1f));

        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = circle;
        fixtureDef2.density = 1f;

        //Connecting Fixtures to Body
        body.createFixture(fixtureDef);
        body.createFixture(fixtureDef2);

        //Foot sensor
        polygon.setAsBox(0.3f, 0.3f, new Vector2(0, -1.4f), 0);
        fixtureDef.isSensor = true;
        Fixture footSensorFixture = body.createFixture(fixtureDef);
        footSensorFixture.setUserData("playerFootSensor");

        polygon.dispose();
        circle.dispose();
    }

    public void processInputs(){
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
            body.applyLinearImpulse(-(body.getMass()*runSpeed), 0f,
                    body.getPosition().x, body.getPosition().y, true);

        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            body.applyLinearImpulse(body.getMass()*runSpeed, 0f,
                    body.getPosition().x, body.getPosition().y, true);

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && canJump){
            body.applyLinearImpulse(0f , body.getMass()*jumpHeight ,
                    body.getPosition().x, body.getPosition().y,true);
        }
    }
}
