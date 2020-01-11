package gyurix.icicles.entity;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import static gyurix.icicles.IciclesGame.am;

/**
 * Created by GyuriX on 2017. 08. 18..
 */

public class Sounds {
    public static Sound achievement, coinpickup, icebreak, eat, failed, buy;
    public static Music game, menu;

    public static void cache() {
        achievement = am.get("sounds/achievement.mp3");
        buy = am.get("sounds/buy.mp3");
        coinpickup = am.get("sounds/coinpickup.mp3");
        eat = am.get("sounds/eat.mp3");
        failed = am.get("sounds/failed.mp3");
        game = am.get("music/game.mp3");
        game.setLooping(true);
        icebreak = am.get("sounds/icebreak.mp3");
        menu = am.get("music/menu.mp3");
        menu.setLooping(true);
    }

    public static void load() {
        am.load("music/game.mp3", Music.class);
        am.load("music/menu.mp3", Music.class);
        am.load("sounds/achievement.mp3", Sound.class);
        am.load("sounds/buy.mp3", Sound.class);
        am.load("sounds/coinpickup.mp3", Sound.class);
        am.load("sounds/eat.mp3", Sound.class);
        am.load("sounds/failed.mp3", Sound.class);
        am.load("sounds/icebreak.mp3", Sound.class);

    }
}
