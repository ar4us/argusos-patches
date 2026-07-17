package app.argusos.patches.youtube.layout.hide.relatedvideooverlay

import app.revanced.patcher.extensions.ExternalLabel
import app.revanced.patcher.extensions.addInstructionsWithLabels
import app.revanced.patcher.extensions.getInstruction
import app.revanced.patcher.immutableClassDef
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.mapping.resourceMappingPatch
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/patches/HideRelatedVideoOverlayPatch;"

@Suppress("unused")
val hideRelatedVideoOverlayPatch = bytecodePatch(
    name = "Hide related video overlay",
    description = "Adds an option to hide the related video overlay shown when swiping up in fullscreen.",
) {
    dependsOn(
        settingsPatch,
        sharedExtensionPatch,
        addResourcesPatch,
        resourceMappingPatch,
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
        addResources("youtube", "layout.hide.relatedvideooverlay.hideRelatedVideoOverlayPatch")

        PreferenceScreen.PLAYER.addPreferences(
            SwitchPreference("argusos_hide_related_videos_overlay"),
        )

        val relatedEndScreenResultsMethod =
            relatedEndScreenResultsParentMethod.immutableClassDef.getRelatedEndScreenResultsMethod()

        relatedEndScreenResultsMethod.addInstructionsWithLabels(
            0,
            """
                invoke-static {}, $EXTENSION_CLASS_DESCRIPTOR->hideRelatedVideoOverlay()Z
                move-result v0
                if-eqz v0, :show
                return-void
            """,
            ExternalLabel("show", relatedEndScreenResultsMethod.getInstruction(0)),
        )
    }
}
