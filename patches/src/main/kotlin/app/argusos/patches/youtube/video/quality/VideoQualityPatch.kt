package app.argusos.patches.youtube.video.quality

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.shared.misc.settings.preference.BasePreference
import app.argusos.patches.shared.misc.settings.preference.PreferenceCategory
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference.Sorting
import app.argusos.patches.youtube.misc.settings.PreferenceScreen

/**
 * Video quality settings. Used to organize all speed related settings together.
 */
internal val settingsMenuVideoQualityGroup = mutableSetOf<BasePreference>()

@Suppress("unused")
val videoQualityPatch = bytecodePatch(
    name = "Video quality",
    description = "Adds options to set default video qualities and always use the advanced video quality menu.",
) {
    dependsOn(
        rememberVideoQualityPatch,
        advancedVideoQualityMenuPatch,
        hidePremiumVideoQualityPatch,
        videoQualityDialogButtonPatch,
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
        PreferenceScreen.VIDEO.addPreferences(
            // Keep the preferences organized together.
            PreferenceCategory(
                key = "argusos_01_video_key", // Dummy key to force the quality preferences first.
                titleKey = null,
                sorting = Sorting.UNSORTED,
                tag = "app.argusos.extension.shared.settings.preference.NoTitlePreferenceCategory",
                preferences = settingsMenuVideoQualityGroup,
            ),
        )
    }
}
