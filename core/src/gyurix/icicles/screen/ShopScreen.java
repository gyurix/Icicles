package gyurix.icicles.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
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
import gyurix.icicles.enums.Constants;
import gyurix.icicles.enums.ItemType;

import static gyurix.icicles.IciclesGame.df;
import static gyurix.icicles.IciclesGame.lang;
import static gyurix.icicles.data.Config.coins;
import static gyurix.icicles.enums.Constants.HUD_BUTTON_SIZE;
import static gyurix.icicles.enums.Constants.HUD_FONT_SCALE;
import static gyurix.icicles.enums.Constants.HUD_ICON_SEP;
import static gyurix.icicles.enums.Constants.ICON_SIZE;
import static gyurix.icicles.enums.Constants.SHOP_BUY_ICON_SIZE;
import static gyurix.icicles.enums.Constants.SHOP_CLOSE_X;
import static gyurix.icicles.enums.Constants.SHOP_CLOSE_Y;
import static gyurix.icicles.enums.Constants.SHOP_DESC;
import static gyurix.icicles.enums.Constants.SHOP_DESC_BUY;
import static gyurix.icicles.enums.Constants.SHOP_DESC_PRICE;
import static gyurix.icicles.enums.Constants.SHOP_DESC_TITLE;
import static gyurix.icicles.enums.Constants.SHOP_DESC_WIDTH;
import static gyurix.icicles.enums.Constants.SHOP_FIRST_ITEM;
import static gyurix.icicles.enums.Constants.SHOP_FRAME_SIZE;
import static gyurix.icicles.enums.Constants.SHOP_ITEM_COLUMNS;
import static gyurix.icicles.enums.Constants.SHOP_ITEM_COUNT_SCALE;
import static gyurix.icicles.enums.Constants.SHOP_TITLE_SCALE;
import static gyurix.icicles.enums.Constants.WORLD_HEIGHT;
import static gyurix.icicles.enums.Constants.WORLD_WIDTH;

/**
 * Created by GyuriX on 2017. 08. 16..
 */

public class ShopScreen extends ScreenAdapter implements InputProcessor {
    private SpriteBatch batch;
    private BitmapFont font;
    private IciclesGame game;
    private ShapeRenderer renderer;
    private int selected;
    private FitViewport viewport;

    public ShopScreen(IciclesGame game) {
        this.game = game;
        font = game.getFont();
    }


