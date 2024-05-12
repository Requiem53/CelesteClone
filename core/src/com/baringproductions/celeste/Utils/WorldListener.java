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
                player.canJump = true;
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
