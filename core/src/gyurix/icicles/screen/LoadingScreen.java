package gyurix.icicles.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import gyurix.icicles.IciclesGame;
import gyurix.icicles.data.Config;
import gyurix.icicles.entity.Icons;
import gyurix.icicles.entity.Sounds;
import gyurix.icicles.enums.Constants;
import gyurix.icicles.level.Levels;

import static gyurix.icicles.IciclesGame.am;
import static gyurix.icicles.IciclesGame.bgs;
import static gyurix.icicles.IciclesGame.lang;

/**
 * Created by GyuriX on 2017. 08. 16..
 */

public class LoadingScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private BitmapFont font;
    private IciclesGame game;
    private ShapeRenderer renderer;
    private FitViewport viewport;

    public LoadingScreen(IciclesGame game) {
        this.game = game;
        font = game.getFont();

    }

    @Override
    public void render(float delta) {
        if (am.update()) {
            Icons.cache();
            Sounds.cache();
            Sounds.menu.play();
            Levels.init();
            float from = 0f;
            bgs.put(-1f, am.get("bg/0.jpg"));
            for (int i = 1; i < 14; ++i) {
                Texture t = am.get("bg/" + i + ".jpg");
                bgs.put(from, t);
                from += t.getHeight() * (i == 8 ? 2 : 10)-Constants.WORLD_HEIGHT;
            }
            for (int i = 0; i < 20; ++i)
                IciclesGame.storyBgs.put(i * (Constants.WORLD_HEIGHT-270f), am.get("story/" + i + ".jpg"));
            game.setScreen(new MainMenuScreen(game));
            return;
        } else {
            if (am.isLoaded("language/lang")) lang = am.get("language/lang");
        }
        Gdx.gl.glClearColor(Constants.LOADING_BACKGROUND.r, Constants.LOADING_BACKGROUND.g,
                Constants.LOADING_BACKGROUND.b, Constants.LOADING_BACKGROUND.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.draw(batch, lang == null ? "Loading...\nPlease wait" : lang.get("loading"), 0, Constants.WORLD_HEIGHT / 2f + 12f * Constants.LOADING_SCALE,
                Constants.WORLD_WIDTH, Align.center, false);
        batch.end();

        float state = am.getProgress() * Constants.LOADING_BAR.width;
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Constants.LOADING_LEFT_COLOR);
        renderer.rect(Constants.LOADING_BAR.x + state, Constants.LOADING_BAR.y, Constants.LOADING_BAR.width - state, Constants.LOADING_BAR.height);
        renderer.setColor(Constants.LOADING_DONE_COLOR);
        renderer.rect(Constants.LOADING_BAR.x, Constants.LOADING_BAR.y, state, Constants.LOADING_BAR.height);
        renderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        font.setColor(Constants.LOADING_COLOR);
        font.getData().setScale(Constants.LOADING_SCALE);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        am.load("language/lang", I18NBundle.class);
        Config.load();
        Sounds.load();
        Icons.load();
        for (int i = 0; i < 14; ++i)
            am.load("bg/" + i + ".jpg", Texture.class);
        for (int i = 0; i < 20; ++i)
            am.load("story/" + i + ".jpg", Texture.class);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
