package gyurix.icicles.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import gyurix.icicles.IciclesGame;
import gyurix.icicles.data.Config;
import gyurix.icicles.entity.Icons;
import gyurix.icicles.entity.Sounds;
import gyurix.icicles.enums.Constants;
import gyurix.icicles.enums.ControlType;
import gyurix.icicles.enums.Difficulty;

import static gyurix.icicles.IciclesGame.df;
import static gyurix.icicles.IciclesGame.lang;
import static gyurix.icicles.data.Config.coins;
import static gyurix.icicles.entity.Icons.difficulty;
import static gyurix.icicles.entity.Icons.difficulty2;
import static gyurix.icicles.enums.Constants.DIFFICULTY_ICON_SIZE;
import static gyurix.icicles.enums.Constants.HUD_FONT_SCALE;
import static gyurix.icicles.enums.Constants.MENU_BOX_CONTROL;
import static gyurix.icicles.enums.Constants.MENU_BOX_PLAY;
import static gyurix.icicles.enums.Constants.MENU_BOX_SHOP;
import static gyurix.icicles.enums.Constants.MENU_BOX_STORY;
import static gyurix.icicles.enums.Constants.MENU_INFO_TEXT_SCALE;
import static gyurix.icicles.enums.Constants.WORLD_HEIGHT;
import static gyurix.icicles.enums.Constants.WORLD_WIDTH;

/**
 * Created by GyuriX on 2017. 08. 16..
 */

public class MainMenuScreen extends ScreenAdapter implements InputProcessor {
    private SpriteBatch batch;
    private IciclesGame game;
    private BitmapFont font;
    private ShapeRenderer renderer;
    private FitViewport viewport;

    public MainMenuScreen(IciclesGame game) {
        this.game = game;
        this.font = game.getFont();
    }

    public void drawBox(Rectangle box, String text) {
        batch.setColor(new Color(1,1,1,0.7f));
        batch.draw(Icons.snow, box.x, box.y, box.width, box.height);
        batch.setColor(new Color(1,1,1,1f));
        font.draw(batch, text, box.x, box.y + box.height / 2f + font.getScaleY() * 24f, box.width, Align.center, false);
    }

    private void drawBoxes() {
        font.getData().setScale(Constants.MENU_BOX_SCALE);
        font.setColor(Constants.MENU_TEXT_COLOR);
        //drawBox(MENU_BOX_STORY, lang.get("menu_story"));
        drawBox(MENU_BOX_PLAY, lang.get("menu_play"));
        drawBox(MENU_BOX_SHOP, lang.get("menu_shop"));
        drawBox(MENU_BOX_CONTROL, lang.format("menu_control", lang.get("menu_control_" + Config.control.name())));
    }

    private void drawDifficulties() {
        float y = WORLD_HEIGHT / 8f * 5f - DIFFICULTY_ICON_SIZE / 2f;
        float am = DIFFICULTY_ICON_SIZE + Constants.DIFFICULTY_ICON_SEP;
        float x3 = WORLD_WIDTH / 2f - DIFFICULTY_ICON_SIZE / 2f;
        float x1 = x3 - 2 * am;
        float x2 = x3 - am;
        float x4 = x3 + am;
        float x5 = x3 + 2 * am;
        int id = Config.difficulty.ordinal();
        batch.draw(id < 0 ? difficulty2 : difficulty, x1, y, DIFFICULTY_ICON_SIZE, DIFFICULTY_ICON_SIZE);
        batch.draw(id < 1 ? difficulty2 : difficulty, x2, y, DIFFICULTY_ICON_SIZE, DIFFICULTY_ICON_SIZE);
        batch.draw(id < 2 ? difficulty2 : difficulty, x3, y, DIFFICULTY_ICON_SIZE, DIFFICULTY_ICON_SIZE);
        batch.draw(id < 3 ? difficulty2 : difficulty, x4, y, DIFFICULTY_ICON_SIZE, DIFFICULTY_ICON_SIZE);
        batch.draw(id < 4 ? difficulty2 : difficulty, x5, y, DIFFICULTY_ICON_SIZE, DIFFICULTY_ICON_SIZE);
    }

