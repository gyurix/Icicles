package gyurix.icicles.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import gyurix.icicles.data.Config;
import gyurix.icicles.enums.Constants;
import gyurix.icicles.enums.IcicleType;
import gyurix.icicles.screen.GameScreen;
import gyurix.icicles.screen.LoadingScreen;
import gyurix.icicles.screen.MainMenuScreen;
import lombok.Getter;
import lombok.Setter;

import static gyurix.icicles.data.Config.difficulty;

/**
 * Created by GyuriX on 2017. 08. 16..
 */

@Getter
public class Icicles {
    @Setter
    private long hardToCoin = 0L;
    @Setter
    private long slowDownExpire;
    @Setter
    private float slowDownPower;
    private Array<Icicle> icicles = new Array<Icicle>();
    private GameScreen game;

    public Icicles(GameScreen game) {
        this.game = game;
        int max = difficulty.getIcicleCount();
        for (int i = 0; i < max; ++i)
            icicles.add(new Icicle(game, getRandomType(), MathUtils.random(Constants.WORLD_WIDTH), Constants.WORLD_HEIGHT));
    }

    public void die() {
        if (Config.sound)
            Sounds.icebreak.play();
        game.getHud().setHealth(game.getHud().getHealth() - 1);
        if (game.getHud().getHealth() < 1) {
            game.getHud().gameOver();
            return;
        }
        for (Icicle ic : icicles)
            ic.setLocation(MathUtils.random(Constants.WORLD_WIDTH), Constants.WORLD_HEIGHT);
    }

    public IcicleType getRandomType() {
        float total = 0;
        for (IcicleType it : IcicleType.values())
            total += it.getChance();
        float goal = MathUtils.random(total);
        for (IcicleType it : IcicleType.values()) {
            goal -= it.getChance();
            if (goal <= 0) {
                if (it == IcicleType.HARD && hardToCoin > System.currentTimeMillis())
                    return IcicleType.COIN;
                return it;
            }
        }
        return null;
    }

    public float getSpeedMultiplier() {
        return slowDownExpire > System.currentTimeMillis() ? slowDownPower : 1;
    }

    public void render(ShapeRenderer renderer) {
        for (Icicle ic : icicles)
            ic.render(renderer);
    }

    public void update(float delta) {
        for (Icicle ic : icicles) {
            if (ic.update(delta))
                switch (ic.getType()) {
                    case HARD:
                    case NORMAL: {
                        die();
                        return;
                    }
                    case COIN: {
                        game.getHud().addCoins(ic.getValue());
                        ic.setType(getRandomType());
                        ic.setRandomSize();
                        ic.setLocation(MathUtils.random(Constants.WORLD_WIDTH), Constants.WORLD_HEIGHT);
                        break;
                    }
                }

            if (!ic.isValid()) {
                game.getHud().setMelted(game.getHud().getMelted() + 1);
                ic.setType(getRandomType());
                ic.setRandomSize();
                ic.setLocation(MathUtils.random(Constants.WORLD_WIDTH), Constants.WORLD_HEIGHT);
            }
        }
    }
}
