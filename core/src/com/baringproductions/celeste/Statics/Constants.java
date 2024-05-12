package com.baringproductions.celeste.Statics;

import com.badlogic.gdx.physics.box2d.World;

public class Constants {
    public static final float PPM = 32.0f;
    public static float PPMScaled(float num){
        return num / PPM;
    }

    public static Player createPlayer(World world){
        return new Player(world);
    }
}
