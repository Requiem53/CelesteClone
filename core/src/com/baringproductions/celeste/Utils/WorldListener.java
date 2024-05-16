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
            Fixture dashGem = body == fixtureA ? fixtureB : fixtureA;

            if (dashGem.getUserData() != null &&
                    InteractiveTile.class.isAssignableFrom(dashGem.getUserData().getClass())) {

                ((InteractiveTile) dashGem.getUserData()).onFeetContact();

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
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
