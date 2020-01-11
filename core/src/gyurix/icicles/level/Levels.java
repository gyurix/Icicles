package gyurix.icicles.level;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import gyurix.icicles.IciclesGame;
import gyurix.icicles.enums.Constants;

import static gyurix.icicles.enums.Constants.WORLD_HEIGHT;
import static gyurix.icicles.enums.Constants.WORLD_WIDTH;

/**
 * Created by GyuriX on 2017. 08. 24
 */

public class Levels {
    private static TreeMap<Float, Level> levels = new TreeMap<>();
    private static final int[] xcoords = new int[]{3, 5, 7, 9, 11, 13, 15, 17, 20, 21, 21, 20, 17, 15, 13, 11, 9, 7, 5, 7, 9, 11, 13, 15, 17, 15, 13, 11, 9, 7, 5, 2, 1, 1, 1};
    private static final int[] ycoords = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 2, 5, 9, 12, 13, 13, 13, 13, 13, 13, 11, 9, 9, 9, 9, 9, 7, 5, 5, 5, 5, 5, 5, 6, 9, 11, 13};

    public static void init() {
        float y = 0;
        for (int id = 0; id < 500; ++id) {
            float idM = (id + 50f) / 50f;
            Level l = new Level(id);
            l.setDuration(id % 3 * idM * 15f);
            l.setBg(IciclesGame.bgs);
            l.setMinIcicles((int) (5 * idM));
            l.setMaxIcicles((int) (8 * idM));
            l.setIconSize((id % 25 == 24 ? 1.8f : id % 10 == 9 ? 1.5f : id % 5 == 4 ? 1.2f : 1f) * Constants.LEVEL_CIRCLE_RADIUS);
            l.fixIcicleCounts();
            int x = getXCoord(id);
            if (id < 498)
                y = ycoords[id % ycoords.length] * WORLD_HEIGHT / 15f + (id / ycoords.length) * WORLD_HEIGHT + 20f+id * 0.01f;
            else
                y+=0.01f;
            l.setLocation(new Vector2(x, y));
            levels.put(y, l);
        }
    }

    private static int getXCoord(int id) {
        return (int) ((id < 498 ? xcoords[id % xcoords.length] : id == 498 ? 19 : 21.5f) * WORLD_WIDTH / 23f);
    }


    public static ArrayList<Level> get(float y) {
        ArrayList<Level> list = new ArrayList<>();
        float max = y + WORLD_HEIGHT + Constants.LEVEL_CIRCLE_RADIUS + Constants.LEVEL_CIRCLE_OUTLINE_WIDTH;
        y-=Constants.LEVEL_CIRCLE_RADIUS + Constants.LEVEL_CIRCLE_OUTLINE_WIDTH;
        while (true) {
            Map.Entry<Float, Level> next = levels.higherEntry(y);
            if (next == null || next.getKey() > max)
                return list;
            y = next.getKey();
            list.add(next.getValue());
        }
    }
}
