package app.argusos.extension.youtube.patches;

import app.argusos.extension.youtube.settings.Settings;

@SuppressWarnings("unused")
public class DisableAutoCaptionsPatch {

    private static volatile boolean captionsButtonStatus;

    /**
     * Injection point.
     */
    public static boolean disableAutoCaptions() {
        return Settings.DISABLE_AUTO_CAPTIONS.get() && !captionsButtonStatus;
    }

    /**
     * Injection point.
     */
    public static void setCaptionsButtonStatus(boolean status) {
        captionsButtonStatus = status;
    }
}
