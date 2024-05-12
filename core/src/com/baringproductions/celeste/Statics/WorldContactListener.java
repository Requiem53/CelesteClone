package com.baringproductions.celeste.Statics;

import com.badlogic.gdx.physics.box2d.*;
import com.baringproductions.celeste.CelesteGame;

public class WorldContactListener implements ContactListener {

    Player player;
    public WorldContactListener(Player player){
        this.player = player;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if((fixtureA.getUserData() == "playerFootSensor" && fixtureB.getUserData() == "ground") ||
            (fixtureB.getUserData() == "playerFootSensor" && fixtureA.getUserData() == "ground")){
            player.canJump = true;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if((fixtureA.getUserData() == "playerFootSensor" && fixtureB.getUserData() == "ground") ||
                (fixtureB.getUserData() == "playerFootSensor" && fixtureA.getUserData() == "ground")){
            player.canJump = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
