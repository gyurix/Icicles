package gyurix.icicles.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import gyurix.icicles.IciclesGame;
import gyurix.icicles.data.Config;
import gyurix.icicles.enums.Constants;
import gyurix.icicles.enums.ControlType;
import gyurix.icicles.enums.ItemType;
import gyurix.icicles.screen.GameScreen;
import gyurix.icicles.screen.MainMenuScreen;
import lombok.Getter;
import lombok.Setter;

import static gyurix.icicles.IciclesGame.df;
import static gyurix.icicles.enums.Constants.HUD_BUTTON_SEP;
import static gyurix.icicles.enums.Constants.HUD_BUTTON_SIZE;
import static gyurix.icicles.enums.Constants.HUD_FONT_SCALE;
import static gyurix.icicles.enums.Constants.HUD_ITEM_SEP;
import static gyurix.icicles.enums.Constants.HUD_ITEM_SIZE;
import static gyurix.icicles.enums.Constants.WORLD_HEIGHT;
import static gyurix.icicles.enums.Constants.WORLD_WIDTH;

/**
 * Created by GyuriX on 2017. 08. 16..
 */
@Getter
public class Player {
    private Vector2 location, velocity;
    private GameScreen game;
    @Setter
    private boolean godMode;

    public Player(GameScreen game) {
        this.game = game;
        location = new Vector2(Constants.WORLD_WIDTH / 2f, 0);
        velocity = new Vector2();
    }

    public void render(ShapeRenderer renderer) {
        renderer.setColor(Constants.PLAYER_COLOR);

        //Head
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.circle(location.x, location.y + Constants.PLAYER_SIZE - Constants.PLAYER_HEAD_SIZE / 2f, Constants.PLAYER_HEAD_SIZE / 2f);

        //Body
        renderer.rectLine(location.x, location.y + Constants.PLAYER_LEG_HEIGHT,
                location.x, location.y + Constants.PLAYER_SIZE - Constants.PLAYER_HEAD_SIZE,
                Constants.PLAYER_BODY_WIDTH);

        //Legs
        renderer.rectLine(location.x, location.y + Constants.PLAYER_LEG_HEIGHT,
                location.x + Constants.PLAYER_LEG_DISTANCE, location.y, Constants.PLAYER_LEG_WIDTH);
        renderer.rectLine(location.x, location.y + Constants.PLAYER_LEG_HEIGHT,
                location.x - Constants.PLAYER_LEG_DISTANCE, location.y, Constants.PLAYER_LEG_WIDTH);

        //Hands
        renderer.rectLine(location.x, location.y + Constants.PLAYER_HAND_HEIGHT,
                location.x + Constants.PLAYER_HAND_DISTANCE, location.y + Constants.PLAYER_SIZE - Constants.PLAYER_HEAD_SIZE,
                Constants.PLAYER_HAND_WIDTH);
        renderer.rectLine(location.x, location.y + Constants.PLAYER_HAND_HEIGHT,
                location.x - Constants.PLAYER_HAND_DISTANCE, location.y + Constants.PLAYER_SIZE - Constants.PLAYER_HEAD_SIZE,
                Constants.PLAYER_HAND_WIDTH);
    }

    public void teleport(int screenX, int screenY) {
        Vector2 loc = game.getViewport().unproject(new Vector2(screenX, screenY));
        if (loc.x < 0)
            loc.x = 0;
        if (loc.x > Constants.WORLD_WIDTH)
            loc.x = Constants.WORLD_WIDTH;
        loc.y = 0;
        location.set(loc);
    }

    public void update(float deltaTime) {
        velocity.x *= (1f - Constants.PLAYER_MOVEMENT_DRAG * deltaTime);
        if (Config.control == ControlType.KEYBOARD) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                velocity.x -= Constants.PLAYER_BUTTON_MOVEMENT_SENSIBILITY * deltaTime;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                velocity.x += Constants.PLAYER_BUTTON_MOVEMENT_SENSIBILITY * deltaTime;
        }
        if (Config.control == ControlType.ACCELEROMETER)
            velocity.x += Constants.PLAYER_ACM_MOVEMENT_SENSIBILITY * deltaTime * Gdx.input.getAccelerometerY();

        velocity.clamp(0, Constants.PLAYER_MAX_VELOCITY);

        location.add(velocity);
        if (location.x < 0)
            location.x = 0;
        if (location.x > Constants.WORLD_WIDTH)
            location.x = Constants.WORLD_WIDTH;
    }
}
