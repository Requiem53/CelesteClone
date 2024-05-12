package com.baringproductions.celeste.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.baringproductions.celeste.Statics.Constants;
import com.baringproductions.celeste.Utils.WorldCreator;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Player;
import com.baringproductions.celeste.Utils.WorldListener;

public class PlayScreen implements Screen {

    // play screen vars
    private final CelesteGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;

    //Tiled map variables
    private TmxMapLoader maploader;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private final World world;
    private final Box2DDebugRenderer b2dr;
    private WorldCreator creator;

    Player player;

    public PlayScreen(CelesteGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(
                CelesteGame.V_WIDTH/48f,
                CelesteGame.V_HEIGHT/48f, camera);

        maploader = new TmxMapLoader();
        map = maploader.load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / CelesteGame.PPM);
        camera.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);

        world = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, CelesteGame.GRAVITY), true);
        b2dr = new Box2DDebugRenderer();

        creator = new WorldCreator(this);

        player = new Player(world);

        world.setContactListener(new WorldListener(player));
    }


    public void update(float dt) {

        player.handleInput(dt);

        world.step(1/60f, 6, 2);

        player.body.setLinearVelocity(0, player.body.getLinearVelocity().y);

        camera.position.x = player.body.getPosition().x;
        
        camera.update();
        renderer.setView(camera);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {

        update(dt);

        ScreenUtils.clear(0,0,0,1);

        renderer.render();
        b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        // draw stuff here
        game.batch.end();
    }

    public com.badlogic.gdx.physics.box2d.World getWorld() { return world; }
    public TiledMap getMap() { return map; }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
