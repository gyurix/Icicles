package gyurix.icicles.enums;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by GyuriX on 2017. 08. 18..
 */

public enum IcicleType {
    NORMAL {
        @Override
        public boolean matchSides() {
            return false;
        }

        @Override
        public double getChance() {
            return 0.8;
        }


        @Override
        public boolean matchGod() {
            return false;
        }

        @Override
        public Color[] getColors() {
            return Constants.ICE_COLOR;
        }
    }, HARD {
        @Override
        public double getChance() {
            return 0.15;
        }

        @Override
        public boolean matchGod() {
            return false;
        }

        @Override
        public Color[] getColors() {
            return Constants.HARD_ICE_COLOR;
        }
    }, COIN {
        @Override
        public double getChance() {
            return 0.05;
        }

        @Override
        public Color[] getColors() {
            return Constants.COIN_ICE_COLOR;
        }
    };

    public boolean matchSides() {
        return true;
    }

    public boolean matchGod() {
        return true;
    }

    public abstract double getChance();

    public abstract Color[] getColors();
}
