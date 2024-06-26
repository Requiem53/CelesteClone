package com.baringproductions.celeste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
    public Sprite hair_sprite;
    public static User user;
    public enum State { FALLING, JUMPING, STANDING, RUNNING};
    public State currentState;
    public State previousState;
    public World world;
    public Body body;

    //Player Movement Variables
    float runSpeed = 2f;
    float jumpHeight = 4.5f;

    private float originalXMaxSpeed = 2f / Constants.PPM;
    float xMaxSpeed = originalXMaxSpeed;

    float originalYMaxSpeed = 6f;
    float yMaxSpeed = originalYMaxSpeed;

    private final float dashStrength = 40f;

    float dashDuration = 0.15f;
    private float moveAfterDashDuration = 0f;

    private float cameraSpeed = 20f;

    private Fixture bottomFixture;
    private float originalFriction = 15f;

    //Jorash Variables
    public static int PLAYER_SPRITE_PIXELS = 25;
    private TextureRegion stand;
    private TextureRegion standHair;
    private Animation runAnim;
    private Animation runAnimHair;
    private Animation jumpAnim;
    private Animation jumpAnimHair;
    private Animation fallAnim;
    private Animation fallAnimHair;

    private float stateTimer;
    boolean facingRight;

    public Player(User user, World world) {
        super(new Texture("Player_Sprite/player_spritesheet_body.png"));
        hair_sprite = new Sprite(new Texture("Player_Sprite/player_spritesheet_hair.png"));
        Player.user = user;
        this.world = world;

        init();
    }

    private void init() {
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

        //Jorash Code
        fixtureDef.filter.categoryBits = CelesteGame.PLAYER_BIT;
        fixtureDef.filter.maskBits = CelesteGame.DEFAULT_BIT | CelesteGame.BERRY_BIT;
        fixtureDef2.filter.categoryBits = CelesteGame.PLAYER_BIT;
        fixtureDef2.filter.maskBits = CelesteGame.DEFAULT_BIT | CelesteGame.BERRY_BIT;

        //Connecting Fixtures to Body
        //Maurice ani pero di nako hilabtan naa niyay maguba -Slamm
        Fixture nigga = body.createFixture(fixtureDef);
        bottomFixture = body.createFixture(fixtureDef2);

        nigga.setUserData("playerBody");
        bottomFixture.setUserData("playerBody");

        //Change Camera Position to TrackedBody Para ma set asa
        //maka left right up down ang camera
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

        //Jorash Code
        //Animation chuchu ni jorash
        stand = new TextureRegion(getTexture(), 0, 0, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS);
        standHair = new TextureRegion(hair_sprite.getTexture(), 0, 0, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS);
        setBounds(0, 0, (PLAYER_SPRITE_PIXELS * 1.1f) / CelesteGame.PPM, (PLAYER_SPRITE_PIXELS * 1.1f) / CelesteGame.PPM);
        hair_sprite.setBounds(0, 0, (PLAYER_SPRITE_PIXELS * 1.1f) / CelesteGame.PPM, (PLAYER_SPRITE_PIXELS * 1.1f) / CelesteGame.PPM);
        setRegion(stand);
        hair_sprite.setRegion(standHair);

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0f;
        facingRight = true;

        //set running animation
        Array<TextureRegion> frames = new Array<TextureRegion>();
        Array<TextureRegion> framesHair = new Array<TextureRegion>();
        for(int i=3; i<9; i++){
            frames.add(new TextureRegion(getTexture(), i * PLAYER_SPRITE_PIXELS, 0, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS));
            framesHair.add(new TextureRegion(hair_sprite.getTexture(), i * PLAYER_SPRITE_PIXELS, 0, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS));
        }
        runAnim = new Animation(0.1f, frames);
        runAnimHair = new Animation(0.1f, framesHair);
        frames.clear();
        framesHair.clear();

        //set jumping animation
        for(int i=0; i<2; i++){
            frames.add(new TextureRegion(getTexture(), i * PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS));
            framesHair.add(new TextureRegion(hair_sprite.getTexture(), i * PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS));

        }
        jumpAnim = new Animation(0.1f, frames);
        jumpAnimHair = new Animation(0.1f, framesHair);

        frames.clear();
        framesHair.clear();

        //set falling animation
        for(int i=2; i<4; i++){
            frames.add(new TextureRegion(getTexture(), i * PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS));
            framesHair.add(new TextureRegion(hair_sprite.getTexture(), i * PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS, PLAYER_SPRITE_PIXELS));
        }
        fallAnim = new Animation(0.1f, frames);
        fallAnimHair = new Animation(0.1f, framesHair);
        frames.clear();
        framesHair.clear();

        applyHairTint(Color.WHITE.cpy());

        Thread inputThread = new Thread(new InputThread(this));
        inputThread.start();
    }

    public void update(float dt){
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 - 0.05f);
        hair_sprite.setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 - 0.05f);
        setSpriteRegions(dt);
    }

    private void setSpriteRegions(float dt){
        currentState = getState();

        TextureRegion region, regionHair;
        switch (currentState){
            case JUMPING:
                region = (TextureRegion) jumpAnim.getKeyFrame(stateTimer);
                regionHair = (TextureRegion) jumpAnimHair.getKeyFrame(stateTimer);
                break;
            case FALLING:
                region = (TextureRegion) fallAnim.getKeyFrame(stateTimer);
                regionHair = (TextureRegion) fallAnimHair.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) runAnim.getKeyFrame(stateTimer, true);
                regionHair = (TextureRegion) runAnimHair.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default:
                region = stand;
                regionHair = standHair;
                break;
        }
        if((facingRight && region.isFlipX())
                || (!facingRight && !region.isFlipX())){
            region.flip(true, false);
            regionHair.flip(true, false);
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        setRegion(region);
        hair_sprite.setRegion(regionHair);
    }
    private State getState(){
        if (body.getLinearVelocity().y > 0) return State.JUMPING;
        if (body.getLinearVelocity().y < 0) return State.FALLING;
        if (Math.abs(body.getLinearVelocity().x) >= 0.05f) return State.RUNNING;
        return State.STANDING;
    }

    //Player States
    public boolean onGround = false;
    public boolean canJump = false;
    public boolean canDash = false;
    public boolean isDashing = false;
    public boolean canMove = true;
    public boolean canLeft = true;
    public boolean canRight = true;
    public boolean isDead = false;
    public boolean onPlatform = false;

    //Camera States
    private float origXCamPosition;
    private float origYCamPosition;
    private boolean cameraHorizontal = false;
    private boolean toMoveCamera = false;

    public void landed(){
        canJump = true;
        canDash = true;
        onGround = true;
    }

    Stack<Integer> inputStack = new Stack<>();

    public void allProcesses(float dt) {
        cameraDebug();
        stateValidation();

        cameraHandling();
        dashingLogic();
        speedClamping();

        //DAPAT NI LAST AMBOT NGANO
        handleInput();
    }

    private void handleInput(){
        inputStackHandling();

        if(canMove && canLeft && (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))){
            if (inputStack.contains(4)){
                body.applyLinearImpulse(-(body.getMass()*runSpeed), 0f,
                        body.getPosition().x, body.getPosition().y, true);
            }
        }

        if(canMove && canRight && (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))){
            if (inputStack.contains(6)){
                body.applyLinearImpulse(body.getMass()*runSpeed, 0f,
                        body.getPosition().x, body.getPosition().y, true);
            }
        }

        if(canMove && canJump && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            body.applyLinearImpulse(0f , body.getMass()*jumpHeight ,
                    body.getPosition().x, body.getPosition().y,true);
            CelesteGame.manager.get("Audio/SoundEffects/jump.mp3", Sound.class).play(0.15f);
        }

        if(canMove && canDash && Gdx.input.isKeyJustPressed(Input.Keys.F)){
            CelesteGame.manager.get("Audio/SoundEffects/dash.wav", Sound.class).play(2f);
            body.setLinearVelocity(-body.getLinearVelocity().x + 0.04f,-body.getLinearVelocity().y + 0.04f);
            body.setAwake(false);
            isDashing = true;
            canMove = false;
            canDash = false;

            //change to determine dash speed
            xMaxSpeed += 20f;
            yMaxSpeed += 20f;

            //how long the dash is in delay seconds
            synchronized (dashingTask) {
                if (!dashingTask.isScheduled()) {
                    Timer.schedule(dashingTask, dashDuration);
                }
            }
        }
    }

    //Hashiri Dashing Task


    final Timer.Task dashingTask = new Timer.Task() {
        @Override
        public void run() {
            isDashing = false;
            xMaxSpeed = originalXMaxSpeed;
            yMaxSpeed = originalYMaxSpeed;
            synchronized (moveAfterDashTask) {
                if (!moveAfterDashTask.isScheduled()) {
                    if(!onGround){
//                        body.applyForce(-0.5f, -0.5f, body.getPosition().x, body.getPosition().y, true);
                        Timer.schedule(moveAfterDashTask, moveAfterDashDuration, 0.1f, 4);
                        Timer.schedule(dashCooldown, 0.25f);
                    }
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
            //??
            if(onGround) cancel();
            canMove = true;
        }
    };

    //Merge Conflict Things
    private final Timer.Task dashCooldown = new Timer.Task() {
        @Override
        public void run() {
            if(onGround) canDash = true;
        }
    };

    private void inputStackHandling(){
        //IMAGINE NUMPAD
        //8 - UP
        //4 - LEFT
        //6 - RIGHT
        //2 - DOWN
        //THANKS CHIAROSCURO

        //Babaw sa stack ang direction na i face
        if((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) &&
                Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            inputStack.remove((Integer) 6);
            inputStack.remove((Integer) 4);
        }else {
            if((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))){
                if(!inputStack.contains(4)){
                    inputStack.push(4);
                    facingRight = false;
                }
            }else{
                inputStack.remove((Integer) 4);
            }
            if((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))){
                if(!inputStack.contains(6)){
                    inputStack.push(6);
                    facingRight = true;
                }
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
    }

    private void speedClamping(){
        //Speed Clamping
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
    }

    private void dashingLogic(){
        if(isDashing){
            //0.04 pang account sa gamayng error
            //SOMEHOW NEED NI ANG SETLINEARVELECOITY UG LINEARIMPLUSE
            body.setLinearVelocity(-body.getLinearVelocity().x,-body.getLinearVelocity().y);
//            body.setLinearVelocity(0f,0f);
            if(inputStack.isEmpty()){
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
    }
    private void dash(boolean isHorizontal, boolean isRightOrUp){
        float xForce = body.getMass()*dashStrength / 3.5f;
        float yForce = body.getMass()*dashStrength / 5f;

        if(!isRightOrUp){
            xForce *= -1;
            yForce *= -1;
        }
        if(!isHorizontal){
            xForce *= 0;
        }else {
            yForce *= 0;
        }

//        body.applyLinearImpulse(xForce, yForce, body.getPosition().x,  body.getPosition().y, true);
        body.setLinearVelocity(xForce, yForce);
    }

    private void diagonalDash(boolean isRight, boolean isUp){
        float xForce = body.getMass()*dashStrength / 5f;
        float yForce = body.getMass()*dashStrength / 5f;

        if(!isRight) xForce *= -1;
        if(!isUp) yForce *= -1;

//        body.applyLinearImpulse(xForce, yForce, body.getPosition().x,  body.getPosition().y, true);
        body.setLinearVelocity(xForce, yForce);
    }

    private boolean camCurrentlyAdjusting = false;
    private Timer.Task camCooldown = new Timer.Task() {
        @Override
        public void run() {
            origXCamPosition = PlayScreen.trackedBody.getPosition().x;
            origYCamPosition = PlayScreen.trackedBody.getPosition().y;
            camCurrentlyAdjusting = false;
        }
    };

    private void cameraHandling(){
        //Detects when the camera should be moved then sets linear velocity of trackedbody
        //Happens when player goes outside of trackedbody area

        if(!camCurrentlyAdjusting){
            //HORIZONTAL
            if((body.getPosition().x < (origXCamPosition - (PlayScreen.trackedBodyWidth * 2f)) + 3.2636f)
                    || (body.getPosition().x > (origXCamPosition + (PlayScreen.trackedBodyWidth * 2f)) - 3.2636f)){

                    if(body.getPosition().x < (origXCamPosition - (PlayScreen.trackedBodyWidth * 2f)) + 3.2636f)
                        PlayScreen.trackedBody.setLinearVelocity(-cameraSpeed, 0f);
                    else
                        PlayScreen.trackedBody.setLinearVelocity(cameraSpeed, 0f);
                    cameraHorizontal = true;
                    toMoveCamera = true;
                    camCurrentlyAdjusting = true;
            }
            //VERTICAL
            if((body.getPosition().y < (origYCamPosition - (PlayScreen.trackedBodyHeight * 2.5f)) + 1.9016669f)
                    || (body.getPosition().y > (origYCamPosition + (PlayScreen.trackedBodyHeight * 2.5f)) - 1.9016669f)){

                if((body.getPosition().y < (origYCamPosition - (PlayScreen.trackedBodyHeight * 2.5f)) + 1.9016669f))
                    PlayScreen.trackedBody.setLinearVelocity(0f, -cameraSpeed);
                else{
                    PlayScreen.trackedBody.setLinearVelocity(0f, cameraSpeed);
                    body.applyLinearImpulse(0f, 10f, body.getPosition().x, body.getPosition().y, true);
                }

                cameraHorizontal = false;
                toMoveCamera = true;
                camCurrentlyAdjusting = true;
            }
        }

        //Tells Camera When To Stop
        if(toMoveCamera){
            if(cameraHorizontal){
                if((PlayScreen.trackedBody.getPosition().x < (origXCamPosition - (PlayScreen.trackedBodyWidth * 2f)))
                        || (PlayScreen.trackedBody.getPosition().x > (origXCamPosition + (PlayScreen.trackedBodyWidth * 2f)))){
                    PlayScreen.trackedBody.setLinearVelocity(0f, 0f);
                    toMoveCamera = false;
//                    origXCamPosition = PlayScreen.trackedBody.getPosition().x;
                    if(!camCooldown.isScheduled()) Timer.schedule(camCooldown, 0.50f);
                }
            }else{
                if((PlayScreen.trackedBody.getPosition().y < (origYCamPosition - (PlayScreen.trackedBodyHeight * 2.25f)))
                        || ((PlayScreen.trackedBody.getPosition().y > (origYCamPosition + (PlayScreen.trackedBodyHeight * 2.25f))))){
                    PlayScreen.trackedBody.setLinearVelocity(0f, 0f);
                    toMoveCamera = false;
//                    origYCamPosition = PlayScreen.trackedBody.getPosition().y;
                    if(!camCooldown.isScheduled()) Timer.schedule(camCooldown, 0.50f);
                }
            }

            PlayScreen.resetLevelInteractiveTiles();
        }

    }

    private void stateValidation(){
        if (onGround){
            canDash = true;
            canJump = true;
        }

        if(onPlatform){
            body.setAwake(true);
        }
    }

    private void cameraDebug(){
        //Minor Adjustment
        if(Gdx.input.isKeyJustPressed(Input.Keys.M)){
            PlayScreen.trackedBody.setTransform(PlayScreen.trackedBody.getPosition().x + 1f / Constants.PPM, PlayScreen.trackedBody.getPosition().y, 0);
            origXCamPosition = PlayScreen.trackedBody.getPosition().x;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.N)){
            PlayScreen.trackedBody.setTransform(PlayScreen.trackedBody.getPosition().x - 1f / Constants.PPM , PlayScreen.trackedBody.getPosition().y, 0);
            origXCamPosition = PlayScreen.trackedBody.getPosition().x;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.L)){
            onGround = true;
            canDash = true;
            canJump = true;
            canMove = true;
            canLeft = true;
            canRight = true;
        }

        //Horizontal
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

        //Vertical
//        if(Gdx.input.isKeyJustPressed(Input.Keys.J)){
//            origYCamPosition = PlayScreen.trackedBody.getPosition().y;
//            PlayScreen.trackedBody.setLinearVelocity(0f, cameraSpeed);
//            cameraHorizontal = false;
//            toMoveCamera = true;
//        }
//
//        if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
//            origYCamPosition = PlayScreen.trackedBody.getPosition().y;
//            PlayScreen.trackedBody.setLinearVelocity(0f, -cameraSpeed);
//            cameraHorizontal = false;
//            toMoveCamera = true;
//        }
    }

    public static void collectBerry(Vector2 vector) {
        user.addBerry(vector);
    }

    public void applyHairTint(Color color) {
        hair_sprite.setColor(color.r, color.g, color.b, color.a);
    }
}
