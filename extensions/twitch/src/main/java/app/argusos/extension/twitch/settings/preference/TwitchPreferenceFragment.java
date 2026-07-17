package app.argusos.extension.twitch.settings.preference;

import app.argusos.extension.shared.Logger;
import app.argusos.extension.shared.settings.preference.AbstractPreferenceFragment;
import app.argusos.extension.twitch.settings.Settings;

/**
 * Preference fragment for ArgusOS settings.
 */
public class TwitchPreferenceFragment extends AbstractPreferenceFragment {

    @Override
    protected void initialize() {
        super.initialize();

        // Do anything that forces this apps Settings bundle to load.
        if (Settings.BLOCK_VIDEO_ADS.get()) {
            Logger.printDebug(() -> "Block video ads enabled"); // Any statement that references the app settings.
        }
    }
}
