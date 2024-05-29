package com.baringproductions.celeste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

public class InputThread implements Runnable {

    Player player;

    public InputThread(Player player){
        this.player = player;
    }

    @Override
    public void run() {
        handleInput();
    }

    public void inputStackHandling(){
        //IMAGINE NUMPAD
        //8 - UP
        //4 - LEFT
        //6 - RIGHT
        //2 - DOWN
        //THANKS CHIAROSCURO

        //Babaw sa stack ang direction na i face
        if((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) &&
                Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.inputStack.remove((Integer) 6);
            player.inputStack.remove((Integer) 4);
        }else {
            if((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))){
                if(!player.inputStack.contains(4)){
                    player.inputStack.push(4);
                    player.facingRight = false;
                }
            }else{
                player.inputStack.remove((Integer) 4);
            }
            if((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))){
                if(!player.inputStack.contains(6)){
                    player.inputStack.push(6);
                    player.facingRight = true;
                }
            }else{
                player.inputStack.remove((Integer) 6);
            }
        }

        if((Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) &&
                Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.inputStack.remove((Integer) 8);
            player.inputStack.remove((Integer) 2);
        }else {
            if((Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))){
                if(!player.inputStack.contains(8)) player.inputStack.push(8);
            }else{
                player.inputStack.remove((Integer) 8);
            }
            if((Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))){
                if(!player.inputStack.contains(2)) player.inputStack.push(2);
            }else{
                player.inputStack.remove((Integer) 2);
            }
        }
    }

    public void handleInput(){
        inputStackHandling();

        if(player.canMove && player.canLeft && (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))){
            if (player.inputStack.contains(4)){
                player.body.applyLinearImpulse(-(player.body.getMass()*player.runSpeed), 0f,
                        player.body.getPosition().x, player.body.getPosition().y, true);
            }
        }

        if(player.canMove && player.canRight && (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))){
            if (player.inputStack.contains(6)){
                player.body.applyLinearImpulse(player.body.getMass()*player.runSpeed, 0f,
                        player.body.getPosition().x, player.body.getPosition().y, true);
            }
        }

        if(player.canMove && player.canJump && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            player.body.applyLinearImpulse(0f , player.body.getMass()*player.jumpHeight ,
                    player.body.getPosition().x, player.body.getPosition().y,true);
            CelesteGame.manager.get("Audio/SoundEffects/jump.mp3", Sound.class).play(0.15f);
        }

        if(player.canMove && player.canDash && Gdx.input.isKeyJustPressed(Input.Keys.F)){
            CelesteGame.manager.get("Audio/SoundEffects/dash.wav", Sound.class).play(2f);
            player.body.setLinearVelocity(-player.body.getLinearVelocity().x + 0.04f,-player.body.getLinearVelocity().y + 0.04f);
            player.body.setAwake(false);
            player.isDashing = true;
            player.canMove = false;
            player.canDash = false;

            //change to determine dash speed
            player.xMaxSpeed += 20f;
            player.yMaxSpeed += 20f;

            //how long the dash is in delay seconds
            synchronized (player.dashingTask) {
                if (!player.dashingTask.isScheduled()) {
                    Timer.schedule(player.dashingTask, player.dashDuration);
                }
            }
        }
    }
}
