package gyurix.icicles.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import gyurix.icicles.IciclesGame;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void createWakeLock(boolean use) {
        super.createWakeLock(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useWakelock = true;
        config.numSamples = 2;
        initialize(new IciclesGame(), config);
    }

    @Override
    public void onBackPressed() {
    }
}
