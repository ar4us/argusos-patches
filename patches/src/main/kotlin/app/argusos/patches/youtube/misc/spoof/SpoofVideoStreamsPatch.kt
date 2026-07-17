package app.argusos.patches.youtube.misc.spoof

import app.revanced.patcher.patch.BytecodePatchContext
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.shared.misc.settings.preference.ListPreference
import app.argusos.patches.shared.misc.settings.preference.NonInteractivePreference
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.shared.misc.spoof.spoofVideoStreamsPatch
import app.argusos.patches.youtube.misc.playservice.is_19_34_or_greater
import app.argusos.patches.youtube.misc.playservice.is_20_03_or_greater
import app.argusos.patches.youtube.misc.playservice.is_20_10_or_greater
import app.argusos.patches.youtube.misc.playservice.is_20_14_or_greater
import app.argusos.patches.youtube.misc.playservice.versionCheckPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.patches.youtube.shared.mainActivityOnCreateMethod

val spoofVideoStreamsPatch = spoofVideoStreamsPatch(
    extensionClassDescriptor = "Lapp/argusos/extension/youtube/patches/spoof/SpoofVideoStreamsPatch;",
    getMainActivityOnCreateMethod = BytecodePatchContext::mainActivityOnCreateMethod::get,
    fixMediaFetchHotConfig = {
        is_19_34_or_greater
    },
    fixMediaFetchHotConfigAlternative = {
        // In 20.14 the flag was merged with 20.03 start playback flag.
        is_20_10_or_greater && !is_20_14_or_greater
    },
    fixParsePlaybackResponseFeatureFlag = {
        is_20_03_or_greater
    },

    block = {
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

        dependsOn(
            userAgentClientSpoofPatch,
            settingsPatch,
            versionCheckPatch,
        )
    },

    executeBlock = {
        addResources("youtube", "misc.fix.playback.spoofVideoStreamsPatch")

        PreferenceScreen.MISC.addPreferences(
            PreferenceScreenPreference(
                key = "argusos_spoof_video_streams_screen",
                sorting = PreferenceScreenPreference.Sorting.UNSORTED,
                preferences = setOf(
                    SwitchPreference("argusos_spoof_video_streams"),
                    ListPreference("argusos_spoof_video_streams_client_type"),
                    NonInteractivePreference(
                        // Requires a key and title but the actual text is chosen at runtime.
                        key = "argusos_spoof_video_streams_about",
                        summaryKey = null,
                        tag = "app.argusos.extension.youtube.settings.preference.SpoofVideoStreamsSideEffectsPreference",
                    ),
                    SwitchPreference("argusos_spoof_video_streams_av1"),
                    SwitchPreference("argusos_spoof_video_streams_stats_for_nerds"),
                ),
            ),
        )
    },
)
