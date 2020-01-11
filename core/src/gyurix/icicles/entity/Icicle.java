package gyurix.icicles.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gyurix.icicles.enums.Constants;
import gyurix.icicles.enums.IcicleType;
import gyurix.icicles.screen.GameScreen;
import lombok.Getter;
import lombok.Setter;

import static gyurix.icicles.enums.Constants.INITIAL_GRAVITY_RANGE;
import static gyurix.icicles.enums.Constants.PLAYER_HEAD_SIZE;
import static gyurix.icicles.enums.Constants.PLAYER_SIZE;

/**
 * Created by GyuriX on 2017. 08. 16..
 */

@Getter
public class Icicle {

    private GameScreen game;
    private float sizeX, sizeY;
    private Vector2 location;
    @Setter
    private IcicleType type;
    private int value = 1;
    private Vector2 velocity;

    public Icicle(GameScreen game, IcicleType type, float x, float y) {
        this.game = game;
        this.type = type;
        this.location = new Vector2(x, y);
        velocity = new Vector2(0, MathUtils.random(INITIAL_GRAVITY_RANGE * 2) - INITIAL_GRAVITY_RANGE);
        setRandomSize();
    }

    public void setRandomSize() {
        sizeX = MathUtils.random(Constants.MIN_ICE_X_SIZE, Constants.MAX_ICE_X_SIZE);
        sizeY = MathUtils.random(Constants.MIN_ICE_Y_SIZE, Constants.MAX_ICE_Y_SIZE);
    }

    public boolean isValid() {
        return location.y >= 0 && location.x >= 0 && location.x <= Constants.WORLD_WIDTH && location.y <= Constants.WORLD_HEIGHT;
    }

    public void render(ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        Color[] colors = type.getColors();
        renderer.triangle(location.x, location.y,
                location.x + sizeX / 2f, location.y + sizeY,
                location.x - sizeX / 2f, location.y + sizeY,
                colors[0], colors[1], colors[2]);
    }

    public void setLocation(float x, float y) {
        location.x = x;
        location.y = y;
        velocity.x = 0;
        velocity.y = MathUtils.random(INITIAL_GRAVITY_RANGE * 2) - INITIAL_GRAVITY_RANGE;
    }

    public boolean update(float delta) {
        location.mulAdd(velocity, delta);
        velocity.y += Constants.GRAVITY*delta*game.getIcicles().getSpeedMultiplier();
        Vector2 loc2 = new Vector2(location).add(sizeX / 2f, sizeY);
        Vector2 loc3 = new Vector2(location).add(-sizeX / 2f, sizeY);
        if (game.getPlayer().isGodMode() && !type.matchGod())
            return false;
        Vector2 head = new Vector2(game.getPlayer().getLocation()).add(0, PLAYER_SIZE - PLAYER_HEAD_SIZE / 2f);
        if (!type.matchSides())
            return head.dst(location) < PLAYER_HEAD_SIZE / 2f;
        return head.dst(loc2) < PLAYER_HEAD_SIZE / 2f || head.dst(loc3) < PLAYER_HEAD_SIZE / 2f;
    }
}
