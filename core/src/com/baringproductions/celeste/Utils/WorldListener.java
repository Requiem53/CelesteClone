package com.baringproductions.celeste.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Gdx.app.log("BEGIN CONTACT", "");
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("END CONTACT", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
