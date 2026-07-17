package app.argusos.patches.twitch.debug

import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.twitch.misc.extension.sharedExtensionPatch
import app.argusos.patches.twitch.misc.settings.PreferenceScreen
import app.argusos.patches.twitch.misc.settings.settingsPatch

@Suppress("ObjectPropertyName")
val debugModePatch = bytecodePatch(
    name = "Debug mode",
    description = "Enables Twitch's internal debugging mode.",
    use = false,
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
        addResourcesPatch,
    )

    compatibleWith("tv.twitch.android.app"("16.9.1", "25.3.0"))

    apply {
        addResources("twitch", "debug.debugModePatch")

        PreferenceScreen.MISC.OTHER.addPreferences(
            SwitchPreference("argusos_twitch_debug_mode"),
        )

        listOf(
            isDebugConfigEnabledMethod,
            isOmVerificationEnabledMethod,
            shouldShowDebugOptionsMethod,
        ).forEach { method ->
            method.addInstructions(
                0,
                """
                    invoke-static {}, Lapp/argusos/extension/twitch/patches/DebugModePatch;->isDebugModeEnabled()Z
                    move-result v0
                    return v0
                """,
            )
        }
    }
}
