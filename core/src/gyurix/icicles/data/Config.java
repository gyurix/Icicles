package gyurix.icicles.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import gyurix.icicles.entity.HUD;
import gyurix.icicles.entity.Sounds;
import gyurix.icicles.enums.ControlType;
import gyurix.icicles.enums.Difficulty;
import gyurix.icicles.enums.ItemType;

/**
 * Created by GyuriX on 2017. 08. 17..
 */

public class Config {
    public static BigDecimal coins;
    public static boolean music, sound;
    public static ControlType control;
    public static Difficulty difficulty;
    private static HashMap<ItemType, BigDecimal> items = new HashMap<>();
    public static EnumMap<Difficulty, HUD> hud = new EnumMap<>(Difficulty.class);
    private static Preferences pref;

    private static String decrypt(String key, Object def) throws Throwable {
        String data = pref.getString(key, null);
        if (data == null)
            return def.toString();
        byte[] d = Base64Coder.decode(data);
        byte[] dkey = getEncDecKey(key);
        StreamUtils.OptimizedByteArrayOutputStream bos = new StreamUtils.OptimizedByteArrayOutputStream(data.length());
        int len = d.length;
        int dklen = dkey.length;
        for (int i = 0; i < len; ++i)
            bos.write(d[i] - dkey[i % dklen]);
        return bos.toString("UTF-8");
    }

    public static int getItemCount(ItemType item) {
        BigDecimal c = items.get(item);
        return c == null ? 0 : c.intValue();
    }


    private static void encrypt(String key, Object value) throws Throwable {
        byte[] d = value.toString().getBytes("UTF-8");
        byte[] dkey = getEncDecKey(key);
        StreamUtils.OptimizedByteArrayOutputStream bos = new StreamUtils.OptimizedByteArrayOutputStream(d.length);
        int len = d.length;
        int dklen = dkey.length;
        for (int i = 0; i < len; ++i)
            bos.write((byte) (d[i] + dkey[i % dklen]));
        pref.putString(key, String.valueOf(Base64Coder.encode(bos.getBuffer())));
    }


    private static byte[] getEncDecKey(String key) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        char[] ar = key.toCharArray();
        String enc = "an^uk9O!K3tRz3141d7FIcPA";
        int len = enc.length();
        for (int i = 0; i < ar.length; ++i) {
            bos.write(ar[i]);
            bos.write(enc.charAt(i % len));
        }
        return bos.toByteArray();
    }

    public static boolean load() {
        pref = Gdx.app.getPreferences("config");
        if (pref.getInteger("version", 0) != 1) {
            pref.clear();
            pref.putInteger("version", 1);
        }
        try {
            for (Difficulty df : Difficulty.values())
                hud.put(df, new HUD(df, Integer.valueOf(decrypt(df.name().toLowerCase() + ".maxScore", "0"))));
            Config.control = ControlType.valueOf(decrypt("control", "DRAG_AND_CLICK"));
            Config.difficulty = Difficulty.valueOf(decrypt("difficulty", "MEDIUM"));
            Config.coins = new BigDecimal(Integer.valueOf(decrypt("coins", 0)));
            Config.music = Boolean.valueOf(decrypt("music", true));
            Config.sound = Boolean.valueOf(decrypt("sound", true));
            itemsFromString(decrypt("items", ""));
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String itemsToString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<ItemType, BigDecimal> et : items.entrySet()) {
            sb.append('\n').append(et.getKey().name()).append(' ').append(et.getValue());
        }
        return sb.length() == 0 ? "" : sb.substring(1);
    }

    private static void itemsFromString(String s) {
        if (s.isEmpty())
            return;
        for (String d : s.split("\n")) {
            String[] e = d.split(" ", 2);
            items.put(ItemType.valueOf(e[0]), new BigDecimal(e[1]));
        }
    }

    public static boolean save() {
        try {
            for (HUD h : hud.values())
                encrypt(h.getDifficulty().name().toLowerCase() + ".maxScore", h.getMaxScore());
            encrypt("control", control.name());
            encrypt("difficulty", difficulty.name());
            encrypt("coins", coins);
            encrypt("music", music);
            encrypt("sound", sound);
            encrypt("items", itemsToString());
            pref.flush();
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void buyItem(ItemType itemType) {
        int coin = coins.intValue();
        if (coin < itemType.getPrice()) {
            if (Config.sound)
                Sounds.failed.play();
            return;
        }
        coins = new BigDecimal(coin - itemType.getPrice());
        items.put(itemType, new BigDecimal(getItemCount(itemType) + 1));
        if (Config.sound)
            Sounds.buy.play();
    }

    public static boolean useItem(ItemType it) {
        int c=getItemCount(it);
        if (c==0)
            return false;
        items.put(it,new BigDecimal(c-1));
        return true;
    }
}
