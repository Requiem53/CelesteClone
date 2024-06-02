package com.baringproductions.celeste.Screens;

import Scenes.LoadGameHUD;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.baringproductions.celeste.CelesteGame;

public class LoadMenuScreen implements Screen {
    private CelesteGame game;
    private LoadGameHUD hud;
    private Texture menuBackground;
    public LoadMenuScreen(CelesteGame game) {
        this.game = game;
        hud = new LoadGameHUD(game.batch, game);
        menuBackground = new Texture("menu_background.jpg");
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.getBatch().begin();
        hud.stage.getBatch().draw(menuBackground, 0, 0, CelesteGame.WIDTH, CelesteGame.HEIGHT);
        hud.stage.getBatch().end();
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        hud.stage.dispose();
    }
}
