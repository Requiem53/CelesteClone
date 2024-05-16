package com.baringproductions.celeste.Utils;

import com.badlogic.gdx.physics.box2d.*;
import com.baringproductions.celeste.Player;
import com.baringproductions.celeste.Tiles.InteractiveTile;

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

//                if(player.bottomFixture.getFriction() != player.originalFriction){
//                    player.bottomFixture.setFriction(player.originalFriction);
//                    player.body.setLinearVelocity(-player.body.getLinearVelocity().x,-player.body.getLinearVelocity().y);
////                    player.body.applyLinearImpulse(0f, 0.5f,
////                            player.body.getPosition().x, player.body.getPosition().y, true);
//                }
////                player.onGround = true;
            }
        }

        if ((fixtureA.getUserData() == "wallSensorLeft") ||
                (fixtureB.getUserData() == "wallSensorLeft")) {

            Fixture feet = fixtureA.getUserData() == "wallSensorLeft" ? fixtureA : fixtureB;
            Fixture wall = feet == fixtureA ? fixtureB : fixtureA;

            if (wall.getUserData() != null &&
                    InteractiveTile.class.isAssignableFrom(wall.getUserData().getClass())) {

                ((InteractiveTile) wall.getUserData()).onFeetContact();
                player.canLeft = false;
                System.out.println("LEFT");
            }
        }

        if ((fixtureA.getUserData() == "wallSensorRight") ||
                (fixtureB.getUserData() == "wallSensorRight")) {

            Fixture feet = fixtureA.getUserData() == "wallSensorRight" ? fixtureA : fixtureB;
            Fixture wall = feet == fixtureA ? fixtureB : fixtureA;

            if (wall.getUserData() != null &&
                    InteractiveTile.class.isAssignableFrom(wall.getUserData().getClass())) {

                ((InteractiveTile) wall.getUserData()).onFeetContact();
                player.canRight = false;
                System.out.println("RIGHT");
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

                ((InteractiveTile) ground.getUserData()).onFeetContact();
                player.canJump = false;
                player.onGround = false;
//                player.bottomFixture.setFriction(0f);
            }
        }

        if ((fixtureA.getUserData() == "wallSensorLeft") ||
                (fixtureB.getUserData() == "wallSensorLeft")) {

            Fixture feet = fixtureA.getUserData() == "wallSensorLeft" ? fixtureA : fixtureB;
            Fixture wall = feet == fixtureA ? fixtureB : fixtureA;

            if (wall.getUserData() != null &&
                    InteractiveTile.class.isAssignableFrom(wall.getUserData().getClass())) {

                ((InteractiveTile) wall.getUserData()).onFeetContact();
                player.canLeft = true;
                System.out.println("LEFT");
            }
        }

        if ((fixtureA.getUserData() == "wallSensorRight") ||
                (fixtureB.getUserData() == "wallSensorRight")) {

            Fixture feet = fixtureA.getUserData() == "wallSensorRight" ? fixtureA : fixtureB;
            Fixture wall = feet == fixtureA ? fixtureB : fixtureA;

            if (wall.getUserData() != null &&
                    InteractiveTile.class.isAssignableFrom(wall.getUserData().getClass())) {

                ((InteractiveTile) wall.getUserData()).onFeetContact();
                player.canRight = true;
                System.out.println("RIGHT");
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
