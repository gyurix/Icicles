package gyurix.icicles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.I18NBundle;

import java.text.DecimalFormat;
import java.util.TreeMap;

import gyurix.icicles.screen.LoadingScreen;
import gyurix.icicles.screen.MainMenuScreen;
import lombok.Getter;
import lombok.Setter;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public class IciclesGame extends Game implements InputProcessor {
    public static final AssetManager am = new AssetManager();
    public static final TreeMap<Float, Texture> bgs = new TreeMap<>();
    public static final DecimalFormat df = new DecimalFormat("###,###,###,###");
    public static final TreeMap<Float, Texture> storyBgs = new TreeMap<>();
    public static final String tf = "%02d:%02d";
    public static I18NBundle lang;
    private @Getter
    BitmapFont font;
    private int sizeX, sizeY;
    @Getter
    private String version = "1.4";


    @Override
    public void create() {
        loadFont();
        Gdx.input.setInputProcessor(this);
        setScreen(new LoadingScreen(this));
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.F11) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setDisplayMode(
                        sizeX,
                        sizeY, false);
                return true;
            }
            sizeX = Gdx.graphics.getWidth();
            sizeY = Gdx.graphics.getHeight();
            Gdx.graphics.setDisplayMode(
                    Gdx.graphics.getDesktopDisplayMode().width,
                    Gdx.graphics.getDesktopDisplayMode().height,
                    true
            );
            return true;
        }
        if (screen instanceof InputProcessor)
            ((InputProcessor) screen).keyDown(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (screen instanceof InputProcessor)
            ((InputProcessor) screen).keyUp(keycode);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        if (screen instanceof InputProcessor)
            ((InputProcessor) screen).keyTyped(character);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screen instanceof InputProcessor)
            ((InputProcessor) screen).touchDown(screenX, screenY, pointer, button);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (screen instanceof InputProcessor)
            ((InputProcessor) screen).touchUp(screenX, screenY, pointer, button);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (screen instanceof InputProcessor)
            ((InputProcessor) screen).touchDragged(screenX, screenY, pointer);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (screen instanceof InputProcessor)
            ((InputProcessor) screen).mouseMoved(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        if (screen instanceof InputProcessor)
            ((InputProcessor) screen).scrolled(amount);
        return true;
    }

    private void loadFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Aller.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ftfp.size = 64;
        ftfp.shadowOffsetX = 3;
        ftfp.shadowOffsetY = 3;
        ftfp.shadowGamma = 0.1f;
        ftfp.minFilter = Texture.TextureFilter.Linear;
        ftfp.magFilter = Texture.TextureFilter.Linear;
        ftfp.characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ;abcdefghijklmnopqrstuvwxyzÁÉÍÓÖŐÚÜŰáéíóöőúüű.:?!";
        font = generator.generateFont(ftfp);
        generator.dispose();
    }
}
