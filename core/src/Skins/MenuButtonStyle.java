package Skins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuButtonStyle {

    public Label.LabelStyle createTitleTextButtonStyle() {

        Texture buttonUpTexture = new Texture(Gdx.files.internal("menu_button_background.jpg"));
        Texture buttonDownTexture = new Texture(Gdx.files.internal("menu_button_background.jpg"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/COOPBL.TTF"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 25;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.genMipMaps = true; // Enable mipmapping
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();


        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        style.fontColor = Color.BLUE;

        return style;
    }
    public TextButton.TextButtonStyle createTextButtonStyle() {

        Texture buttonUpTexture = new Texture(Gdx.files.internal("menu_button_background.jpg"));
        Texture buttonDownTexture = new Texture(Gdx.files.internal("menu_button_background.jpg"));


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ARLRDBD.TTF"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 8;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.genMipMaps = true;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();


        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(buttonUpTexture));
        style.down = new TextureRegionDrawable(new TextureRegion(buttonDownTexture));
        style.font = font;
        style.fontColor = Color.WHITE;

        return style;
    }

    public LabelStyle createLabelStyle() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/your_font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 8; // Set the font size
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.genMipMaps = true;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        LabelStyle style = new LabelStyle();
        style.font = font;
        style.fontColor = Color.WHITE;

        return style;
    }
}
