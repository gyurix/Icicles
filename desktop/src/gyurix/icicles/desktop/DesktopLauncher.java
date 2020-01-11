package gyurix.icicles.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import gyurix.icicles.IciclesGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.addIcon("ic_launcher_mini.png", Files.FileType.Internal);
        config.samples = 3;
        new LwjglApplication(new IciclesGame(), config);
    }
}
