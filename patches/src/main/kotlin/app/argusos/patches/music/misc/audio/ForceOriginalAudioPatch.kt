package app.argusos.patches.music.misc.audio

import app.argusos.patches.music.misc.extension.sharedExtensionPatch
import app.argusos.patches.music.misc.settings.PreferenceScreen
import app.argusos.patches.music.misc.settings.settingsPatch
import app.argusos.patches.music.playservice.is_8_05_or_greater
import app.argusos.patches.music.playservice.versionCheckPatch
import app.argusos.patches.music.shared.mainActivityOnCreateMethod
import app.argusos.patches.shared.misc.audio.forceOriginalAudioPatch

@Suppress("unused")
val forceOriginalAudioPatch = forceOriginalAudioPatch(
    block = {
        dependsOn(
            sharedExtensionPatch,
            settingsPatch,
            versionCheckPatch
        )

        compatibleWith(
            "com.google.android.apps.youtube.music"(
                "7.29.52",
                "8.10.52",
                "8.37.56",
                "8.40.54"
            )
        )
    },
    fixUseLocalizedAudioTrackFlag = { is_8_05_or_greater },
    getMainActivityOnCreateMethod = { mainActivityOnCreateMethod },
    subclassExtensionClassDescriptor = "Lapp/argusos/extension/music/patches/ForceOriginalAudioPatch;",
    preferenceScreen = PreferenceScreen.MISC,
)
