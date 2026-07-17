package app.argusos.extension.music.patches;

import app.argusos.extension.music.settings.Settings;

@SuppressWarnings("unused")
public class HideGetPremiumPatch {

    /**
     * Injection point
     */
    public static boolean hideGetPremiumLabel() {
        return Settings.HIDE_GET_PREMIUM_LABEL.get();
    }
}
