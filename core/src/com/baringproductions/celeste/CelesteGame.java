package com.baringproductions.celeste;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class CelesteGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private Texture image;
	private Rectangle imageRep;
	World world;

	Box2DDebugRenderer debugRenderer;

	@Override
	public void create () {
//		Box2D.init();
		world = new World(new Vector2(0, -10), true);
		debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		image = new Texture("badlogic.jpg");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);

		imageRep = new Rectangle();
		imageRep.x = Gdx.graphics.getDisplayMode().width / 2 - 64/2;
		imageRep.y = 50;
		imageRep.width = 64;
		imageRep.height = 64;
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		batch.draw(image, imageRep.x ,imageRep.y);

		batch.end();

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) imageRep.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) imageRep.x += 200 * Gdx.graphics.getDeltaTime();

		world.step(1/60f, 6, 2);
		debugRenderer.render(world, camera.combined);

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
