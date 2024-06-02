package Scenes;

import Skins.MenuButtonStyle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Database.GameClass;
import com.baringproductions.celeste.Database.PlayerDatabase;
import com.baringproductions.celeste.Screens.*;
import com.baringproductions.celeste.User;

import javax.swing.text.View;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuHUD {
    public Stage stage;
    private Viewport viewport;

    private Table table;
    private ArrayList<GameClass> loadedGames;

    public MenuHUD(SpriteBatch sb, CelesteGame game) {

        LoadGameHUD hud = new LoadGameHUD(sb, game);
        viewport = new FitViewport(CelesteGame.WIDTH, CelesteGame.HEIGHT);
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.top();
        table.setFillParent(true);

        MenuButtonStyle titleStyle = new MenuButtonStyle();
        Label.LabelStyle titleStyleLabel = titleStyle.createTitleTextButtonStyle(155);
        Label title = new Label("Celeste", titleStyleLabel);

        MenuButtonStyle buttonStyle = new MenuButtonStyle();
        TextButton.TextButtonStyle style3 = buttonStyle.createTextButtonStyle();

        TextButton btnNewGame = new TextButton("", style3);
        btnNewGame.setText("New Game");
        btnNewGame.pad(5);
        TextButton btnLoadGame = new TextButton("", style3);
        btnLoadGame.setText("Load Game");
        btnLoadGame.pad(5);
        TextButton btnQuit = new TextButton("", style3);
        btnQuit.setText("Quit");
        btnQuit.pad(5);


        btnNewGame.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                loadedGames = PlayerDatabase.loadGame();
                if(loadedGames.size() >= 5) {
                    return;
                }
                System.out.println("height: " + btnNewGame.getHeight());
                System.out.println("width: " + btnNewGame.getWidth());
                game.setScreen(new PlayerNameScreen(game));
            }
        });
        btnLoadGame.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                System.out.println("height: " + btnNewGame.getHeight());
                System.out.println("width: " + btnNewGame.getWidth());
                game.setScreen(new LoadMenuScreen(game));
            }
        });
        btnQuit.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                // call SQL save
                Gdx.app.exit();
            }
        });

        table.add(title).colspan(3).center();
        table.row();
        table.add(btnNewGame).colspan(3).center().padTop(75);
        table.row();
        table.row().padTop(25);
        table.add(btnLoadGame).colspan(3).center();
        table.row().padTop(25);
        table.add(btnQuit).colspan(3).center();

        stage.addActor(table);
    }
}
