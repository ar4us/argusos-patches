package app.argusos.patches.music.misc.gms

import app.revanced.patcher.patch.BytecodePatchContext
import app.revanced.patcher.patch.Option
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.music.misc.extension.sharedExtensionPatch
import app.argusos.patches.music.misc.fileprovider.fileProviderPatch
import app.argusos.patches.music.misc.gms.Constants.MUSIC_PACKAGE_NAME
import app.argusos.patches.music.misc.gms.Constants.ARGUSOS_MUSIC_PACKAGE_NAME
import app.argusos.patches.music.misc.settings.PreferenceScreen
import app.argusos.patches.music.misc.settings.settingsPatch
import app.argusos.patches.shared.castContextFetchMethod
import app.argusos.patches.shared.misc.gms.gmsCoreSupportPatch
import app.argusos.patches.shared.misc.settings.preference.IntentPreference
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.shared.primeMethod

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    fromPackageName = MUSIC_PACKAGE_NAME,
    toPackageName = ARGUSOS_MUSIC_PACKAGE_NAME,
    getPrimeMethod = { primeMethod },
    getEarlyReturnMethods = setOf(BytecodePatchContext::castContextFetchMethod::get),
    getMainActivityOnCreateMethodToGetInsertIndex = BytecodePatchContext::musicActivityOnCreateMethod::get to { 0 },
    extensionPatch = sharedExtensionPatch,
    gmsCoreSupportResourcePatchFactory = ::gmsCoreSupportResourcePatch,
) {

    compatibleWith(
        MUSIC_PACKAGE_NAME(
            "7.29.52",
            "8.10.52",
            "8.37.56",
            "8.40.54",
        ),
    )
}

private fun gmsCoreSupportResourcePatch(
    gmsCoreVendorGroupIdOption: Option<String>,
) = app.argusos.patches.shared.misc.gms.gmsCoreSupportResourcePatch(
    fromPackageName = MUSIC_PACKAGE_NAME,
    toPackageName = ARGUSOS_MUSIC_PACKAGE_NAME,
    gmsCoreVendorGroupIdOption = gmsCoreVendorGroupIdOption,
    spoofedPackageSignature = "afb0fed5eeaebdd86f56a97742f4b6b33ef59875",
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
        fileProviderPatch(
            MUSIC_PACKAGE_NAME,
            ARGUSOS_MUSIC_PACKAGE_NAME,
        ),
    )
}
