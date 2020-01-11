package gyurix.icicles.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Map;

import gyurix.icicles.IciclesGame;
import gyurix.icicles.data.Config;
import gyurix.icicles.entity.Icons;
import gyurix.icicles.enums.Constants;
import gyurix.icicles.level.Level;
import gyurix.icicles.level.Levels;
import gyurix.icicles.utils.TextureUtils;

import static gyurix.icicles.IciclesGame.df;
import static gyurix.icicles.IciclesGame.lang;
import static gyurix.icicles.data.Config.coins;
import static gyurix.icicles.enums.Constants.HUD_BUTTON_SIZE;
import static gyurix.icicles.enums.Constants.HUD_FONT_SCALE;
import static gyurix.icicles.enums.Constants.HUD_ICON_SEP;
import static gyurix.icicles.enums.Constants.ICON_SIZE;
import static gyurix.icicles.enums.Constants.SHOP_CLOSE_X;
import static gyurix.icicles.enums.Constants.SHOP_CLOSE_Y;
import static gyurix.icicles.enums.Constants.STORY_TITLE_COLOR;
import static gyurix.icicles.enums.Constants.STORY_TITLE_SCALE;
import static gyurix.icicles.enums.Constants.WORLD_HEIGHT;
import static gyurix.icicles.enums.Constants.WORLD_WIDTH;

/**
 * Created by GyuriX on 2017. 08. 16..
 */

public class StoryScreen extends ScreenAdapter implements InputProcessor {
    private SpriteBatch batch;
    private BitmapFont font;
    private IciclesGame game;
    private ShapeRenderer renderer;
    private float y;
    private FitViewport viewport;
    private float startY = 0;
    private Vector2 startDragAt = new Vector2();

    public StoryScreen(IciclesGame game) {
        this.game = game;
        font = game.getFont();
    }

    public void drawBackground() {
        float max = IciclesGame.storyBgs.floorKey(100_000_000f);
        y = Math.max(0, Math.min(max, y));
        Map.Entry<Float, Texture> old = IciclesGame.storyBgs.floorEntry(y - Constants.WORLD_HEIGHT / 2);
        Map.Entry<Float, Texture> cur = IciclesGame.storyBgs.floorEntry(y);
        Map.Entry<Float, Texture> next = IciclesGame.storyBgs.floorEntry(y + Constants.WORLD_HEIGHT / 2);
        Map.Entry<Float, Texture> next2 = IciclesGame.storyBgs.floorEntry(y + Constants.WORLD_HEIGHT);
        if (old != null && !old.getKey().equals(cur.getKey()))
            batch.draw(old.getValue(), 0, old.getKey(), WORLD_WIDTH, WORLD_HEIGHT);
        if (cur.getKey() == 0f)
            batch.draw(cur.getValue(), 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        else
            TextureUtils.draw(cur.getValue(), batch, 270, cur.getKey(), WORLD_HEIGHT);
        if (!cur.getKey().equals(next.getKey()))
            TextureUtils.draw(next.getValue(), batch, 270, next.getKey(), WORLD_HEIGHT);
        if (!next.getKey().equals(next2.getKey()))
            TextureUtils.draw(next2.getValue(), batch, 270, next2.getKey(), WORLD_HEIGHT);
    }

    public void drawHUD() {
        font.getData().setScale(STORY_TITLE_SCALE);
        font.setColor(STORY_TITLE_COLOR);
        font.draw(batch, lang.get("menu_story"), 0, WORLD_HEIGHT - STORY_TITLE_SCALE * 24f + y, WORLD_WIDTH - 20, Align.center, false);
        font.setColor(Constants.HUD_FONT_COLOR);
        font.getData().setScale(HUD_FONT_SCALE);
        batch.draw(Icons.coin, 20f, WORLD_HEIGHT - 60f - ICON_SIZE + y, ICON_SIZE, ICON_SIZE);
        font.draw(batch, df.format(coins), 20f + ICON_SIZE + HUD_ICON_SEP,
                WORLD_HEIGHT - 60f - ICON_SIZE / 2f + Constants.HUD_FONT_SCALE * 24f + y);
        batch.draw(Icons.close, SHOP_CLOSE_X, SHOP_CLOSE_Y + y, Constants.HUD_BUTTON_SIZE, Constants.HUD_BUTTON_SIZE);
    }

    private void drawLevels() {
        Gdx.gl.glLineWidth(Constants.LEVEL_CIRCLE_OUTLINE_WIDTH / Constants.WORLD_WIDTH * viewport.getScreenWidth());
        renderer.set(ShapeRenderer.ShapeType.Line);
        ArrayList<Level> lvs = Levels.get(y);
        for (Level l : lvs) {
            Vector2 v2 = l.getLocation();
            if (v2 == null)
                continue;
            renderer.setColor(l.getOutlineColor());
            renderer.circle(v2.x, v2.y, l.getIconSize() + Constants.LEVEL_CIRCLE_OUTLINE_WIDTH / 2f);
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        for (Level l : lvs) {
            Vector2 v2 = l.getLocation();
            if (v2 == null)
                continue;
            renderer.setColor(l.getInnerColor());
            renderer.circle(v2.x, v2.y, l.getIconSize());
        }
    }

    private void drawLevelNames() {
        ArrayList<Level> lvs = Levels.get(y);
        for (Level l : lvs) {
            Vector2 loc = l.getLocation();
            if (loc == null)
                return;
            font.getData().setScale(l.getTextScale());
            font.setColor(l.getTextColor());
            font.draw(batch, String.valueOf(l.getId() + 1),
                    loc.x - l.getIconSize(), loc.y + l.getIconSize() / 2f - l.getTextScale() * 6f,
                    l.getIconSize() * 2f, Align.center, false);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            game.setScreen(new MainMenuScreen(game));
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 coords = viewport.unproject(new Vector2(screenX, screenY));
        if (new Rectangle(SHOP_CLOSE_X, SHOP_CLOSE_Y + y, HUD_BUTTON_SIZE, HUD_BUTTON_SIZE).contains(coords)) {
            game.setScreen(new MainMenuScreen(game));
            return true;
        }
        startDragAt = coords;
        startY = y;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 coords = viewport.unproject(new Vector2(screenX, screenY));
        y = Math.max(0, Math.min(IciclesGame.storyBgs.floorKey(100_000_000f), startY + startDragAt.y - coords.y));
        viewport.getCamera().position.y = y + Constants.WORLD_HEIGHT / 2f;
        viewport.apply();
        coords = viewport.unproject(new Vector2(screenX, screenY));
        startDragAt = coords;
        startY = y;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        y = Math.max(0, Math.min(IciclesGame.storyBgs.floorKey(100_000_000f), y + amount * -200f));
        return true;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        viewport.getCamera().position.y = y + Constants.WORLD_HEIGHT / 2f;
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        drawBackground();
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin();
        drawLevels();
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        drawLevelNames();
        drawHUD();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        batch = new SpriteBatch();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        Config.save();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
