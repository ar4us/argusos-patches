package app.argusos.extension.tiktok.settings.preference;

import android.content.Context;
import android.view.View;

import java.util.Map;

import app.argusos.extension.shared.Logger;
import app.argusos.extension.shared.settings.preference.ArgusOSAboutPreference;
import app.argusos.extension.tiktok.Utils;

@SuppressWarnings("deprecation")
public class ArgusOSTikTokAboutPreference extends ArgusOSAboutPreference {

    /**
     * Because resources cannot be added to TikTok,
     * these strings are copied from the shared strings.xml file.
     * <p>
     * Changes here must also be made in strings.xml
     */
    private final Map<String, String> aboutStrings = Map.of(
            "argusos_settings_about_links_body", "You are using ArgusOS Patches version <i>%s</i>",
            "argusos_settings_about_links_dev_header", "Note",
            "argusos_settings_about_links_dev_body", "This version is a pre-release and you may experience unexpected issues",
            "argusos_settings_about_links_header", "Official links"
    );

    public ArgusOSTikTokAboutPreference(Context context) {
        super(context);

        setTitle("About");
        setSummary("About ArgusOS");
    }

    @Override
    protected String getString(String key, Object ... args) {
        String format = aboutStrings.get(key);

        if (format == null) {
            Logger.printException(() -> "Unknown key: " + key);
            return "";
        }

        return String.format(format, args);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        Utils.setTitleAndSummaryColor(view);
    }
}
