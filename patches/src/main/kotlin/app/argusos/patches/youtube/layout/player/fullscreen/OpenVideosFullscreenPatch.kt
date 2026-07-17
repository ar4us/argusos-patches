package app.argusos.patches.youtube.layout.player.fullscreen

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.util.returnEarly

@Suppress("unused")
val openVideosFullscreenPatch = bytecodePatch(
    name = "Open videos fullscreen",
    description = "Adds an option to open videos in full screen portrait mode.",
) {
    dependsOn(
        openVideosFullscreenHookPatch,
        settingsPatch,
        addResourcesPatch,
    )

    compatibleWith(
        "com.google.android.youtube"(
            "19.47.53",
            "20.14.43",
            "20.21.37",
            "20.26.46",
            "20.31.42",
            "20.37.48",
            "20.40.45"
        ),
    )

    apply {
        addResources("youtube", "layout.player.fullscreen.openVideosFullscreen")

        PreferenceScreen.PLAYER.addPreferences(
            SwitchPreference("argusos_open_videos_fullscreen_portrait"),
        )

        // Enable the logic for the user Setting to open regular videos fullscreen.
        openVideosFullscreenHookPatchExtensionMethod.returnEarly(true)
    }
}
