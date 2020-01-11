package gyurix.icicles.enums;

import gyurix.icicles.data.Config;
import gyurix.icicles.entity.HUD;
import gyurix.icicles.screen.GameScreen;

/**
 * Created by GyuriX on 2017. 08. 16..
 */

public enum Difficulty {
    VERY_EASY {
        @Override
        public int getIcicleCount() {
            return 4;
        }

        @Override
        public float getBackgroundSpeed() {
            return 100f;
        }
    },
    EASY {
        @Override
        public int getIcicleCount() {
            return 7;
        }

        @Override
        public float getBackgroundSpeed() {
            return 200f;
        }
    }, MEDIUM {
        @Override
        public int getIcicleCount() {
            return 12;
        }

        @Override
        public float getBackgroundSpeed() {
            return 400f;
        }
    }, HARD {
        @Override
        public int getIcicleCount() {
            return 16;
        }

        @Override
        public float getBackgroundSpeed() {
            return 800f;
        }
    }, EXTREME {
        @Override
        public int getIcicleCount() {
            return 20;
        }

        @Override
        public float getBackgroundSpeed() {
            return 1600f;
        }
    };

    public abstract float getBackgroundSpeed();

    public HUD getNewHUD(GameScreen screen) {
        HUD hud=new HUD(screen,Config.hud.get(this));
        Config.hud.put(this,hud);
        return hud;
    }

    public abstract int getIcicleCount();
}
