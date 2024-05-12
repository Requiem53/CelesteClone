package com.baringproductions.celeste.Utils;

import com.badlogic.gdx.physics.box2d.*;
import com.baringproductions.celeste.Tiles.InteractiveTile;

public class WorldListener implements ContactListener {
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
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
