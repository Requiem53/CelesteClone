package com.baringproductions.celeste;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.baringproductions.celeste.Database.PlayerDatabase;
import com.baringproductions.celeste.Screens.MenuScreen;
import com.baringproductions.celeste.Screens.PlayerNameScreen;
import com.baringproductions.celeste.Statics.Constants;
import com.baringproductions.celeste.Statics.Player;
import com.baringproductions.celeste.Statics.WorldContactListener;
import com.baringproductions.celeste.Screens.PlayScreen;
import com.baringproductions.celeste.Utils.WorldCreator;

import java.sql.SQLException;

import static com.baringproductions.celeste.Statics.Constants.PPMScaled;
import static com.baringproductions.celeste.Statics.Constants.createPlayer;

public class CelesteGame extends Game {
	public SpriteBatch batch;
	private OrthographicCamera camera;

	WorldCreator worldCreator;

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 160;
	public static final float PPM = 64;

	public static final float GRAVITY = -9.81f;

	public static final short DEFAULT_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short DESTROYED_BIT = 4;
	public static final short BERRY_BIT = 8;
	private Screen currentScreen;

	Box2DDebugRenderer debugRenderer;

	World world;
	Player player;
	Body playerBody;


	public static AssetManager manager;

	@Override
	public void create () {
//		world = new World(new Vector2(0, -15.81f), true);
//
//		player = createPlayer(world);
//		playerBody = player.body;
//
//		platformMaker(1);
//		platformMaker(5);
//		platformMaker(10);
//		platformMaker(15);
//
//		world.setContactListener(new WorldContactListener(player));
//		debugRenderer = new Box2DDebugRenderer();
//
//		camera = new OrthographicCamera();
//		camera.setToOrtho(false, PPMScaled(1280), PPMScaled(720));

		PlayerDatabase.initTables();
		batch = new SpriteBatch();

		manager = new AssetManager();
		manager.load("Audio/Music/bg_music.mp3", Music.class);
		manager.load("Audio/SoundEffects/spring_bounce.wav", Sound.class);
		manager.load("Audio/SoundEffects/platform_collapse.wav", Sound.class);
		manager.load("Audio/SoundEffects/dashgem_get.wav", Sound.class);
		manager.load("Audio/SoundEffects/die.mp3", Sound.class);
		manager.load("Audio/SoundEffects/jump.mp3", Sound.class);
		manager.load("Audio/SoundEffects/dash.wav", Sound.class);
		manager.finishLoading();


		setScreen(new MenuScreen(this));

    }

	@Override
	public void setScreen(Screen screen) {
		// Dispose of the current screen
		if (currentScreen != null) {
			currentScreen.dispose();
		}
		// Set the new screen
		currentScreen = screen;
		super.setScreen(screen);
	}

	@Override
	public void render () {
//		ScreenUtils.clear(0, 0, 0, 1);
//
//		camera.update();
//		batch.setProjectionMatrix(camera.combined);
//
//		batch.begin();
//
//		batch.end();
//
//		player.processInputs();
//
//		world.step(1/60f, 6, 2);
//		debugRenderer.render(world, camera.combined);
//
//		playerBody.setLinearVelocity(0, playerBody.getLinearVelocity().y);

		super.render();
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
		fixtureDef2.friction = 0.8f;
		Fixture groundFixture = body2.createFixture(fixtureDef2);

		groundFixture.setUserData("ground");
		rect.dispose();
	}
}
