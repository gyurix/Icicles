package gyurix.icicles.enums;

import com.badlogic.gdx.graphics.Texture;

import static gyurix.icicles.IciclesGame.am;
import static gyurix.icicles.IciclesGame.lang;

/**
 * Created by GyuriX on 2017. 08. 22..
 */

public enum ItemType {
    APPLE {
        @Override
        public int getPrice() {
            return 50;
        }

    }, BLUEBERRY {
        @Override
        public int getPrice() {
            return 10;
        }

        @Override
        public long getEffectLength() {
            return 15000L;
        }

        @Override
        public float getPower() {
            return 0.2f;
        }
    }, STRAWBERRY {
        @Override
        public int getPrice() {
            return 15;
        }

        @Override
        public long getEffectLength() {
            return 15000L;
        }
    };

    public float getPower() {
        return 0f;
    }

    public abstract int getPrice();
    public long getEffectLength(){
        return 0L;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
    public String getName(){
        return lang.get("item_"+this);
    }
    public String getDescription(){
        return lang.get("item_"+this+"_desc");
    }

    public void loadTexture() {
        am.load("items/" + this + ".png", Texture.class);
    }

    public Texture getTexture() {
        return am.get("items/" + this + ".png");
    }
}
