package gyurix.icicles.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Map;

import gyurix.icicles.IciclesGame;
import gyurix.icicles.data.Config;
import gyurix.icicles.entity.HUD;
import gyurix.icicles.entity.Icicles;
import gyurix.icicles.entity.Player;
import gyurix.icicles.entity.Sounds;
import gyurix.icicles.enums.Constants;
import gyurix.icicles.enums.ControlType;
import gyurix.icicles.enums.ItemType;
import gyurix.icicles.utils.TextureUtils;
import lombok.Getter;
import lombok.Setter;

import static gyurix.icicles.IciclesGame.bgs;
import static gyurix.icicles.enums.Constants.GAME_OVER_ICON_SIZE;
import static gyurix.icicles.enums.Constants.HUD_BUTTON_SEP;
import static gyurix.icicles.enums.Constants.HUD_BUTTON_SIZE;
import static gyurix.icicles.enums.Constants.HUD_ITEM_SEP;
import static gyurix.icicles.enums.Constants.HUD_ITEM_SIZE;
import static gyurix.icicles.enums.Constants.WORLD_HEIGHT;
import static gyurix.icicles.enums.Constants.WORLD_WIDTH;

/**
 * Created by GyuriX on 2017. 08. 16..
 */

@Getter
public class GameScreen extends ScreenAdapter implements InputProcessor {
    @Getter
    private BitmapFont font;
    private IciclesGame game;
    @Setter
    private HUD hud;
    private Icicles icicles;
    private Player player;
    private ShapeRenderer renderer;
    private SpriteBatch batch;
    private FitViewport viewport;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.G)
            player.setGodMode(!player.isGodMode());
        else if (keycode == Input.Keys.DOWN)
            hud.setDistance(Math.max(0, hud.getDistance() - 500));
        else if (keycode == Input.Keys.UP)
            hud.setDistance(hud.getDistance() + 500);
        else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK)
            game.setScreen(new MainMenuScreen(game));
        else
            return false;
        return true;
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
        if (hud.isPaused()) {
            hud.setPaused(false);
            return true;
        }
        Vector2 coords = viewport.unproject(new Vector2(screenX, screenY));
        float size = HUD_BUTTON_SIZE;
        float x = WORLD_WIDTH - 20f - size;
        float y = WORLD_HEIGHT - 20f;
        float dist = size + HUD_BUTTON_SEP;
        if (hud.isGameover()) {
            if (new Rectangle(WORLD_WIDTH / 2f + GAME_OVER_ICON_SIZE * 0.5f, WORLD_HEIGHT / 2f - GAME_OVER_ICON_SIZE * 1.5f, GAME_OVER_ICON_SIZE, GAME_OVER_ICON_SIZE).contains(coords)) {
                game.setScreen(new GameScreen(game));
                return true;
            } else if (new Rectangle(WORLD_WIDTH / 2f - GAME_OVER_ICON_SIZE * 1.5f, WORLD_HEIGHT / 2f - GAME_OVER_ICON_SIZE * 1.5f, GAME_OVER_ICON_SIZE, GAME_OVER_ICON_SIZE).contains(coords)) {
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        } else {
            if (new Rectangle(x, y - dist, x + size, y - dist + size).contains(coords)) {
                hud.setPaused(true);
                return true;
            }
        }
        if (new Rectangle(x, y - dist * 2f, x + size, y - dist * 2f + size).contains(coords)) {
            Config.music = !Config.music;
            if (Config.music)
                (hud.isPaused() ? Sounds.menu : Sounds.game).play();
            else
                (hud.isPaused() ? Sounds.menu : Sounds.game).pause();
            return true;
        }
        if (new Rectangle(x, y - dist * 3f, x + size, y - dist * 3f + size).contains(coords)) {
            Config.sound = !Config.sound;
            return true;
        }
        int i = 0;
        for (ItemType it : ItemType.values()) {
            if (new Rectangle(20 + i * (HUD_ITEM_SIZE + HUD_ITEM_SEP), 20, HUD_ITEM_SIZE, HUD_ITEM_SIZE).contains(coords)) {
                if (Config.useItem(it)) {
                    apply(it);
                    return true;
                }
            }
            ++i;
        }
        if (hud.getContinueIn() > 0.2f)
            return true;
        if (Config.control == ControlType.DRAG_AND_CLICK)
            player.teleport(screenX, screenY);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (hud.isPaused() || hud.getContinueIn() > 0.2f)
            return true;
        Vector2 coords = viewport.unproject(new Vector2(screenX, screenY));
        float size = HUD_BUTTON_SIZE;
        float x = WORLD_WIDTH - 20f - size;
        float y = WORLD_HEIGHT - 20f;
        float dist = size + HUD_BUTTON_SEP;
        if (new Rectangle(x, y - dist, x + size, y - dist + size).contains(coords)) {
            return true;
        }
        if (new Rectangle(x, y - dist * 2f, x + size, y - dist * 2f + size).contains(coords)) {
            return true;
        }
        if (new Rectangle(x, y - dist * 3f, x + size, y - dist * 3f + size).contains(coords)) {
            return true;
        }
        if (Config.control == ControlType.DRAG_AND_CLICK)
            player.teleport(screenX, screenY);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (hud.isPaused() || hud.getContinueIn() > 0.2f)
            return true;
        if (Config.control == ControlType.HOVER)
            player.teleport(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public GameScreen(IciclesGame game) {
        this.game = game;
        font = game.getFont();
        hud = Config.difficulty.getNewHUD(this);
    }

    public void apply(ItemType it) {
        if (Config.sound)
            Sounds.eat.play();
        switch (it) {
            case APPLE: {
                hud.setHealth(hud.getHealth() + 1);
                return;
            }
            case STRAWBERRY: {
                icicles.setHardToCoin(System.currentTimeMillis() + it.getEffectLength());
                return;
            }
            case BLUEBERRY: {
                icicles.setSlowDownExpire(System.currentTimeMillis() + it.getEffectLength());
                icicles.setSlowDownPower(it.getPower());
                return;
            }
        }
    }


    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        renderBackground(hud.isGameover()||hud.getContinueIn() > 0 ? 0 : delta);
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        icicles.render(renderer);
        player.render(renderer);
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        hud.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    @Override
    public void show() {
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        batch = new SpriteBatch();
        viewport = new FitViewport(WORLD_WIDTH, Constants.WORLD_HEIGHT);
        icicles = new Icicles(this);
        player = new Player(this);
        Sounds.menu.pause();
        Sounds.game.play();
        if (!Config.music)
            Sounds.game.pause();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    public void renderBackground(float delta) {
        hud.addElapsedTime(delta);
        hud.addDistance(delta * Config.difficulty.getBackgroundSpeed());
        Map.Entry<Float, Texture> old = bgs.floorEntry(hud.getDistance() - Constants.WORLD_HEIGHT);
        Map.Entry<Float, Texture> cur = bgs.floorEntry(hud.getDistance());
        Map.Entry<Float, Texture> next = bgs.floorEntry(hud.getDistance() + Constants.WORLD_HEIGHT);
        if (old != null)
            batch.draw(old.getValue(), 0, (old.getKey() - hud.getDistance()) % (old.getValue().getHeight() * 2), Constants.WORLD_WIDTH, old.getValue().getHeight() * 2);
        batch.draw(cur.getValue(), 0, (cur.getKey() - hud.getDistance()) % (cur.getValue().getHeight() * 2) + cur.getValue().getHeight() * 2, Constants.WORLD_WIDTH, cur.getValue().getHeight() * 2);
        TextureUtils.draw(next.getValue(), batch, Constants.WORLD_HEIGHT, (next.getKey() - hud.getDistance()), next.getValue().getHeight() * 2);
    }

    private void update(float delta) {
        if (!hud.update(delta))
            return;
        player.update(delta);
        icicles.update(delta);
    }
}
