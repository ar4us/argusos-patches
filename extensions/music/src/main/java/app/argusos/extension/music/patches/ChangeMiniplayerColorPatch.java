package app.argusos.extension.music.patches;

import app.argusos.extension.music.settings.Settings;

@SuppressWarnings("unused")
public class ChangeMiniplayerColorPatch {

    /**
     * Injection point
     */
    public static boolean changeMiniplayerColor() {
        return Settings.CHANGE_MINIPLAYER_COLOR.get();
    }
}
