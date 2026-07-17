package app.argusos.extension.twitch.patches;

import app.argusos.extension.twitch.settings.Settings;

@SuppressWarnings("unused")
public class AudioAdsPatch {
    public static boolean shouldBlockAudioAds() {
        return Settings.BLOCK_AUDIO_ADS.get();
    }
}
