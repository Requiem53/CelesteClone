package com.baringproductions.celeste;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.baringproductions.celeste.Screens.PlayScreen;
import com.baringproductions.celeste.Utils.WorldCreator;

import java.awt.*;

public class CelesteGame extends Game {
	public SpriteBatch batch;
	private OrthographicCamera camera;

	private Texture image;
	private Rectangle imageRep;
	WorldCreator world;

	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 160;
	public static final float PPM = 100;

	public static final int GRAVITY = -5;

	Box2DDebugRenderer debugRenderer;

	@Override
	public void create () {
//		Box2D.init();
//		world = new World(new Vector2(0, -10), true);
//		debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
//		image = new Texture("badlogic.jpg");
//		camera = new OrthographicCamera();
//		camera.setToOrtho(false, 1280, 720);
//
//		imageRep = new Rectangle();
//		imageRep.x = Gdx.graphics.getDisplayMode().width / 2 - 64/2;
//		imageRep.y = 50;
//		imageRep.width = 16;
//		imageRep.height = 16;
	}

	@Override
	public void render () {
		super.render();
//		ScreenUtils.clear(0, 0, 0, 1);
//
//		camera.update();
//		batch.setProjectionMatrix(camera.combined);
//
//		batch.begin();
//
//		batch.draw(image, imageRep.x ,imageRep.y);
//
//		batch.end();
//
//		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) imageRep.x -= (int) (200 * Gdx.graphics.getDeltaTime());
//		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) imageRep.x += (int) (200 * Gdx.graphics.getDeltaTime());
//
//		world.step(1/60f, 6, 2);
//		debugRenderer.render(world, camera.combined);
//
		fullScreenToWindowedControls();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void fullScreenToWindowedControls(){
		//Use escape to go from fscreen to windowed
		//Use F11 to switch switch

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			if (Gdx.graphics.isFullscreen())  Gdx.graphics.setWindowedMode(1280, 720);
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.F11)){
			if (!Gdx.graphics.isFullscreen()) Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			else Gdx.graphics.setWindowedMode(1280, 720);
		}
	}
}
