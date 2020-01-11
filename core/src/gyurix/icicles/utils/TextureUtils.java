package gyurix.icicles.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gyurix.icicles.enums.Constants;

import static gyurix.icicles.enums.Constants.WORLD_WIDTH;

/**
 * Created by GyuriX on 2017. 08. 26..
 */

public class TextureUtils {
    public static void draw(Texture texture, SpriteBatch batch, float size, float y, float height) {
        float ratio = height / texture.getHeight();
        for (int yfix = 0; yfix < size; ++yfix) {
            batch.setColor(1, 1, 1, 1-yfix/size);
            batch.draw(texture, 0, y + size-yfix, 0, 0, Constants.WORLD_WIDTH,
                    1, 1, 1, 0,
                    0, (int) (texture.getHeight() - (size-yfix)/ratio), texture.getWidth(), 1,
                    false, false);
        }
        batch.setColor(1, 1, 1, 1);
        batch.draw(texture, 0, y + size, 0, 0, Constants.WORLD_WIDTH,
                height - size, 1, 1, 0,
                0, 0, texture.getWidth(), (int) (texture.getHeight() - size/ratio),
                false, false);
    }
}