    public void drawHUD() {
        batch.draw(Icons.shopBg, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        font.getData().setScale(SHOP_TITLE_SCALE);
        font.setColor(Constants.SHOP_TITLE_COLOR);
        font.draw(batch, lang.get("menu_shop"), 0, WORLD_HEIGHT - SHOP_TITLE_SCALE * 24f, WORLD_WIDTH - 20, Align.center, false);
        font.setColor(Constants.HUD_FONT_COLOR);
        font.getData().setScale(HUD_FONT_SCALE);
        batch.draw(Icons.coin, 20f, WORLD_HEIGHT - 60f - ICON_SIZE, ICON_SIZE, ICON_SIZE);
        font.draw(batch, df.format(coins), 20f + ICON_SIZE + HUD_ICON_SEP,
                WORLD_HEIGHT - 60f - ICON_SIZE / 2f + Constants.HUD_FONT_SCALE * 24f);
        batch.draw(Icons.close, SHOP_CLOSE_X, SHOP_CLOSE_Y, Constants.HUD_BUTTON_SIZE, Constants.HUD_BUTTON_SIZE);
    }

    private void drawItems() {
        ItemType it = ItemType.values()[selected];
        font.setColor(Constants.SHOP_DESC_TITLE_COLOR);
        font.getData().setScale(Constants.SHOP_DESC_TITLE_SCALE);
        font.draw(batch, it.getName(), SHOP_DESC_TITLE.x, SHOP_DESC_TITLE.y, SHOP_DESC_WIDTH, Align.center, true);

        font.setColor(Constants.SHOP_DESC_COLOR);
        font.getData().setScale(Constants.SHOP_DESC_SCALE);
        font.draw(batch, it.getDescription(), SHOP_DESC.x, SHOP_DESC.y, SHOP_DESC_WIDTH, Align.center, true);

        font.setColor(Constants.SHOP_DESC_PRICE_COLOR);
        font.getData().setScale(Constants.SHOP_DESC_PRICE_SCALE);
        font.draw(batch, lang.format("item_price", IciclesGame.df.format(it.getPrice())), SHOP_DESC_PRICE.x, SHOP_DESC_PRICE.y);

        batch.draw(Icons.buy, SHOP_DESC_BUY.x, SHOP_DESC_BUY.y, Constants.SHOP_BUY_ICON_SIZE, Constants.SHOP_BUY_ICON_SIZE);

        font.setColor(Constants.SHOP_ITEM_COUNT_COLOR);
        font.getData().setScale(Constants.SHOP_ITEM_COUNT_SCALE);
        float xDist = Constants.SHOP_FRAME_XSEP + Constants.SHOP_FRAME_SIZE;
        float yDist = Constants.SHOP_FRAME_YSEP + Constants.SHOP_FRAME_SIZE;
        float itemAdd = (Constants.SHOP_FRAME_SIZE - Constants.SHOP_ITEM_SIZE) / 2f;
        int id = 0;
        for (int row = 0; row < Constants.SHOP_ITEM_ROWS; ++row)
            for (int col = 0; col < Constants.SHOP_ITEM_COLUMNS; ++col) {
                it = ItemType.values()[id];
                batch.draw(selected == row * SHOP_ITEM_COLUMNS + col ? Icons.shopItem2 : Icons.shopItem,
                        SHOP_FIRST_ITEM.x + col * xDist, SHOP_FIRST_ITEM.y - row * yDist,
                        Constants.SHOP_FRAME_SIZE, Constants.SHOP_FRAME_SIZE);
                batch.draw(it.getTexture(), SHOP_FIRST_ITEM.x + col * xDist + itemAdd,
                        SHOP_FIRST_ITEM.y - row * yDist + itemAdd,
                        Constants.SHOP_ITEM_SIZE, Constants.SHOP_ITEM_SIZE);
                font.draw(batch, df.format(Config.getItemCount(it)),
                        SHOP_FIRST_ITEM.x + col * xDist, SHOP_FIRST_ITEM.y - row * yDist + SHOP_ITEM_COUNT_SCALE * 24f);
                ++id;
                if (id >= ItemType.values().length)
                    return;
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
        if (new Rectangle(SHOP_CLOSE_X, SHOP_CLOSE_Y, HUD_BUTTON_SIZE, HUD_BUTTON_SIZE).contains(coords)) {
            game.setScreen(new MainMenuScreen(game));
            return true;
        }
        if (new Rectangle(SHOP_DESC_BUY.x, SHOP_DESC_BUY.y, SHOP_BUY_ICON_SIZE, SHOP_BUY_ICON_SIZE).contains(coords)) {
            Config.buyItem(ItemType.values()[selected]);
            return true;
        }
        float xDist = Constants.SHOP_FRAME_XSEP + Constants.SHOP_FRAME_SIZE;
        float yDist = Constants.SHOP_FRAME_YSEP + Constants.SHOP_FRAME_SIZE;
        for (int col = 0; col < Constants.SHOP_ITEM_COLUMNS; ++col)
            for (int row = 0; row < Constants.SHOP_ITEM_ROWS; ++row) {
                if (new Rectangle(SHOP_FIRST_ITEM.x + xDist * col, SHOP_FIRST_ITEM.y - yDist * row, SHOP_FRAME_SIZE, SHOP_FRAME_SIZE).contains(coords)) {
                    int id = row * SHOP_ITEM_COLUMNS + col;
                    if (id >= ItemType.values().length)
                        return true;
                    selected = id;
                    return true;
                }
            }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();
        drawHUD();
        drawItems();
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
