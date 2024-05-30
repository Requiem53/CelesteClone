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
import com.baringproductions.celeste.Database.PlayerDatabase;
import com.baringproductions.celeste.Screens.GameMenuScreen;
import com.baringproductions.celeste.Screens.LoadMenuScreen;
import com.baringproductions.celeste.Screens.MenuScreen;
import com.baringproductions.celeste.Screens.PlayScreen;
import com.baringproductions.celeste.User;

import javax.swing.text.View;
import java.awt.*;
import java.sql.SQLException;

public class PlayerNameHUD {
    public Stage stage;
    private Viewport viewport;

    public TextButton btnPlayGame;
    public TextButton btnLoadGame;
    public TextButton btnQuit;
    public TextButton btnBack;
    public Table table;
    Label title;
    Label txtName;
    TextField playerName;

    public PlayerNameHUD(SpriteBatch sb, CelesteGame game) {

        LoadGameHUD hud = new LoadGameHUD(sb, game);
        viewport = new FitViewport(CelesteGame.WIDTH, CelesteGame.HEIGHT);
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.top();
        table.setFillParent(true);

        MenuButtonStyle titleStyle = new MenuButtonStyle();
        Label.LabelStyle titleStyleLabel = titleStyle.createTitleTextButtonStyle(155);
        title = new Label("Celeste", titleStyleLabel);
        txtName = new Label("Enter name", titleStyle.createLabelStyle(35));

        MenuButtonStyle buttonStyle = new MenuButtonStyle();
        TextButton.TextButtonStyle style3 = buttonStyle.createTextButtonStyle();

        playerName = new TextField("Name", buttonStyle.createTextFieldStyle());
        playerName.setHeight(89);
        playerName.setWidth(255);

        btnPlayGame = new TextButton("", style3);
        btnPlayGame.setText("Play");
        btnPlayGame.pad(5);

        btnPlayGame.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                System.out.println("height: " + btnPlayGame.getHeight());
                System.out.println("width: " + btnPlayGame.getWidth());
                String name = playerName.getText();

                //game.setScreen(new PlayScreen(PlayerDatabase.getNewUser(name), game));
                User newUser = PlayerDatabase.getNewUser(name);
                if (newUser != null) {
                    game.setScreen(new PlayScreen(newUser, game));
                } else {
                    System.out.println("Failed to create a new user");
                }
            }
        });
        btnBack = new TextButton("Back", style3);
        btnBack.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                // call SQL save
                game.setScreen(new MenuScreen(game));
            }
        });

        table.add(title).colspan(3).center();
        table.row();
        table.add(playerName).colspan(3).center().padTop(75);
        table.row();
        table.add(btnPlayGame).colspan(3).center().padTop(25);
        table.row();
        table.add(btnBack).colspan(3).center().padTop(35);


        stage.addActor(table);
    }
}
