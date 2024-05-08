package com.baringproductions.celeste;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class CelesteGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private Texture image;
	private Rectangle imageRep;
	World world;

	Box2DDebugRenderer debugRenderer;

	Body body;

	@Override
	public void create () {
//		Box2D.init();
		world = new World(new Vector2(0, -18.81f), true);
		debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		image = new Texture("badlogic.jpg");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);

		imageRep = new Rectangle();
		imageRep.x = Gdx.graphics.getDisplayMode().width / 2 - 64/2;
		imageRep.y = 50;
		imageRep.width = 16;
		imageRep.height = 16;

		paraDiHugaw();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		batch.draw(image, imageRep.x ,imageRep.y);

		batch.end();

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) imageRep.x -= (int) (200 * Gdx.graphics.getDeltaTime());
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) imageRep.x += (int) (200 * Gdx.graphics.getDeltaTime());

		if(Gdx.input.isKeyPressed(Input.Keys.A))
			body.applyLinearImpulse(-(body.getMass()*18), 0f, body.getPosition().x, body.getPosition().y, true);

		if(Gdx.input.isKeyPressed(Input.Keys.D))
			body.applyLinearImpulse(body.getMass()*18, 0f, body.getPosition().x, body.getPosition().y, true);


		world.step(1/60f, 6, 2);
		debugRenderer.render(world, camera.combined);

		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
//			body.setLinearVelocity(0, body.getLinearVelocity().y);
			body.applyLinearImpulse(0f, body.getMass()*18, body.getPosition().x, body.getPosition().y, true);
		}
		body.setLinearVelocity(0, body.getLinearVelocity().y);
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

	public void paraDiHugaw(){
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		bodyDef.type = BodyDef.BodyType.DynamicBody;
// Set our body's starting position in the world
		bodyDef.position.set((float) Gdx.graphics.getDisplayMode().width / 2 - (float) 64 /2, 500);

// Create our body in the world using our body definition
		body = world.createBody(bodyDef);
		body.setFixedRotation(true);

// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(16f);

// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 40f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0f; // Make it bounce a little bit

// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);

// Remember to dispose of any shapes after you're done with them!
// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();

		// First we create a body definition
		BodyDef platform = new BodyDef();
// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		platform.type = BodyDef.BodyType.StaticBody;
// Set our body's starting position in the world
		platform.position.set((float) Gdx.graphics.getDisplayMode().width / 2 - (float) 64 /2, 200);

// Create our body in the world using our body definition
		Body body2 = world.createBody(platform);

// Create a circle shape and set its radius to 6
		PolygonShape rect = new PolygonShape();
		rect.setAsBox(100f, 50f);

// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = rect;
		fixtureDef2.density = 0.5f;
		fixtureDef2.friction = 0.4f;
		fixtureDef2.restitution = 0f; // Make it bounce a little bit

// Create our fixture and attach it to the body
		Fixture fixture2 = body2.createFixture(fixtureDef2);

// Remember to dispose of any shapes after you're done with them!
// BodyDef and FixtureDef don't need disposing, but shapes do.
		rect.dispose();
	}
}
