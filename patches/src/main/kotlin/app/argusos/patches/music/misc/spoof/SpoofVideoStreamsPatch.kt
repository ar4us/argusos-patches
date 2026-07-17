package app.argusos.patches.music.misc.spoof

import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.music.misc.extension.sharedExtensionPatch
import app.argusos.patches.music.misc.gms.musicActivityOnCreateMethod
import app.argusos.patches.music.misc.settings.PreferenceScreen
import app.argusos.patches.music.misc.settings.settingsPatch
import app.argusos.patches.music.playservice.*
import app.argusos.patches.shared.misc.settings.preference.ListPreference
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.shared.misc.spoof.spoofVideoStreamsPatch

val spoofVideoStreamsPatch = spoofVideoStreamsPatch(
    extensionClassDescriptor = "Lapp/argusos/extension/music/patches/spoof/SpoofVideoStreamsPatch;",
    getMainActivityOnCreateMethod = { musicActivityOnCreateMethod },
    fixMediaFetchHotConfig = { is_7_16_or_greater },
    fixMediaFetchHotConfigAlternative = { is_8_11_or_greater && !is_8_15_or_greater },
    fixParsePlaybackResponseFeatureFlag = { is_7_33_or_greater },

    block = {
        dependsOn(
            sharedExtensionPatch,
            settingsPatch,
            addResourcesPatch,
            versionCheckPatch,
            userAgentClientSpoofPatch
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

    executeBlock = {
        addResources("music", "misc.fix.playback.spoofVideoStreamsPatch")

        PreferenceScreen.MISC.addPreferences(
            PreferenceScreenPreference(
                key = "argusos_spoof_video_streams_screen",
                sorting = PreferenceScreenPreference.Sorting.UNSORTED,
                preferences = setOf(
                    SwitchPreference("argusos_spoof_video_streams"),
                    ListPreference("argusos_spoof_video_streams_client_type"),
                )
            )
        )
    }
)
