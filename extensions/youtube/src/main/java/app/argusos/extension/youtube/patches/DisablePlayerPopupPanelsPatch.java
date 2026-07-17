package app.argusos.extension.youtube.patches;

import app.argusos.extension.youtube.settings.Settings;

@SuppressWarnings("unused")
public class DisablePlayerPopupPanelsPatch {
    /**
     * Injection point.
     */
    public static boolean disablePlayerPopupPanels() {
        return Settings.DISABLE_PLAYER_POPUP_PANELS.get();
    }
}
