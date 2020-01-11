package gyurix.icicles.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import java.math.BigDecimal;

import gyurix.icicles.data.Config;
import gyurix.icicles.enums.Constants;
import gyurix.icicles.enums.Difficulty;
import gyurix.icicles.enums.ItemType;
import gyurix.icicles.screen.GameScreen;
import gyurix.icicles.screen.MainMenuScreen;
import lombok.Getter;
import lombok.Setter;

import static gyurix.icicles.IciclesGame.df;
import static gyurix.icicles.IciclesGame.lang;
import static gyurix.icicles.IciclesGame.tf;
import static gyurix.icicles.enums.Constants.GAME_OVER_ICON_SIZE;
import static gyurix.icicles.enums.Constants.GAME_OVER_SCALE;
import static gyurix.icicles.enums.Constants.GAME_OVER_SMILEY_ICON_SIZE;
import static gyurix.icicles.enums.Constants.HUD_BUTTON_SEP;
import static gyurix.icicles.enums.Constants.HUD_FONT_SCALE;
import static gyurix.icicles.enums.Constants.HUD_ICON_SEP;
import static gyurix.icicles.enums.Constants.HUD_ITEM_SEP;
import static gyurix.icicles.enums.Constants.HUD_ITEM_SIZE;
import static gyurix.icicles.enums.Constants.HUD_LINE_SEP;
import static gyurix.icicles.enums.Constants.ICON_SIZE;
import static gyurix.icicles.enums.Constants.PAUSE_DESC_SCALE;
import static gyurix.icicles.enums.Constants.HUD_BUTTON_SIZE;
import static gyurix.icicles.enums.Constants.PAUSE_TITLE_SCALE;
import static gyurix.icicles.enums.Constants.WORLD_HEIGHT;
import static gyurix.icicles.enums.Constants.WORLD_WIDTH;

/**
 * Created by GyuriX on 2017. 08. 17..
 */

@Getter
public class HUD {
    private final Difficulty difficulty;
    private final GameScreen game;
    private final BitmapFont font;
    @Setter
    private float continueIn, distance, elapsedTime;
    private BigDecimal maxScore, melted = new BigDecimal(0), coins = new BigDecimal(0), health = new BigDecimal(0);
    private boolean paused, gameover;

    public HUD(GameScreen game, HUD old) {
        this.game = game;
        font = game.getFont();
        setHealth(Constants.PLAYER_LIFE);
        difficulty = old.getDifficulty();
        setMaxScore(old.getMaxScore());
    }

    public HUD(Difficulty dif, int max) {
        this.game = null;
        font = null;
        difficulty = dif;
        setMaxScore(max);
    }

    public void addCoins(int value) {
        if (Config.sound)
            Sounds.coinpickup.play();
        setCoins(getCoins() + value);
        Config.coins = Config.coins.add(new BigDecimal(value));
    }

    public void addDistance(float delta) {
        distance += delta;
    }

    public void addElapsedTime(float delta) {
        elapsedTime += delta;
    }

    public void gameOver() {
        gameover = true;
    }

    public int getCoins() {
        return coins.intValue();
    }

    public void setCoins(int coins) {
        this.coins = new BigDecimal(coins);
    }

    public int getHealth() {
        return health.intValue();
    }

    public void setHealth(int health) {
        this.health = new BigDecimal(health);
    }

    public int getMaxScore() {
        return maxScore.intValue();
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = new BigDecimal(maxScore);
    }

    public int getMelted() {
        return melted.intValue();
    }

    public void setMelted(int melted) {
        this.melted = new BigDecimal(melted);
    }

    public void render(SpriteBatch batch) {
        if (melted.compareTo(maxScore) > 0)
            setMaxScore(getMelted());
        font.setColor(Constants.HUD_FONT_COLOR);
        font.getData().setScale(HUD_FONT_SCALE);
        float y1 = WORLD_HEIGHT - 20f - ICON_SIZE;
        float ty1 = y1 + ICON_SIZE / 2f + HUD_FONT_SCALE * 24f;
        float y2 = y1 - ICON_SIZE - HUD_LINE_SEP;
        float ty2 = y2 + ICON_SIZE / 2f + HUD_FONT_SCALE * 24f;
        float y3 = y2 - ICON_SIZE - HUD_LINE_SEP;
        float ty3 = y3 + ICON_SIZE / 2f + HUD_FONT_SCALE * 24f;
        float y4 = y3 - ICON_SIZE - HUD_LINE_SEP;
        float ty4 = y4 + ICON_SIZE / 2f + HUD_FONT_SCALE * 24f;
        for (int x = 0; x < getHealth(); ++x)
            batch.draw(Icons.hp, 20 + x * (ICON_SIZE + HUD_ICON_SEP), y1, ICON_SIZE, ICON_SIZE);
        for (int x = 0; x <= difficulty.ordinal(); ++x)
            batch.draw(Icons.difficulty, 20 + x * (ICON_SIZE + HUD_ICON_SEP), y2, ICON_SIZE, ICON_SIZE);
        batch.draw(Icons.highscore, 20f, y3, ICON_SIZE, ICON_SIZE);
        font.draw(batch, df.format(maxScore), 20f + ICON_SIZE + HUD_ICON_SEP, ty3);

        float textX = WORLD_WIDTH - 240f * HUD_FONT_SCALE - HUD_BUTTON_SIZE - HUD_ICON_SEP - 20f;
        float iconX = textX - HUD_ICON_SEP - ICON_SIZE;

        batch.draw(Icons.distance, iconX, y1, ICON_SIZE, ICON_SIZE);
        font.draw(batch, df.format((int) (distance / 50f)) + " m", textX, ty1);
        batch.draw(Icons.time, iconX, y2, ICON_SIZE, ICON_SIZE);
        font.draw(batch, String.format(tf, (int) (elapsedTime / 60), (int) (elapsedTime % 60)), textX, ty2);
        batch.draw(Icons.melted, iconX, y3, ICON_SIZE, ICON_SIZE);
        font.draw(batch, df.format(melted), textX, ty3);
        batch.draw(Icons.coin, iconX, y4, ICON_SIZE, ICON_SIZE);
        font.draw(batch, df.format(coins), textX, ty4);

        renderItems(batch);
        renderButtons(batch);

    }

