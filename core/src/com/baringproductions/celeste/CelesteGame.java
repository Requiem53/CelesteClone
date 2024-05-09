package com.baringproductions.celeste;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.baringproductions.celeste.Statics.Constants;
import com.baringproductions.celeste.Statics.Player;

import static com.baringproductions.celeste.Statics.Constants.PPMScaled;
import static com.baringproductions.celeste.Statics.Constants.createPlayer;

public class CelesteGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;

	Box2DDebugRenderer debugRenderer;

	World world;
	Player player;
	Body playerBody;

	float runSpeed = 6f;
	float jumpHeight = 10f;

	@Override
	public void create () {
		world = new World(new Vector2(0, -15.81f), true);
		debugRenderer = new Box2DDebugRenderer();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, PPMScaled(1280), PPMScaled(720));

		batch = new SpriteBatch();

		player = createPlayer(world);
		playerBody = player.body;

		platformMaker(1);
		platformMaker(5);
		platformMaker(10);
		platformMaker(15);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		batch.end();


		if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
			playerBody.applyLinearImpulse(-(playerBody.getMass()*runSpeed), 0f,
					playerBody.getPosition().x, playerBody.getPosition().y, true);

		if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			playerBody.applyLinearImpulse(playerBody.getMass()*runSpeed, 0f,
					playerBody.getPosition().x, playerBody.getPosition().y, true);

		world.step(1/60f, 6, 2);
		debugRenderer.render(world, camera.combined);

		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			playerBody.applyLinearImpulse(0f , playerBody.getMass()*jumpHeight ,
					playerBody.getPosition().x, playerBody.getPosition().y,true);
		}
		playerBody.setLinearVelocity(0, playerBody.getLinearVelocity().y);
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

	public void platformMaker(int pos){
		BodyDef platform = new BodyDef();
		platform.type = BodyDef.BodyType.StaticBody;
		platform.position.set((((float) Gdx.graphics.getDisplayMode().width / 2) - (float) (64*pos)/2) / Constants.PPM, 200 / Constants.PPM);
		Body body2 = world.createBody(platform);

		PolygonShape rect = new PolygonShape();
		rect.setAsBox(100f / Constants.PPM, 50f / Constants.PPM);

		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = rect;
		Fixture fixture2 = body2.createFixture(fixtureDef2);
		rect.dispose();
	}
}
