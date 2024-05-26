package com.baringproductions.celeste.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Player;
import com.baringproductions.celeste.Screens.PlayScreen;
import com.baringproductions.celeste.Tiles.*;
import com.sun.jndi.ldap.Ber;

public class WorldListener implements ContactListener {

    Player player;

    public WorldListener(Player player){
        this.player = player;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if ((fixtureA.getUserData() == "playerFoot") ||
                (fixtureB.getUserData() == "playerFoot")) {

            Fixture feet = fixtureA.getUserData() == "playerFoot" ? fixtureA : fixtureB;
            Fixture ground = feet == fixtureA ? fixtureB : fixtureA;

            if (ground.getUserData() != null &&
                    InteractiveTile.class.isAssignableFrom(ground.getUserData().getClass())) {
                ((InteractiveTile) ground.getUserData()).onFeetContact();
//                System.out.println("foot contact");
//                System.out.println(ground.getUserData().getClass());

            }
        }

        if (
                ((fixtureA.getUserData() == "wallSensorLeft") || (fixtureA.getUserData() == "wallSensorRight")) ||
                ((fixtureB.getUserData() == "wallSensorLeft") || (fixtureB.getUserData() == "wallSensorRight"))
        ) {

            Fixture sensor, tile;

            if (
                    ((fixtureA.getUserData() == "wallSensorLeft") || (fixtureA.getUserData() == "wallSensorRight"))
                ) {
                sensor = fixtureA;
                tile = fixtureB;
            } else {
                sensor = fixtureB;
                tile = fixtureA;
            }



            if (tile.getUserData() instanceof Spring) {
                ((Spring) tile.getUserData()).activateSpring();
            }
        }

        //AYAW HILABTI
        if ((fixtureA.getUserData() == "wallSensorLeft") ||
                (fixtureB.getUserData() == "wallSensorLeft")) {

            Fixture feet = fixtureA.getUserData() == "wallSensorLeft" ? fixtureA : fixtureB;
            Fixture wall = feet == fixtureA ? fixtureB : fixtureA;

            if (wall.getUserData() == "Wall") {

                player.canLeft = false;
            }
        }

        if ((fixtureA.getUserData() == "wallSensorRight") ||
                (fixtureB.getUserData() == "wallSensorRight")) {

            Fixture feet = fixtureA.getUserData() == "wallSensorRight" ? fixtureA : fixtureB;
            Fixture wall = feet == fixtureA ? fixtureB : fixtureA;

            if (wall.getUserData() == "Wall") {

                player.canRight = false;
            }
        }

        if ((fixtureA.getUserData() == "playerBody") ||
                (fixtureB.getUserData() == "playerBody")) {

            Fixture body = fixtureA.getUserData() == "playerBody" ? fixtureA : fixtureB;
            Fixture fixture = body == fixtureA ? fixtureB : fixtureA;

            if (fixture.getUserData() != null &&
                    InteractiveTile.class.isAssignableFrom(fixture.getUserData().getClass())) {

                if(fixture.getUserData() instanceof DashGem){
                    ((InteractiveTile) fixture.getUserData()).onFeetContact();
                }
                else if(fixture.getUserData() instanceof Berry){
                    ((Berry) fixture.getUserData()).onBodyContact();
                }else if(fixture.getUserData() instanceof Spike){
                    ((Spike) fixture.getUserData()).onBodyContact();
                }

            }

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if ((fixtureA.getUserData() == "playerFoot") ||
                (fixtureB.getUserData() == "playerFoot")) {

            Fixture feet = fixtureA.getUserData() == "playerFoot" ? fixtureA : fixtureB;
            Fixture ground = feet == fixtureA ? fixtureB : fixtureA;

            if (ground.getUserData() != null &&
                    InteractiveTile.class.isAssignableFrom(ground.getUserData().getClass())) {

                ((InteractiveTile) ground.getUserData()).onFeetLeave();

//                ((InteractiveTile) ground.getUserData()).onFeetContact();
//                System.out.println("foot left");
//                System.out.println(ground.getUserData().getClass());

                player.canJump = false;
                player.onGround = false;
            }
        }

        if ((fixtureA.getUserData() == "wallSensorLeft") ||
                (fixtureB.getUserData() == "wallSensorLeft")) {

            Fixture feet = fixtureA.getUserData() == "wallSensorLeft" ? fixtureA : fixtureB;
            Fixture wall = feet == fixtureA ? fixtureB : fixtureA;

            if (wall.getUserData() == "Wall") {
                player.canLeft = true;
            }
        }

        if ((fixtureA.getUserData() == "wallSensorRight") ||
                (fixtureB.getUserData() == "wallSensorRight")) {

            Fixture feet = fixtureA.getUserData() == "wallSensorRight" ? fixtureA : fixtureB;
            Fixture wall = feet == fixtureA ? fixtureB : fixtureA;

            if (wall.getUserData() == "Wall") {

                player.canRight = true;
            }
        }

        if ((fixtureA.getUserData() == "trackedBody") ||
                (fixtureB.getUserData() == "trackedBody")) {

            Fixture feet = fixtureA.getUserData() == "trackedBody" ? fixtureA : fixtureB;
            Fixture wall = feet == fixtureA ? fixtureB : fixtureA;

//            if (wall.getUserData() == "wallSensorRight") {
//                if(!player.toMoveCamera){
//                    player.origXCamPosition = PlayScreen.trackedBody.getPosition().x;
//                    PlayScreen.trackedBody.setLinearVelocity(10f, 0f);
//                    player.cameraToLeft = false;
//                    player.toMoveCamera = true;
//                }
//            }
//            else if (wall.getUserData() == "wallSensorLeft") {
//                if(!player.toMoveCamera){
//                    player.origXCamPosition = PlayScreen.trackedBody.getPosition().x;
//                    PlayScreen.trackedBody.setLinearVelocity(-10f, 0f);
//                    player.cameraToLeft = true;
//                    player.toMoveCamera = true;
//                }
//            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}

//                Gdx.app.postRunnable(new Runnable() {
//                    @Override
//                    public void run() {
//                        PlayScreen.trackedBody.setTransform(PlayScreen.trackedBody.getPosition().x + PlayScreen.trackedBodyWidth, PlayScreen.trackedBody.getPosition().y, 0);
//                    }
//                });
//                System.out.println("AFTER: " + PlayScreen.trackedBody.getPosition().x);