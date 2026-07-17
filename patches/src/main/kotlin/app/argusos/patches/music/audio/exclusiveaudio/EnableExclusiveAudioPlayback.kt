package app.argusos.patches.music.audio.exclusiveaudio

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.music.misc.extension.sharedExtensionPatch
import app.argusos.patches.music.misc.settings.settingsPatch
import app.argusos.util.returnEarly

@Suppress("unused")
val enableExclusiveAudioPlaybackPatch = bytecodePatch(
    name = "Enable exclusive audio playback",
    description = "Enables the option to play audio without video.",
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
        allowExclusiveAudioPlaybackMethod.returnEarly(true)
    }
}
