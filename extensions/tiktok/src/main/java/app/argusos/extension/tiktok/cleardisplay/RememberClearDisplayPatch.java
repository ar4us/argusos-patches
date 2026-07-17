package app.argusos.extension.tiktok.cleardisplay;

import app.argusos.extension.tiktok.settings.Settings;

@SuppressWarnings("unused")
public class RememberClearDisplayPatch {
    public static boolean getClearDisplayState() {
        return Settings.CLEAR_DISPLAY.get();
    }
    public static void rememberClearDisplayState(boolean newState) {
        Settings.CLEAR_DISPLAY.save(newState);
    }
}
