package app.argusos.patches.youtube.layout.thumbnails

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.ListPreference
import app.argusos.patches.shared.misc.settings.preference.NonInteractivePreference
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.shared.misc.settings.preference.TextPreference
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.imageurlhook.addImageURLErrorCallbackHook
import app.argusos.patches.youtube.misc.imageurlhook.addImageURLHook
import app.argusos.patches.youtube.misc.imageurlhook.addImageURLSuccessCallbackHook
import app.argusos.patches.youtube.misc.imageurlhook.cronetImageURLHookPatch
import app.argusos.patches.youtube.misc.navigation.navigationBarHookPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/patches/AlternativeThumbnailsPatch;"

@Suppress("unused")
val alternativeThumbnailsPatch = bytecodePatch(
    name = "Alternative thumbnails",
    description = "Adds options to replace video thumbnails using the DeArrow API or image captures from the video.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
        addResourcesPatch,
        navigationBarHookPatch,
        cronetImageURLHookPatch,
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
        addResources("youtube", "layout.thumbnails.alternativeThumbnailsPatch")

        val entries = "argusos_alt_thumbnail_options_entries"
        val values = "argusos_alt_thumbnail_options_entry_values"
        PreferenceScreen.ALTERNATIVE_THUMBNAILS.addPreferences(
            ListPreference(
                key = "argusos_alt_thumbnail_home",
                entriesKey = entries,
                entryValuesKey = values,
            ),
            ListPreference(
                key = "argusos_alt_thumbnail_subscription",
                entriesKey = entries,
                entryValuesKey = values,
            ),
            ListPreference(
                key = "argusos_alt_thumbnail_library",
                entriesKey = entries,
                entryValuesKey = values,
            ),
            ListPreference(
                key = "argusos_alt_thumbnail_player",
                entriesKey = entries,
                entryValuesKey = values,
            ),
            ListPreference(
                key = "argusos_alt_thumbnail_search",
                entriesKey = entries,
                entryValuesKey = values,
            ),
            NonInteractivePreference(
                "argusos_alt_thumbnail_dearrow_about",
                // Custom about preference with link to the DeArrow website.
                tag = "app.argusos.extension.youtube.settings.preference.AlternativeThumbnailsAboutDeArrowPreference",
                selectable = true,
            ),
            SwitchPreference("argusos_alt_thumbnail_dearrow_connection_toast"),
            TextPreference("argusos_alt_thumbnail_dearrow_api_url"),
            NonInteractivePreference("argusos_alt_thumbnail_stills_about"),
            SwitchPreference("argusos_alt_thumbnail_stills_fast"),
            ListPreference("argusos_alt_thumbnail_stills_time"),
        )

        addImageURLHook(EXTENSION_CLASS_DESCRIPTOR)
        addImageURLSuccessCallbackHook(EXTENSION_CLASS_DESCRIPTOR)
        addImageURLErrorCallbackHook(EXTENSION_CLASS_DESCRIPTOR)
    }
}
