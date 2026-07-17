package app.argusos.patches.youtube.layout.branding

import app.revanced.patcher.patch.BytecodePatchContext
import app.argusos.patches.shared.layout.branding.baseCustomBrandingPatch
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.gms.Constants.YOUTUBE_MAIN_ACTIVITY_NAME
import app.argusos.patches.youtube.misc.gms.Constants.YOUTUBE_PACKAGE_NAME
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.shared.mainActivityOnCreateMethod

@Suppress("unused")
val customBrandingPatch = baseCustomBrandingPatch(
    addResourcePatchName = "youtube",
    originalLauncherIconName = "ic_launcher",
    originalAppName = "@string/application_name",
    originalAppPackageName = YOUTUBE_PACKAGE_NAME,
    isYouTubeMusic = false,
    numberOfPresetAppNames = 5,
    getMainActivityOnCreate = BytecodePatchContext::mainActivityOnCreateMethod::get,
    mainActivityName = YOUTUBE_MAIN_ACTIVITY_NAME,
    activityAliasNameWithIntents = $$"com.google.android.youtube.app.honeycomb.Shell$HomeActivity",
    preferenceScreen = PreferenceScreen.GENERAL,

    block = {
        dependsOn(sharedExtensionPatch)

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
)
