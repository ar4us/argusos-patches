package app.argusos.patches.youtube.layout.thumbnails

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.imageurlhook.addImageURLHook
import app.argusos.patches.youtube.misc.imageurlhook.cronetImageURLHookPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/patches/BypassImageRegionRestrictionsPatch;"

@Suppress("unused")
val bypassImageRegionRestrictionsPatch = bytecodePatch(
    name = "Bypass image region restrictions",
    description = "Adds an option to use a different host for user avatar and channel images " +
            "and can fix missing images that are blocked in some countries.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
        addResourcesPatch,
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
        addResources("youtube", "layout.thumbnails.bypassImageRegionRestrictionsPatch")

        PreferenceScreen.MISC.addPreferences(
            SwitchPreference("argusos_bypass_image_region_restrictions"),
        )

        // A priority hook is not needed, as the image URLs of interest are not modified
        // by AlternativeThumbnails or any other patch in this repo.
        addImageURLHook(EXTENSION_CLASS_DESCRIPTOR)
    }
}
