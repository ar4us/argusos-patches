package app.argusos.patches.youtube.layout.buttons.action

import app.revanced.patcher.patch.resourcePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.mapping.resourceMappingPatch
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.shared.misc.litho.filter.addLithoFilter
import app.argusos.patches.youtube.misc.litho.filter.lithoFilterPatch
import app.argusos.patches.youtube.misc.playservice.is_20_22_or_greater
import app.argusos.patches.youtube.misc.playservice.versionCheckPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import java.util.logging.Logger

@Suppress("unused")
val hideVideoActionButtonsPatch = resourcePatch(
    name = "Hide video action buttons",
    description = "Adds options to hide action buttons (such as the Download button) under videos. " +
            "Patching version 20.21.37 or lower can hide more player button types."
) {
    dependsOn(
        resourceMappingPatch,
        lithoFilterPatch,
        addResourcesPatch,
        versionCheckPatch,
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
        addResources("youtube", "layout.buttons.action.hideButtonsPatch")

        val preferences = mutableSetOf(
            SwitchPreference("argusos_disable_like_subscribe_glow"),
            SwitchPreference("argusos_hide_download_button"),
            SwitchPreference("argusos_hide_like_dislike_button"),
            SwitchPreference("argusos_hide_comments_button"),
            SwitchPreference("argusos_hide_save_button"),
            SwitchPreference("argusos_hide_remix_button"),
            SwitchPreference("argusos_hide_share_button"),

            )

        // 20.22+ cannot hide all action buttons because of buffer changes.
        if (!is_20_22_or_greater) {
            preferences.addAll(
                listOf(
                    SwitchPreference("argusos_hide_hype_button"),
                    SwitchPreference("argusos_hide_ask_button"),
                    SwitchPreference("argusos_hide_clip_button"),
                    SwitchPreference("argusos_hide_promote_button"),
                    SwitchPreference("argusos_hide_report_button"),
                    SwitchPreference("argusos_hide_shop_button"),
                    SwitchPreference("argusos_hide_stop_ads_button"),
                    SwitchPreference("argusos_hide_thanks_button"),
                ),
            )
        }

        PreferenceScreen.PLAYER.addPreferences(
            PreferenceScreenPreference(
                "argusos_hide_buttons_screen",
                preferences = preferences,
            ),
        )

        addLithoFilter("Lapp/argusos/extension/youtube/patches/litho/VideoActionButtonsFilter;")
    }
}
