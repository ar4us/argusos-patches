package app.argusos.extension.twitch.patches;

import app.argusos.extension.twitch.settings.Settings;

@SuppressWarnings("unused")
public class VideoAdsPatch {
    public static boolean shouldBlockVideoAds() {
        return Settings.BLOCK_VIDEO_ADS.get();
    }
}