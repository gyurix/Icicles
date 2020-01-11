package gyurix.icicles.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import gyurix.icicles.enums.Constants;
import gyurix.icicles.enums.ItemType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by GyuriX on 2017. 08. 24..
 */

@Getter
@Setter
public class Level {
    private final int id;
    private ArrayList<ItemType> allowedItems = new ArrayList<>();
    private TreeMap<Float, Texture> bg = new TreeMap<>();
    private HashMap<ItemType, Integer> collect = new HashMap<>();
    private float duration;
    private float goal;
    private Vector2 location;
    private float iconSize;
    private int minIcicles, maxIcicles;

    public Level(int id) {
        this.id = id;
    }
    public Color getOutlineColor(){
        return Constants.LEVEL_CIRCLE_OUTLINE_COLOR;
    }
    public Color getInnerColor(){
        return Constants.LEVEL_CIRCLE_INNER_COLOR;
    }
    public Color getTextColor(){
        return Constants.LEVEL_CIRCLE_OUTLINE_COLOR;
    }
    public float getTextScale(){
        return iconSize/64f;
    }
    public void fixIcicleCounts() {
        if (maxIcicles < minIcicles) {
            maxIcicles += minIcicles;
            minIcicles = maxIcicles - minIcicles;
            maxIcicles -= minIcicles;
        }
        if (minIcicles < 5)
            minIcicles = 5;
        if (minIcicles + 3 > maxIcicles)
            maxIcicles = minIcicles + 3;
    }

    private boolean isCompleted(float elapsedTime, HashMap<ItemType, Integer> collected) {
        if (collect.isEmpty())
            return elapsedTime >= duration;
        for (Map.Entry<ItemType, Integer> e : collect.entrySet()) {
            Integer am = collected.get(e.getKey());
            if (am == null || am < e.getValue())
                return false;
        }
        return true;
    }

    private boolean isFailed(float elapsedTime) {
        return !(elapsedTime < duration) && collect.isEmpty();
    }

    public void setIconSize(float iconSize) {
        this.iconSize = iconSize;
    }
}
