package app.argusos.extension.shared.settings;

import static app.argusos.extension.shared.settings.Setting.parent;
import static java.lang.Boolean.FALSE;

public class YouTubeAndMusicSettings extends BaseSettings {
    // Custom filter
    public static final BooleanSetting CUSTOM_FILTER = new BooleanSetting("argusos_custom_filter", FALSE);
    public static final StringSetting CUSTOM_FILTER_STRINGS = new StringSetting("argusos_custom_filter_strings", "", true, parent(CUSTOM_FILTER));

    // Miscellaneous
    public static final BooleanSetting DEBUG_PROTOCOLBUFFER = new BooleanSetting("argusos_debug_protocolbuffer", FALSE, false,
            "argusos_debug_protocolbuffer_user_dialog_message", parent(BaseSettings.DEBUG));
}
