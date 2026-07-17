package app.argusos.extension.twitch.patches;

import app.argusos.extension.twitch.settings.Settings;

@SuppressWarnings("unused")
public class DebugModePatch {
    public static boolean isDebugModeEnabled() {
        return Settings.TWITCH_DEBUG_MODE.get();
    }
}
