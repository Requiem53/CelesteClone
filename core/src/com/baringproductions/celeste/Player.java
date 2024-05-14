package com.baringproductions.celeste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.baringproductions.celeste.Statics.Constants;

import java.util.Stack;

public class Player extends Sprite {
    public World world;
    public Body body;

    public Boolean canJump;

    float runSpeed = 4f;
    float jumpHeight = 4.5f;

    float originalXMaxSpeed = 2f;
    float xMaxSpeed = originalXMaxSpeed;

    float originalYMaxSpeed = 6f;
    float yMaxSpeed = originalYMaxSpeed;

    float dashStrength = 100f;

    float dashDuration = 0.15f;
    float moveAfterDashDuration = 0.2f;

    public Player(World world) {
        this.world = world;

        init();
    }

    public void init() {
        canJump = false;

        //Body Def and Position
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(32/CelesteGame.PPM, 32/CelesteGame.PPM);

        //Body Creation
        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        //First Part Of Fixture as Box
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(4f / Constants.PPM,4f / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 1f;

        //Second Part as Circle
        CircleShape circle = new CircleShape();
        circle.setRadius(4f/Constants.PPM);
        circle.setPosition(new Vector2(0, -0.2f));

        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = circle;
        fixtureDef2.density = 1f;
        fixtureDef2.friction = 5f;

        //Connecting Fixtures to Body
        body.createFixture(fixtureDef);
        body.createFixture(fixtureDef2);

        //Foot sensor
        polygon.setAsBox(0.1f, 0.05f, new Vector2(0, -0.32f), 0);
        fixtureDef.isSensor = true;
        Fixture footSensorFixture = body.createFixture(fixtureDef);
        footSensorFixture.setUserData("playerFoot");

        polygon.dispose();
        circle.dispose();

        //        BodyDef bdef = new BodyDef();
//
//        bdef.position.set(32/CelesteGame.PPM, 32/CelesteGame.PPM);
//        bdef.type = BodyDef.BodyType.DynamicBody;
//
//        body = world.createBody(bdef);
//
//        FixtureDef fdef = new FixtureDef();
//        CircleShape shape = new CircleShape();
//        shape.setRadius(5/CelesteGame.PPM);
//
//        fdef.shape = shape;
//        body.createFixture(fdef);
    }

    private boolean isDashing = false;
    public boolean canDash = false;
    private boolean canMove = true;
    public boolean onGround = false;

    private final Timer.Task dashingTask = new Timer.Task() {
        @Override
        public void run() {
            isDashing = false;
            xMaxSpeed = originalXMaxSpeed;
            yMaxSpeed = originalYMaxSpeed;
            System.out.println("Done Dashing");
            synchronized (moveAfterDashTask) {
                if (!moveAfterDashTask.isScheduled()) {
                    if(!onGround) Timer.schedule(moveAfterDashTask, moveAfterDashDuration, 0.1f, 4);
                    else Timer.schedule(moveAfterDashTask, 0f);
                }
            }
        }
    };

    private final Timer.Task moveAfterDashTask = new Timer.Task() {
        @Override
        public void run() {
            if(onGround) cancel();
            canMove = true;
            System.out.println("Can Move After Dashing");

        }
    };

    private Stack<Integer> inputStack = new Stack<Integer>();

    public void handleInput(float dt) {
//        System.out.println(inputStack);
//        System.out.println(body.getMass());
//        System.out.println(body.getLinearVelocity());
//        System.out.println(onGround);
//        startTimer += Gdx.graphics.getDeltaTime();
        if (onGround){
            canDash = true;
            canJump = true;
        }

        if(isDashing){
//            body.setLinearVelocity(40f,-body.getLinearVelocity().y);

            //0.04 pang account sa gamayng error

            //SOMEHOW NEED NI ANG SETLINEARVELECOITY UG LINEARIMPLUSE
            if(inputStack.isEmpty()){
                //SHOULD BE BASED ON DIRECTION FACING
                body.setLinearVelocity(body.getLinearVelocity().x,-body.getLinearVelocity().y + 0.04f);
                body.applyLinearImpulse(body.getMass()*dashStrength,  0f,
                        body.getPosition().x, body.getPosition().y,true);
            } else if (inputStack.contains(6)) {
                if (inputStack.contains(8)){
                    body.setLinearVelocity(-body.getLinearVelocity().x + 0.04f, -body.getLinearVelocity().y + 0.04f);
                    body.applyLinearImpulse(body.getMass()*dashStrength/4,  body.getMass()*dashStrength/8,
                            body.getPosition().x, body.getPosition().y,true);
                } else if (inputStack.contains(2)) {
                    body.setLinearVelocity(-body.getLinearVelocity().x + 0.04f, -body.getLinearVelocity().y + 0.04f);
                    body.applyLinearImpulse(body.getMass()*dashStrength/4,  -(body.getMass()*dashStrength/8),
                            body.getPosition().x, body.getPosition().y,true);
                }else if (!inputStack.contains(4)){
                    body.setLinearVelocity(body.getLinearVelocity().x,-body.getLinearVelocity().y + 0.04f);
                    body.applyLinearImpulse(body.getMass()*dashStrength,  0f,
                            body.getPosition().x, body.getPosition().y,true);
                }
            } else if (inputStack.contains(4)) {
                if (inputStack.contains(8)){
                    body.setLinearVelocity(-body.getLinearVelocity().x + 0.04f, -body.getLinearVelocity().y + 0.04f);
                    body.applyLinearImpulse(-(body.getMass()*dashStrength/4),  body.getMass()*dashStrength/8,
                            body.getPosition().x, body.getPosition().y,true);
                } else if (inputStack.contains(2)) {
                    body.setLinearVelocity(-body.getLinearVelocity().x + 0.04f, -body.getLinearVelocity().y + 0.04f);
                    body.applyLinearImpulse(-(body.getMass()*dashStrength/4),  -(body.getMass()*dashStrength/8),
                            body.getPosition().x, body.getPosition().y,true);
                }else if (!inputStack.contains(6)){
                    body.setLinearVelocity(body.getLinearVelocity().x,-body.getLinearVelocity().y + 0.04f);
                    body.applyLinearImpulse(-(body.getMass()*dashStrength),  0f,
                            body.getPosition().x, body.getPosition().y,true);
                }
            } else if (inputStack.contains(2)) {
                if (inputStack.contains(4)){
                    body.setLinearVelocity(-body.getLinearVelocity().x + 0.04f, -body.getLinearVelocity().y + 0.04f);
                    body.applyLinearImpulse(-(body.getMass()*dashStrength/4),  -(body.getMass()*dashStrength/8),
                            body.getPosition().x, body.getPosition().y,true);
                } else if (inputStack.contains(6)) {
                    body.setLinearVelocity(-body.getLinearVelocity().x + 0.04f, -body.getLinearVelocity().y + 0.04f);
                    body.applyLinearImpulse(body.getMass()*dashStrength/4,  -(body.getMass()*dashStrength/8),
                            body.getPosition().x, body.getPosition().y,true);
                }else if (!inputStack.contains(8)){
                    body.setLinearVelocity(body.getLinearVelocity().x,-body.getLinearVelocity().y + 0.04f);
                    body.applyLinearImpulse(0f,  -(body.getMass()*dashStrength),
                            body.getPosition().x, body.getPosition().y,true);
                }
            } else if (inputStack.contains(8)) {
                if (inputStack.contains(4)){
                    body.setLinearVelocity(-body.getLinearVelocity().x + 0.04f, -body.getLinearVelocity().y + 0.04f);
                    body.applyLinearImpulse(-(body.getMass()*dashStrength/4), body.getMass()*dashStrength/8,
                            body.getPosition().x, body.getPosition().y,true);
                } else if (inputStack.contains(6)) {
                    body.setLinearVelocity(-body.getLinearVelocity().x + 0.04f, -body.getLinearVelocity().y + 0.04f);
                    body.applyLinearImpulse(body.getMass()*dashStrength/4,  body.getMass()*dashStrength/8,
                            body.getPosition().x, body.getPosition().y,true);
                }else if (!inputStack.contains(2)){
                    body.setLinearVelocity(body.getLinearVelocity().x,-body.getLinearVelocity().y + 0.04f);
                    body.applyLinearImpulse(0f,  body.getMass()*dashStrength,
                            body.getPosition().x, body.getPosition().y,true);
                }
            }


//            body.applyLinearImpulse(2f, 0f, body.getPosition().x, body.getPosition().y, true);
//            body.applyForceToCenter(100f, 0f, true);
        }
//
//        if(startTimer > dashPeriod){
//            isDashing = false;
//            otherTimer += Gdx.graphics.getDeltaTime();
//            if(otherTimer > afterDashPeriod){
//                canMove = true;
//                startTimer = 0f;
//                otherTimer = 0f;
//            }
//        }

        //Naa ra man guro ni code na ma clamp
        if(Math.abs(body.getLinearVelocity().x) > xMaxSpeed){
            if(body.getLinearVelocity().x < 0){
                body.setLinearVelocity(-xMaxSpeed, body.getLinearVelocity().y);
            }else{
                body.setLinearVelocity(xMaxSpeed, body.getLinearVelocity().y);
            }
        }

        if(Math.abs(body.getLinearVelocity().y) > yMaxSpeed){
            if(body.getLinearVelocity().y < 0){
                body.setLinearVelocity(body.getLinearVelocity().x, -yMaxSpeed);
            }else{
                body.setLinearVelocity(body.getLinearVelocity().x, yMaxSpeed);
            }
        }

        //Pwede ra hash hash key value ang kaning stack
        //Pero fail fast sa ko

        //IMAGINE NUMPAD
        //8 - UP
        //4 - LEFT
        //6 - RIGHT
        //2 - DOWN
        //THANKS CHIAROSCURO
        if((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) &&
            Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            inputStack.remove((Integer) 6);
            inputStack.remove((Integer) 4);
        }else {
            if((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))){
                if(!inputStack.contains(4)) inputStack.push(4);
            }else{
                inputStack.remove((Integer) 4);
            }
            if((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))){
                if(!inputStack.contains(6)) inputStack.push(6);
            }else{
                inputStack.remove((Integer) 6);
            }
        }

