package app.argusos.extension.twitch.patches;

import app.argusos.extension.twitch.settings.Settings;

@SuppressWarnings("unused")
public class AutoClaimChannelPointsPatch {
    public static boolean shouldAutoClaim() {
        return Settings.AUTO_CLAIM_CHANNEL_POINTS.get();
    }
}
