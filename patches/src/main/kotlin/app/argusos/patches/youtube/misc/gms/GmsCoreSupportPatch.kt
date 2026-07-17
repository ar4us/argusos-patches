package app.argusos.patches.youtube.misc.gms

import app.revanced.patcher.patch.BytecodePatchContext
import app.revanced.patcher.patch.Option
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.castContextFetchMethod
import app.argusos.patches.shared.misc.gms.gmsCoreSupportPatch
import app.argusos.patches.shared.misc.settings.preference.IntentPreference
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.shared.primeMethod
import app.argusos.patches.youtube.layout.buttons.overlay.hidePlayerOverlayButtonsPatch
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.gms.Constants.ARGUSOS_YOUTUBE_PACKAGE_NAME
import app.argusos.patches.youtube.misc.gms.Constants.YOUTUBE_PACKAGE_NAME
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.patches.youtube.misc.spoof.spoofVideoStreamsPatch
import app.argusos.patches.youtube.shared.mainActivityOnCreateMethod

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    fromPackageName = YOUTUBE_PACKAGE_NAME,
    toPackageName = ARGUSOS_YOUTUBE_PACKAGE_NAME,
    getPrimeMethod = BytecodePatchContext::primeMethod::get,
    getEarlyReturnMethods = setOf(BytecodePatchContext::castContextFetchMethod::get),
    getMainActivityOnCreateMethodToGetInsertIndex = BytecodePatchContext::mainActivityOnCreateMethod::get to { 0 },
    extensionPatch = sharedExtensionPatch,
    gmsCoreSupportResourcePatchFactory = ::gmsCoreSupportResourcePatch,
) {
    dependsOn(
        hidePlayerOverlayButtonsPatch, // Hide non-functional cast button.
        spoofVideoStreamsPatch,
    )

    compatibleWith(
        YOUTUBE_PACKAGE_NAME(
            "20.14.43",
            "20.21.37",
            "20.26.46",
            "20.31.42",
            "20.37.48",
            "20.40.45"
        ),
    )
}

private fun gmsCoreSupportResourcePatch(
    gmsCoreVendorGroupIdOption: Option<String>,
) = app.argusos.patches.shared.misc.gms.gmsCoreSupportResourcePatch(
    fromPackageName = YOUTUBE_PACKAGE_NAME,
    toPackageName = ARGUSOS_YOUTUBE_PACKAGE_NAME,
    gmsCoreVendorGroupIdOption = gmsCoreVendorGroupIdOption,
    spoofedPackageSignature = "24bb24c05e47e0aefa68a58a766179d9b613a600",
    executeBlock = {
        addResources("shared", "misc.gms.gmsCoreSupportResourcePatch")

        val gmsCoreVendorGroupId by gmsCoreVendorGroupIdOption

        PreferenceScreen.MISC.addPreferences(
            PreferenceScreenPreference(
                "argusos_gms_core_screen",
                preferences = setOf(
                    SwitchPreference("argusos_gms_core_check_updates"),
                    IntentPreference(
                        "argusos_gms_core_settings",
                        intent = IntentPreference.Intent("", "org.microg.gms.ui.SettingsActivity") {
                            "$gmsCoreVendorGroupId.android.gms"
                        },
                    ),
                ),
            ),
        )
    },
) {
    dependsOn(
        addResourcesPatch,
        settingsPatch,
        accountCredentialsInvalidTextPatch,
    )
}
