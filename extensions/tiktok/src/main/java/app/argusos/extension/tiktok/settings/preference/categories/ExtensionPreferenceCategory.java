package app.argusos.extension.tiktok.settings.preference.categories;

import android.content.Context;
import android.preference.PreferenceScreen;

import app.argusos.extension.shared.settings.BaseSettings;
import app.argusos.extension.tiktok.settings.preference.ArgusOSTikTokAboutPreference;
import app.argusos.extension.tiktok.settings.preference.TogglePreference;

@SuppressWarnings("deprecation")
public class ExtensionPreferenceCategory extends ConditionalPreferenceCategory {
    public ExtensionPreferenceCategory(Context context, PreferenceScreen screen) {
        super(context, screen);
        setTitle("Miscellaneous");
    }

    @Override
    public boolean getSettingsStatus() {
        return true;
    }

    @Override
    public void addPreferences(Context context) {
        addPreference(new ArgusOSTikTokAboutPreference(context));

        addPreference(new TogglePreference(context,
                "Sanitize sharing links",
                "Remove tracking parameters from shared links.",
                BaseSettings.SANITIZE_SHARING_LINKS
        ));

        addPreference(new TogglePreference(context,
                "Enable debug log",
                "Show extension debug log.",
                BaseSettings.DEBUG
        ));
    }
}
