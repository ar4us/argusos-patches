package app.argusos.extension.twitch.settings;

import app.argusos.extension.shared.settings.BooleanSetting;
import app.argusos.extension.shared.settings.BaseSettings;
import app.argusos.extension.shared.settings.StringSetting;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Settings extends BaseSettings {
    /* Ads */
    public static final BooleanSetting BLOCK_VIDEO_ADS = new BooleanSetting("argusos_block_video_ads", TRUE);
    public static final BooleanSetting BLOCK_AUDIO_ADS = new BooleanSetting("argusos_block_audio_ads", TRUE);
    public static final StringSetting BLOCK_EMBEDDED_ADS = new StringSetting("argusos_block_embedded_ads", "luminous");

    /* Chat */
    public static final StringSetting SHOW_DELETED_MESSAGES = new StringSetting("argusos_show_deleted_messages", "cross-out");
    public static final BooleanSetting AUTO_CLAIM_CHANNEL_POINTS = new BooleanSetting("argusos_auto_claim_channel_points", TRUE);

    /* Misc */
    /**
     * Not to be confused with {@link BaseSettings#DEBUG}.
     */
    public static final BooleanSetting TWITCH_DEBUG_MODE = new BooleanSetting("argusos_twitch_debug_mode", FALSE, true);
}
