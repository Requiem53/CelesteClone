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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Database.PlayerDatabase;
import com.baringproductions.celeste.Screens.GameMenuScreen;
import com.baringproductions.celeste.Screens.LoadMenuScreen;
import com.baringproductions.celeste.Screens.MenuScreen;
import com.baringproductions.celeste.Screens.PlayScreen;
import com.baringproductions.celeste.User;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.sql.SQLException;

public class PlayScreenHUD {
    public Stage stage;
    private Viewport viewport;

    public TextButton btnSave;
    public TextButton btnExit;
    public TextButton btnQuit;
    public TextButton btnClose;
    public Table table;
    public  Window gameMenu;
    Label title;
    User user;

    public PlayScreenHUD(User user, SpriteBatch sb, CelesteGame game) {

        this.user = user;
        MenuButtonStyle titleStyle = new MenuButtonStyle();
        LoadGameHUD hud = new LoadGameHUD(sb, game);
        gameMenu = new Window("Game Menu", titleStyle.createWindowStyle(25));
        gameMenu.top();
        gameMenu.setFillParent(true);
        viewport = new FitViewport(CelesteGame.WIDTH, CelesteGame.HEIGHT);
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.top();
        table.setFillParent(true);

        titleStyle = new MenuButtonStyle();
        Label.LabelStyle titleStyleLabel = titleStyle.createPlayScreenTitleStyle(75);
        title = new Label("Menu", titleStyleLabel);

        MenuButtonStyle buttonStyle = new MenuButtonStyle();
        TextButton.TextButtonStyle style3 = buttonStyle.createTextButtonStyle();

        btnSave = new TextButton("", style3);
        btnSave.setText("Save");
        btnSave.pad(5);
        btnExit = new TextButton("", style3);
        btnExit.setText("Exit");
        btnExit.pad(5);
        btnQuit = new TextButton("", style3);
        btnQuit.setText("Quit");
        btnQuit.pad(5);
        btnClose = new TextButton("", style3);
        btnClose.setText("Close");
        btnClose.pad(5);

        btnSave.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                PlayerDatabase.saveGame();
                table.remove();
            }
        });
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                // call SQL save
                game.setScreen(new MenuScreen(game));
            }
        });
        btnQuit.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                // call SQL save
                game.setScreen(new MenuScreen(game));
                Gdx.app.exit();
            }
        });
        btnClose.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                table.remove();
            }
        });

        table.add(title).colspan(3).center().padTop(55);
        table.row();
        table.add(btnSave).colspan(3).center().padTop(15);
        table.row();
        table.row().padTop(8);
        table.add(btnExit).colspan(3).center();
        table.row().padTop(8);
        table.add(btnQuit).colspan(3).center();
        table.row().padTop(8);
        table.add(btnClose).colspan(3).center();
        gameMenu.add(table);

    }
}
