package app.argusos.patches.youtube.video.audio

import app.revanced.patcher.patch.BytecodePatchContext
import app.argusos.patches.shared.misc.audio.forceOriginalAudioPatch
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.playservice.is_20_07_or_greater
import app.argusos.patches.youtube.misc.playservice.versionCheckPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.patches.youtube.shared.mainActivityOnCreateMethod

@Suppress("unused")
val forceOriginalAudioPatch = forceOriginalAudioPatch(
    block = {
        dependsOn(
            sharedExtensionPatch,
            settingsPatch,
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
    },
    fixUseLocalizedAudioTrackFlag = { is_20_07_or_greater },
    getMainActivityOnCreateMethod = BytecodePatchContext::mainActivityOnCreateMethod::get,
    subclassExtensionClassDescriptor = "Lapp/argusos/extension/youtube/patches/ForceOriginalAudioPatch;",
    preferenceScreen = PreferenceScreen.VIDEO,
)
