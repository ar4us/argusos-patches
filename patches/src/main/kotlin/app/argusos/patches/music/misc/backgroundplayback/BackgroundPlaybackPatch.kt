package app.argusos.patches.music.misc.backgroundplayback

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.music.misc.extension.sharedExtensionPatch
import app.argusos.patches.music.misc.settings.settingsPatch
import app.argusos.util.returnEarly

@Suppress("unused")
val removeBackgroundPlaybackRestrictionsPatch = bytecodePatch(
    name = "Remove background playback restrictions",
    description = "Removes restrictions on background playback, including playing kids videos in the background.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
    )

    compatibleWith(
        "com.google.android.apps.youtube.music"(
            "7.29.52",
            "8.10.52",
            "8.37.56",
            "8.40.54",
        ),
    )

    apply {
        kidsBackgroundPlaybackPolicyControllerMethod.returnEarly()
        backgroundPlaybackDisableMethod.returnEarly(true)
    }
}