    private void renderItems(SpriteBatch batch) {
        int i = 0;
        for (ItemType it : ItemType.values()) {
            batch.draw(it.getTexture(), 20 + i * (HUD_ITEM_SIZE + HUD_ITEM_SEP), 20, HUD_ITEM_SIZE, HUD_ITEM_SIZE);
            font.draw(batch, df.format(Config.getItemCount(it)), 20 + i * (HUD_ITEM_SIZE + HUD_ITEM_SEP), 20 + HUD_FONT_SCALE * 24f);
            ++i;
        }
    }

    private void renderButtons(SpriteBatch batch) {
        if (paused || continueIn > 0 || gameover) {
            batch.setColor(1f, 1f, 1f, 0.6f);
            batch.draw(Icons.pauseBg, WORLD_WIDTH / 8f, WORLD_HEIGHT / 16f * 3f,
                    WORLD_WIDTH / 4f * 3f, (gameover ? WORLD_HEIGHT / 4f : 0) + WORLD_HEIGHT / 2f);
            batch.setColor(1f, 1f, 1f, 1f);
        }
        float size = HUD_BUTTON_SIZE;
        float x = WORLD_WIDTH - 20f - size;
        float y = WORLD_HEIGHT - 20f;
        float dist = size + HUD_BUTTON_SEP;
        batch.draw(Icons.pause, x,
                y - dist, size, size);
        batch.draw(Config.music ? Icons.music : Icons.music2, x,
                y - dist * 2f, size, size);
        batch.draw(Config.sound ? Icons.sound : Icons.sound2, x,
                y - dist * 3f, size, size);
        if (gameover) {
            batch.draw(Icons.gameover, WORLD_WIDTH / 2f - GAME_OVER_SMILEY_ICON_SIZE*0.5f,
                    WORLD_HEIGHT/2f + GAME_OVER_SCALE*64f, GAME_OVER_SMILEY_ICON_SIZE, GAME_OVER_SMILEY_ICON_SIZE);
            font.getData().setScale(GAME_OVER_SCALE);
            font.setColor(Constants.GAME_OVER_COLOR);
            font.draw(batch, lang.get("game_over"), 0, WORLD_HEIGHT / 2f + GAME_OVER_SCALE * 48f,
                    WORLD_WIDTH, Align.center, false);
            batch.draw(Icons.mainmenu, WORLD_WIDTH / 2f - GAME_OVER_ICON_SIZE * 1.5f, WORLD_HEIGHT / 2f - GAME_OVER_ICON_SIZE * 1.5f, GAME_OVER_ICON_SIZE, GAME_OVER_ICON_SIZE);
            batch.draw(Icons.reset, WORLD_WIDTH / 2f + GAME_OVER_ICON_SIZE * 0.5f, WORLD_HEIGHT / 2f - GAME_OVER_ICON_SIZE * 1.5f, GAME_OVER_ICON_SIZE, GAME_OVER_ICON_SIZE);
            return;
        }
        if (paused) {
            font.getData().setScale(PAUSE_TITLE_SCALE);
            font.setColor(Constants.PAUSE_TITLE_COLOR);
            font.draw(batch, lang.get("pause_title"), 0, WORLD_HEIGHT / 2f + PAUSE_TITLE_SCALE * 48f,
                    WORLD_WIDTH, Align.center, false);
            font.getData().setScale(PAUSE_DESC_SCALE);
            font.setColor(Constants.PAUSE_DESC_COLOR);
            font.draw(batch, lang.get("pause_sub"), 0, WORLD_HEIGHT / 2f - PAUSE_TITLE_SCALE * 24f - PAUSE_DESC_SCALE * 48f,
                    WORLD_WIDTH, Align.center, false);
        } else
            renderContinue(batch);
    }

    private void renderContinue(SpriteBatch batch) {
        if (continueIn > 0) {
            font.getData().setScale(Constants.PAUSE_COUNT_SCALE);
            font.setColor(Constants.PAUSE_COUNT_COLOR);
            font.draw(batch, String.valueOf((int) (continueIn + 0.9999f)),
                    0, WORLD_HEIGHT / 2f + Constants.PAUSE_COUNT_SCALE * 24f, WORLD_WIDTH, Align.center, false);
        }
    }


    public void setPaused(boolean paused) {
        this.paused = paused;
        if (paused) {
            Sounds.game.pause();
            Sounds.menu.play();
            continueIn = Constants.PAUSE_CD;
        }
    }

    public boolean update(float delta) {
        if (paused || gameover)
            return false;
        if (continueIn > 0) {
            continueIn -= delta;
            if (continueIn <= 0) {
                Sounds.menu.pause();
                Sounds.game.play();
            }
            return false;
        }
        return true;
    }
}
