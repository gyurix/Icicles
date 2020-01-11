package gyurix.icicles.entity;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import gyurix.icicles.enums.ItemType;

import static gyurix.icicles.IciclesGame.am;

/**
 * Created by GyuriX on 2017. 08. 18..
 */

public class Icons {
    public static Texture close;
    public static Texture coin;
    public static Texture difficulty, difficulty2;
    public static Texture distance;
    public static Texture highscore;
    public static Texture hp;
    public static Texture melted;
    public static Texture music, sound, music2, sound2;
    public static Texture pause, mainmenu, reset, gameover;
    public static Texture shopBg, shopItem, shopItem2, pauseBg, snow, buy;
    public static Texture time;

    public static void cache() {
        buy = am.get("icon/buy.png");
        close = am.get("icon/close.png");
        coin = am.get("icon/coin.png");
        difficulty = am.get("icon/difficulty.png");
        difficulty2 = am.get("icon/difficulty2.png");
        distance = am.get("icon/distance.png");
        highscore = am.get("icon/highscore.png");
        hp = am.get("icon/hp.png");
        melted = am.get("icon/melted.png");
        music = am.get("icon/music.png");
        music2 = am.get("icon/music2.png");
        pause = am.get("icon/pause.png");
        mainmenu = am.get("icon/mainmenu.png");
        reset = am.get("icon/reset.png");
        gameover = am.get("icon/gameover.png");
        shopBg = am.get("bg/shop.jpg");
        pauseBg = am.get("bg/pausebg.jpg");
        shopItem = am.get("bg/shopitem.png");
        shopItem2 = am.get("bg/shopitem2.png");
        snow = am.get("bg/snow.png");
        sound = am.get("icon/sound.png");
        sound2 = am.get("icon/sound2.png");
        time = am.get("icon/time.png");

    }

    public static void load() {
        am.load("icon/buy.png", Texture.class);
        am.load("icon/close.png", Texture.class);
        am.load("icon/coin.png", Texture.class);
        am.load("icon/difficulty.png", Texture.class);
        am.load("icon/difficulty2.png", Texture.class);
        am.load("icon/distance.png", Texture.class);
        am.load("icon/gameover.png", Texture.class);
        am.load("icon/highscore.png", Texture.class);
        am.load("icon/hp.png", Texture.class);
        am.load("icon/mainmenu.png", Texture.class);
        am.load("icon/melted.png", Texture.class);
        am.load("icon/music.png", Texture.class);
        am.load("icon/music2.png", Texture.class);
        am.load("icon/pause.png", Texture.class);
        am.load("icon/reset.png", Texture.class);
        am.load("icon/sound.png", Texture.class);
        am.load("icon/sound2.png", Texture.class);
        am.load("icon/time.png", Texture.class);
        am.load("bg/shop.jpg", Texture.class);
        am.load("bg/shopitem.png", Texture.class);
        am.load("bg/shopitem2.png", Texture.class);
        am.load("bg/pausebg.jpg", Texture.class);
        am.load("bg/snow.png", Texture.class);
        for (ItemType it : ItemType.values())
            it.loadTexture();
    }
}