    private void drawInfo() {
        font.getData().setScale(MENU_INFO_TEXT_SCALE);
        font.setColor(Constants.MENU_INFO_TEXT_COLOR);
        font.draw(batch, lang.format("menu_info", game.getVersion()), 0, WORLD_HEIGHT - MENU_INFO_TEXT_SCALE * 24f, WORLD_WIDTH - 20, Align.center, false);
    }

    public void drawStats() {
        font.setColor(Constants.HUD_FONT_COLOR);
        font.getData().setScale(HUD_FONT_SCALE);
        batch.draw(IciclesGame.bgs.floorEntry(-1f).getValue(), 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        float textX = Constants.WORLD_WIDTH - 60f * Constants.HUD_FONT_SCALE - Constants.HUD_BUTTON_SIZE - Constants.HUD_ICON_SEP - 20f;
        float iconX = textX - Constants.HUD_ICON_SEP - Constants.ICON_SIZE;
        float y1 = Constants.WORLD_HEIGHT - 20f - Constants.ICON_SIZE;
        float ty1 = y1 + Constants.ICON_SIZE / 2f + Constants.HUD_FONT_SCALE * 24f;
        float y2 = y1 - Constants.HUD_LINE_SEP - Constants.ICON_SIZE;
        float ty2 = ty1 - Constants.HUD_LINE_SEP - Constants.ICON_SIZE;
        batch.draw(Icons.coin, iconX, y1, Constants.ICON_SIZE, Constants.ICON_SIZE);
        font.draw(batch, df.format(coins), textX, ty1);
        batch.draw(Icons.highscore, iconX, y2, Constants.ICON_SIZE, Constants.ICON_SIZE);
        font.draw(batch, df.format(Config.hud.get(Config.difficulty).getMaxScore()), textX, ty2);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            System.exit(0);
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
        float am = DIFFICULTY_ICON_SIZE + Constants.DIFFICULTY_ICON_SEP;
        float y = WORLD_HEIGHT / 8f * 5f - DIFFICULTY_ICON_SIZE / 2f;
        float x = WORLD_WIDTH / 2f - DIFFICULTY_ICON_SIZE - 2.5f * am;
        for (Difficulty dif : Difficulty.values()) {
            x += am;
            if (new Rectangle(x, y, DIFFICULTY_ICON_SIZE, DIFFICULTY_ICON_SIZE).contains(coords)) {
                Config.difficulty = dif;
                return true;
            }
        }
        if (MENU_BOX_SHOP.contains(coords)) {
            game.setScreen(new ShopScreen(game));
            return true;
        } else if (MENU_BOX_PLAY.contains(coords)) {
            game.setScreen(new GameScreen(game));
            return true;
        /*} else if (MENU_BOX_STORY.contains(coords)) {
            game.setScreen(new StoryScreen(game));
            return true;*/
        } else if (MENU_BOX_CONTROL.contains(coords)) {
            Config.control = ControlType.values()[((Config.control.ordinal() + 1) % ControlType.values().length)];
            Config.save();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 coords = viewport.unproject(new Vector2(screenX, screenY));
        float am = DIFFICULTY_ICON_SIZE + Constants.DIFFICULTY_ICON_SEP;
        float y = WORLD_HEIGHT / 8f * 5f - DIFFICULTY_ICON_SIZE / 2f;
        float x = WORLD_WIDTH / 2f - DIFFICULTY_ICON_SIZE - 2.5f * am;
        for (Difficulty dif : Difficulty.values()) {
            x += am;
            if (new Rectangle(x, y, DIFFICULTY_ICON_SIZE, DIFFICULTY_ICON_SIZE).contains(coords)) {
                Config.difficulty = dif;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        drawStats();
        drawDifficulties();
        drawInfo();
        drawBoxes();
        batch.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
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
        Sounds.game.stop();
        if (Config.music)
            Sounds.menu.play();
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