        if((Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) &&
                Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            inputStack.remove((Integer) 8);
            inputStack.remove((Integer) 2);
        }else {
            if((Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))){
                if(!inputStack.contains(8)) inputStack.push(8);
            }else{
                inputStack.remove((Integer) 8);
            }
            if((Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))){
                if(!inputStack.contains(2)) inputStack.push(2);
            }else{
                inputStack.remove((Integer) 2);
            }
        }



        if(canMove && (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))){
            if (inputStack.contains(4)){
//                body.setLinearVelocity(-runSpeed, body.getLinearVelocity().y);
                body.applyLinearImpulse(-(body.getMass()*runSpeed), 0f,
                body.getPosition().x, body.getPosition().y, true);
            }
        }



        if(canMove && (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))){
            if (inputStack.contains(6)){
//                body.setLinearVelocity(runSpeed, body.getLinearVelocity().y);
                body.applyLinearImpulse(body.getMass()*runSpeed, 0f,
                    body.getPosition().x, body.getPosition().y, true);
            }
        }

        if(canMove && Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && canJump){
            body.applyLinearImpulse(0f , body.getMass()*jumpHeight ,
                    body.getPosition().x, body.getPosition().y,true);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F) && canMove && canDash){
//            body.applyLinearImpulse(body.getMass()*runSpeed, 0f,
//                    body.getPosition().x, body.getPosition().y, true);
            body.setLinearVelocity(-body.getLinearVelocity().x + 0.04f,-body.getLinearVelocity().y + 0.04f);
            body.setAwake(false);
            isDashing = true;
            canMove = false;
            canDash = false;

            //change to determine dash speed
            xMaxSpeed += 10f;
            yMaxSpeed -= 1.5f;

            //how long the dash is in delay seconds
            synchronized (dashingTask) {
                if (!dashingTask.isScheduled()) {
                    Timer.schedule(dashingTask, dashDuration);
                }
            }
        }

//        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
//            body.applyLinearImpulse(new Vector2(0, 2), body.getWorldCenter(), true);
//
//        if (Gdx.input.isKeyPressed(Input.Keys.D) && body.getLinearVelocity().x <= 2)
//            body.applyLinearImpulse(new Vector2(0.1f, 0), body.getWorldCenter(), true);
//        if (Gdx.input.isKeyPressed(Input.Keys.A) && body.getLinearVelocity().x >= -2)
//            body.applyLinearImpulse(new Vector2(-0.1f, 0), body.getWorldCenter(), true);
    }
}
