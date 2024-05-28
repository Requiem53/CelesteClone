package com.baringproductions.celeste.Screens;

import Scenes.MenuHUD;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.baringproductions.celeste.CelesteGame;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.sql.SQLException;

public class MenuScreen implements Screen {
    private CelesteGame game;
    private OrthographicCamera camera;
    private Viewport gamePort;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    MenuHUD hud;
    Texture menuBackground;
    public MenuScreen(CelesteGame game) {
        this.game = game;
        hud = new MenuHUD(game.batch, game);
        menuBackground = new Texture("menu_background.jpg");
        camera = new OrthographicCamera();
        gamePort = new FitViewport(CelesteGame.WIDTH, CelesteGame.HEIGHT,camera);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level2Celeste.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);


    }
    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if(Gdx.input.isTouched()) {
            camera.position.x += 100 * dt;
        }
    }

    public void update(float dt) {
        handleInput(dt);
        camera.update();
        renderer.setView(camera);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.getBatch().begin();
        hud.stage.getBatch().draw(menuBackground, 0, 0, CelesteGame.WIDTH, CelesteGame.HEIGHT);
        hud.stage.getBatch().end();
        hud.stage.draw();




    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
        hud.stage.dispose();
    }
}
