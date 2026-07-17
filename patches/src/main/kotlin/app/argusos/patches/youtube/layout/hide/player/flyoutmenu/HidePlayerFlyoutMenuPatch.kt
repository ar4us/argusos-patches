package app.argusos.patches.youtube.layout.hide.player.flyoutmenu

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.shared.misc.litho.filter.addLithoFilter
import app.argusos.patches.youtube.misc.litho.filter.lithoFilterPatch
import app.argusos.patches.youtube.misc.playertype.playerTypeHookPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch

@Suppress("unused")
val hidePlayerFlyoutMenuItemsPatch = bytecodePatch(
    name = "Hide player flyout menu items",
    description = "Adds options to hide menu items that appear when pressing the gear icon in the video player.",
) {
    dependsOn(
        lithoFilterPatch,
        playerTypeHookPatch,
        settingsPatch,
        addResourcesPatch,
    )

    compatibleWith(
        "com.google.android.youtube"(
            "20.14.43",
            "20.21.37",
            "20.26.46",
            "20.31.42",
            "20.37.48",
            "20.40.45"
        ),
    )

    apply {
        val filterClassDescriptor =
            "Lapp/argusos/extension/youtube/patches/litho/PlayerFlyoutMenuItemsFilter;"

        addResources("youtube", "layout.hide.player.flyoutmenupanel.hidePlayerFlyoutMenuPatch")

        PreferenceScreen.PLAYER.addPreferences(
            PreferenceScreenPreference(
                key = "argusos_hide_player_flyout",
                preferences = setOf(
                    SwitchPreference("argusos_hide_player_flyout_captions"),
                    SwitchPreference("argusos_hide_player_flyout_listen_with_youtube_music"),
                    SwitchPreference("argusos_hide_player_flyout_help"),
                    SwitchPreference("argusos_hide_player_flyout_speed"),
                    SwitchPreference("argusos_hide_player_flyout_lock_screen"),
                    SwitchPreference(
                        key = "argusos_hide_player_flyout_audio_track",
                        tag = "app.argusos.extension.youtube.settings.preference.HideAudioFlyoutMenuPreference"
                    ),
                    SwitchPreference("argusos_hide_player_flyout_video_quality"),
                    SwitchPreference("argusos_hide_player_flyout_video_quality_footer"),
                    SwitchPreference("argusos_hide_player_flyout_additional_settings"),
                    SwitchPreference("argusos_hide_player_flyout_ambient_mode"),
                    SwitchPreference("argusos_hide_player_flyout_stable_volume"),
                    SwitchPreference("argusos_hide_player_flyout_loop_video"),
                    SwitchPreference("argusos_hide_player_flyout_sleep_timer"),
                    SwitchPreference("argusos_hide_player_flyout_watch_in_vr"),
                ),
            )
        )

        addLithoFilter(filterClassDescriptor)
    }
}
