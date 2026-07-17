package app.argusos.patches.youtube.layout.hide.player.popup

import app.revanced.patcher.extensions.addInstructionsWithLabels
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.patches.youtube.shared.getEngagementPanelControllerMethodMatch

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/patches/DisablePlayerPopupPanelsPatch;"

@Suppress("unused")
val disablePlayerPopupPanelsPatch = bytecodePatch(
    name = "Disable player popup panels",
    description = "Adds an option to disable panels (such as live chat) from opening automatically.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
        addResourcesPatch,
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
        addResources("youtube", "layout.panels.popup.playerPopupPanelsPatch")

        PreferenceScreen.PLAYER.addPreferences(
            SwitchPreference("argusos_disable_player_popup_panels"),
        )

        getEngagementPanelControllerMethodMatch().method.addInstructionsWithLabels(
            0,
            """
                invoke-static { }, $EXTENSION_CLASS_DESCRIPTOR->disablePlayerPopupPanels()Z
                move-result v0
                if-eqz v0, :player_popup_panels
                if-eqz p4, :player_popup_panels
                const/4 v0, 0x0
                return-object v0
                :player_popup_panels
                nop
            """,
        )
    }
}
