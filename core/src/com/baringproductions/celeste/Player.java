package com.baringproductions.celeste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.baringproductions.celeste.Screens.PlayScreen;
import com.baringproductions.celeste.Statics.Constants;

import java.util.Stack;

public class Player extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING};
    public State currentState;
    public State previousState;
    public World world;
    public Body body;

    float runSpeed = 2f;
    float jumpHeight = 4.5f;

    float originalXMaxSpeed = 2f / Constants.PPM;
    float xMaxSpeed = originalXMaxSpeed;

    float originalYMaxSpeed = 6f;
    float yMaxSpeed = originalYMaxSpeed;

    float dashStrength = 100f;

    float dashDuration = 0.15f;
    float moveAfterDashDuration = 0f;

    public float cameraSpeed = 20f;

    public static int PLAYER_SPRITE_PIXELS = 25;
    private TextureRegion stand;
    private Animation runAnim;
    private Animation jumpAnim;
    private Animation fallAnim;
    private float stateTimer;
    private boolean facingRight;

    public Fixture bottomFixture;
    public float originalFriction = 15f;

    public Player(World world) {
        super(new Texture("player_spritesheet.png"));
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
        fixtureDef.friction = 0f;

        //Second Part as Circle
        CircleShape circle = new CircleShape();
        circle.setRadius(4f/Constants.PPM);
        circle.setPosition(new Vector2(0, -(4f / Constants.PPM)));

        PolygonShape polygon2 = new PolygonShape();
        polygon2.setAsBox(4f / Constants.PPM,4f / Constants.PPM, new Vector2(0f, -(4f / Constants.PPM)), 0);
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = polygon2;
        fixtureDef2.density = 5f;
        fixtureDef2.friction = originalFriction;
//
        fixtureDef.filter.categoryBits = CelesteGame.PLAYER_BIT;
        fixtureDef.filter.maskBits = CelesteGame.DEFAULT_BIT;
        fixtureDef2.filter.categoryBits = CelesteGame.PLAYER_BIT;
        fixtureDef2.filter.maskBits = CelesteGame.DEFAULT_BIT;

        //Connecting Fixtures to Body
        Fixture nigga = body.createFixture(fixtureDef);
        bottomFixture = body.createFixture(fixtureDef2);

        nigga.setUserData("playerBody");
        bottomFixture.setUserData("playerBody");

        origXCamPosition = PlayScreen.trackedBody.getPosition().x;
        origYCamPosition = PlayScreen.trackedBody.getPosition().y;

        //Foot sensor
        polygon.setAsBox(0.1f, 0.05f, new Vector2(0, -0.32f), 0);
        fixtureDef.isSensor = true;
        Fixture footSensorFixture = body.createFixture(fixtureDef);
        footSensorFixture.setUserData("playerFoot");

        //Wall sensors
        polygon.setAsBox(2f/Constants.PPM, (8f/Constants.PPM) - 0.125f, new Vector2(4.2f / Constants.PPM, -4f / Constants.PPM + 0.025f), 0);
        fixtureDef.isSensor = true;
        Fixture wallSensorLeft = body.createFixture(fixtureDef);
        wallSensorLeft.setUserData("wallSensorRight");

        polygon.setAsBox(2f/Constants.PPM, (8f/Constants.PPM) - 0.125f, new Vector2(-4.2f / Constants.PPM, -4f / Constants.PPM + 0.025f), 0);
        fixtureDef.isSensor = true;
        Fixture wallSensorRight = body.createFixture(fixtureDef);
        wallSensorRight.setUserData("wallSensorLeft");

        polygon.dispose();
        circle.dispose();

        float width = 16f;
        float height = 16f;

        //Animation chuchu ni jorash
        stand = new TextureRegion(getTexture(), 0, 0, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS);
        float scaleX =  width / PLAYER_SPRITE_PIXELS;
        float scaleY = height / PLAYER_SPRITE_PIXELS;
        setBounds(0, 0, (PLAYER_SPRITE_PIXELS * 1.1f) / CelesteGame.PPM, (PLAYER_SPRITE_PIXELS * 1.1f) / CelesteGame.PPM);
        setRegion(stand);

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0f;
        facingRight = true;

        //set running animation
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=3; i<9; i++){
            frames.add(new TextureRegion(getTexture(), i * PLAYER_SPRITE_PIXELS, 0, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS));
        }
        runAnim = new Animation(0.1f, frames);
        frames.clear();

        //set jumping animation
        for(int i=0; i<2; i++){
            frames.add(new TextureRegion(getTexture(), i * PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS));
        }
        jumpAnim = new Animation(0.1f, frames);
        frames.clear();

        //set falling animation
        for(int i=2; i<4; i++){
            frames.add(new TextureRegion(getTexture(), i * PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS));
        }
        fallAnim = new Animation(0.1f, frames);
        frames.clear();

    }

    public void update(float dt){
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 - 0.05f);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = (TextureRegion) jumpAnim.getKeyFrame(stateTimer);
                break;
            case FALLING:
                region = (TextureRegion) fallAnim.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) runAnim.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default:
                region = stand;
                break;
        }

        if((body.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX()) {
            region.flip(true, false);
            facingRight = false;
        }else if ((body.getLinearVelocity().x > 0 || facingRight) && region.isFlipX()){
            region.flip(true, false);
            facingRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if (body.getLinearVelocity().y > 0) return State.JUMPING;
        if (body.getLinearVelocity().y < 0) return State.FALLING;
        if (Math.abs(body.getLinearVelocity().x) >= 0.20) return State.RUNNING;
        return State.STANDING;
    }

    public boolean onGround = false;
    public boolean canJump = false;
    public boolean canDash = false;
    private boolean isDashing = false;
    public boolean canMove = true;
    public boolean canLeft = true;
    public boolean canRight = true;
    public boolean isDead = false;
    public boolean onPlatform = false;

    public boolean toMoveCamera = false;
    public float origXCamPosition;
    public float origYCamPosition;
    public boolean cameraToLeftOrDown = false;
    public boolean cameraHorizontal = false;
    public boolean debugCam = false;
//
//    public void moveTargetObject(){
//        toMoveCamera = true;
//
//    }

    public void landed(){
        canJump = true;
        canDash = true;
        onGround = true;
    }

    private final Timer.Task dashingTask = new Timer.Task() {
        @Override
        public void run() {
            isDashing = false;
            xMaxSpeed = originalXMaxSpeed;
            yMaxSpeed = originalYMaxSpeed;
            synchronized (moveAfterDashTask) {
                if (!moveAfterDashTask.isScheduled()) {
                    if(!onGround){
                        Timer.schedule(moveAfterDashTask, moveAfterDashDuration, 0.1f, 4);
                    }
//                    else Timer.schedule(moveAfterDashTask, 0f);
                    else {
                        Timer.schedule(moveAfterDashTask, 0f);
                    }
                }
            }
        }
    };

    private final Timer.Task moveAfterDashTask = new Timer.Task() {
        @Override
        public void run() {
            if(onGround) cancel();
            canMove = true;
        }
    };

    private Stack<Integer> inputStack = new Stack<Integer>();

    public void handleInput(float dt) {
//        System.out.println(canJump);
//        System.out.println(bottomFixture.getFriction());
//        System.out.println(inputStack);
//        System.out.println(body.getMass());
//        System.out.println(body.getLinearVelocity());
//        System.out.println(onGround);
//        startTimer += Gdx.graphics.getDeltaTime();
        if (onGround){
            canDash = true;
            canJump = true;
        }
//        if(toMoveCamera){
//            System.out.println("CURRENT: " + PlayScreen.trackedBody.getPosition().x );
//            System.out.println("DESTINATION: " + ((origXCamPosition - PlayScreen.trackedBodyWidth)));
//            if(cameraToLeft){
//                if(PlayScreen.trackedBody.getPosition().x <= (origXCamPosition - (PlayScreen.trackedBodyWidth * 2f))){
//                    System.out.println("TO ZE LEFT");
//                    PlayScreen.trackedBody.setLinearVelocity(0f, 0f);
//                    toMoveCamera = false;
//                }
//            }else {
//                if(PlayScreen.trackedBody.getPosition().x >= (origXCamPosition + (PlayScreen.trackedBodyWidth * 2f))){
//                    System.out.println("TO ZE RIGHT");
//                    PlayScreen.trackedBody.setLinearVelocity(0f, 0f);
//                    toMoveCamera = false;
//                }
//            }
//        }

        //FOR DEBUGGING
//        if(Gdx.input.isKeyJustPressed(Input.Keys.J)){
//            origXCamPosition = PlayScreen.trackedBody.getPosition().x;
//            PlayScreen.trackedBody.setLinearVelocity(-10f, 0f);
//            debugCam = true;
//            cameraToLeftOrDown = true;
//        }
//
//        if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
//            origXCamPosition = PlayScreen.trackedBody.getPosition().x;
//            PlayScreen.trackedBody.setLinearVelocity(10f, 0f);
//            debugCam = true;
//            cameraToLeftOrDown = false;
//        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.J)){
            origYCamPosition = PlayScreen.trackedBody.getPosition().y;
            PlayScreen.trackedBody.setLinearVelocity(0f, cameraSpeed);
            cameraHorizontal = false;
            cameraToLeftOrDown = false;
            debugCam = true;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
            origYCamPosition = PlayScreen.trackedBody.getPosition().y;
            PlayScreen.trackedBody.setLinearVelocity(0f, -cameraSpeed);
            cameraHorizontal = false;
            cameraToLeftOrDown = true;
            debugCam = true;
        }

        if(debugCam){
//            System.out.println("CURRENT: " + PlayScreen.trackedBody.getPosition().x );
//            System.out.println("DESTINATION: " + ((origXCamPosition - PlayScreen.trackedBodyWidth)));
            if(cameraHorizontal){
                if(cameraToLeftOrDown){
                    if(PlayScreen.trackedBody.getPosition().x <= (origXCamPosition - (PlayScreen.trackedBodyWidth * 2f))){
//                        System.out.println("TO ZE LEFT");
                        PlayScreen.trackedBody.setLinearVelocity(0f, 0f);
                        debugCam = false;
                        origXCamPosition = PlayScreen.trackedBody.getPosition().x;
                    }
                }else {
                    if(PlayScreen.trackedBody.getPosition().x >= (origXCamPosition + (PlayScreen.trackedBodyWidth * 2f))){
//                        System.out.println("TO ZE RIGHT");
                        PlayScreen.trackedBody.setLinearVelocity(0f, 0f);
                        debugCam = false;
                        origXCamPosition = PlayScreen.trackedBody.getPosition().x;
                    }
                }
            }else{
                if(cameraToLeftOrDown){
                    if(PlayScreen.trackedBody.getPosition().y <= (origYCamPosition - (PlayScreen.trackedBodyHeight * 2.25f))){
                        PlayScreen.trackedBody.setLinearVelocity(0f, 0f);
                        debugCam = false;
                        origYCamPosition = PlayScreen.trackedBody.getPosition().y;
                    }
                }else {
                    if(PlayScreen.trackedBody.getPosition().y >= (origYCamPosition + (PlayScreen.trackedBodyHeight * 2.25f))){
                        PlayScreen.trackedBody.setLinearVelocity(0f, 0f);
                        debugCam = false;
                        origYCamPosition = PlayScreen.trackedBody.getPosition().y;
                    }
                }
            }

        }

//        System.out.println("CURRENT: " + body.getPosition().x);
//        System.out.println("DESTINATION: " + ((origXCamPosition + (PlayScreen.trackedBodyWidth * 2f)) - 3.2636f));

//        System.out.println("CURRENT: " + body.getPosition().x);
//        System.out.println("DESTINATION: " + ((origXCamPosition + (PlayScreen.trackedBodyWidth * 2f))));

//        System.out.println("CURRENT: " + body.getPosition().y);
//        System.out.println("DESTINATION: " + ((origYCamPosition + (PlayScreen.trackedBodyHeight * 2.25f))));


        //CAMERA MOVEMENT
        //HORIZONTAL
        if(body.getPosition().x <= (origXCamPosition - (PlayScreen.trackedBodyWidth * 2f)) + 3.2636f){
            PlayScreen.trackedBody.setLinearVelocity(-cameraSpeed, 0f);
            cameraHorizontal = true;
            cameraToLeftOrDown = true;
            debugCam = true;
        }else if(body.getPosition().x >= (origXCamPosition + (PlayScreen.trackedBodyWidth * 2f)) - 3.2636f){
//            System.out.println("PLAYER: " + body.getPosition().x);
//            System.out.println("SENSOR: " + (origXCamPosition + (PlayScreen.trackedBodyWidth * 2f)));
            PlayScreen.trackedBody.setLinearVelocity(cameraSpeed, 0f);
            cameraHorizontal = true;
            cameraToLeftOrDown = false;
            debugCam = true;
        }

        //VERTICAL
        if(body.getPosition().y <= (origYCamPosition - (PlayScreen.trackedBodyHeight * 2.5f)) + 1.9016669f){
            PlayScreen.trackedBody.setLinearVelocity(0f, -cameraSpeed);
            cameraHorizontal = false;
            cameraToLeftOrDown = true;
            debugCam = true;
        }else if(body.getPosition().y >= (origYCamPosition + (PlayScreen.trackedBodyHeight * 2.5f)) - 1.9016669f){
//            System.out.println("PLAYER: " + body.getPosition().y);
//            System.out.println("SENSOR: " + (origYCamPosition + (PlayScreen.trackedBodyHeight * 2f)));
            PlayScreen.trackedBody.setLinearVelocity(0f, cameraSpeed);
            cameraHorizontal = false;
            cameraToLeftOrDown = false;
            debugCam = true;
        }

        if(onPlatform){
            body.setAwake(true);
        }

        if(isDashing){
            //0.04 pang account sa gamayng error

            //SOMEHOW NEED NI ANG SETLINEARVELECOITY UG LINEARIMPLUSE
            body.setLinearVelocity(-body.getLinearVelocity().x,-body.getLinearVelocity().y + 0.04f);
            if(inputStack.isEmpty()){
                //SHOULD BE BASED ON DIRECTION FACING
                dash(true, facingRight);
            } else if (inputStack.size() == 1) {
                boolean isHorizontal = true;
                boolean isRightOrUp = true;

                if(inputStack.peek() == 2 || inputStack.peek() == 8) isHorizontal = false;
                if(inputStack.peek() == 4 || inputStack.peek() == 2) isRightOrUp = false;

                dash(isHorizontal, isRightOrUp);
            }else {
                boolean isRight = true;
                boolean isUp = true;

                if(inputStack.contains(2)) isUp = false;
                if(inputStack.contains(4)) isRight = false;

                diagonalDash(isRight, isUp);
            }
        }

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



        if(canMove && canLeft && (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))){
            if (inputStack.contains(4)){
//                body.setLinearVelocity(-runSpeed, body.getLinearVelocity().y);
                body.applyLinearImpulse(-(body.getMass()*runSpeed), 0f,
                body.getPosition().x, body.getPosition().y, true);
            }
        }



        if(canMove && canRight && (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))){
            if (inputStack.contains(6)){
//                body.setLinearVelocity(runSpeed, body.getLinearVelocity().y);
                body.applyLinearImpulse(body.getMass()*runSpeed, 0f,
                    body.getPosition().x, body.getPosition().y, true);
            }
        }

        if(canMove && canJump && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            body.applyLinearImpulse(0f , body.getMass()*jumpHeight ,
                    body.getPosition().x, body.getPosition().y,true);
        }

        if(canMove && canDash && Gdx.input.isKeyJustPressed(Input.Keys.F)){
//            body.applyLinearImpulse(body.getMass()*runSpeed, 0f,
//                    body.getPosition().x, body.getPosition().y, true);
            body.setLinearVelocity(-body.getLinearVelocity().x + 0.04f,-body.getLinearVelocity().y + 0.04f);
            body.setAwake(false);
            isDashing = true;
            canMove = false;
            canDash = false;

            //change to determine dash speed
            xMaxSpeed = xMaxSpeed + 7f;
            yMaxSpeed -= 1.5f;

            //how long the dash is in delay seconds
            synchronized (dashingTask) {
                if (!dashingTask.isScheduled()) {
                    Timer.schedule(dashingTask, dashDuration);
                }
            }
        }
    }

    public void diagonalDash(boolean isRight, boolean isUp){
        float xForce = body.getMass()*dashStrength / 12f;
        float yForce = body.getMass()*dashStrength / 24f;

        if(!isRight) xForce *= -1;
        if(!isUp) yForce *= -1;

        body.applyLinearImpulse(xForce, yForce, body.getPosition().x,  body.getPosition().y, true);
    }

    public void dash(boolean isHorizontal, boolean isRightOrUp){
        float xForce = body.getMass()*dashStrength;
        float yForce = body.getMass()*dashStrength;

        if(!isRightOrUp){
            xForce *= -1;
            yForce *= -1;
        }
        if(!isHorizontal){
            xForce *= 0;
        }else {
            yForce *= 0;
        }

        body.applyLinearImpulse(xForce, yForce, body.getPosition().x,  body.getPosition().y, true);
    }
}
