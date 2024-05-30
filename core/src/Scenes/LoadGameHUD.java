package Scenes;

import Skins.MenuButtonStyle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.baringproductions.celeste.CelesteGame;
import com.baringproductions.celeste.Database.GameClass;
import com.baringproductions.celeste.Database.PlayerDatabase;
import com.baringproductions.celeste.Screens.GameMenuScreen;
import com.baringproductions.celeste.Screens.LoadMenuScreen;
import com.baringproductions.celeste.Screens.MenuScreen;
import com.baringproductions.celeste.Screens.PlayScreen;
import com.baringproductions.celeste.User;

import java.sql.ResultSet;
import java.util.ArrayList;

public class LoadGameHUD {
    public Stage stage;
    private Viewport viewport;

    public TextButton btnNewGame;
    public TextButton btnLoadGame;
    public TextButton btnBack;
    public Table mainTable;
    public Table savedGames;
    Label title;
    ArrayList<GameClass> loadedGames;

    public LoadGameHUD(SpriteBatch sb, CelesteGame game) {
        viewport = new FitViewport(CelesteGame.WIDTH, CelesteGame.HEIGHT);
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        mainTable = new Table();
        mainTable.top();
        mainTable.setFillParent(true);

        savedGames = new Table();
        savedGames.top();
        savedGames.setFillParent(true);

        MenuButtonStyle titleStyle = new MenuButtonStyle();
        Label.LabelStyle titleStyleLabel = titleStyle.createTitleTextButtonStyle(85);
        title = new Label("Load Game", titleStyleLabel);

        mainTable.add(title).colspan(3).center().padTop(10).padBottom(35);
        mainTable.row();

        loadedGames = PlayerDatabase.loadGame();

        for (GameClass gameloaded: loadedGames) {
            addSavedGameBlock(gameloaded.getId(), gameloaded.getName(), game);
        }
        MenuButtonStyle buttonStyle = new MenuButtonStyle();
        Label.LabelStyle gameNameStyleLabel = buttonStyle.createLabelStyle(35);
        TextButton.TextButtonStyle style3 = buttonStyle.createTextButtonStyle();
        btnBack = new TextButton("Back", style3);
        btnBack.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                // call SQL save
                game.setScreen(new MenuScreen(game));
            }
        });
        mainTable.add(btnBack).colspan(3).center().padTop(35);
        stage.addActor(mainTable);
    }

    private void addSavedGameBlock(int id, String slotName, CelesteGame game) {
        MenuButtonStyle buttonStyle = new MenuButtonStyle();
        Label.LabelStyle gameNameStyleLabel = buttonStyle.createLabelStyle(35);
        TextButton.TextButtonStyle style3 = buttonStyle.createTextButtonStyle();

        Label gameName = new Label(slotName + "(" +PlayerDatabase.getNumBerries(id) + ")", gameNameStyleLabel);
        TextButton loadButton = new TextButton("Load", style3);
        loadButton.pad(5);
        TextButton deleteButton = new TextButton("Delete", style3);
        loadButton.pad(5);

        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Loading " + slotName);
                User user = PlayerDatabase.loadPlayer(id);
                System.out.println("Spawnpoint: " + user.getSpawn());
                game.setScreen(new PlayScreen(user, game));
            }
        });
        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Loading " + slotName);
                PlayerDatabase.deleteGame(id);
                game.setScreen(new LoadMenuScreen(game));
            }
        });

        mainTable.add(gameName).center().padBottom(10).padRight(25);
        mainTable.add(loadButton).padBottom(10);
        mainTable.add(deleteButton).padBottom(10);
        mainTable.row();
    }
}
