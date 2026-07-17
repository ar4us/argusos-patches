package app.argusos.patches.youtube.layout.player.overlay

import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.getInstruction
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.mapping.resourceMappingPatch
import app.argusos.patches.shared.misc.settings.preference.InputType
import app.argusos.patches.shared.misc.settings.preference.TextPreference
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/patches/CustomPlayerOverlayOpacityPatch;"

@Suppress("unused")
val customPlayerOverlayOpacityPatch = bytecodePatch(
    name = "Custom player overlay opacity",
    description = "Adds an option to change the opacity of the video player background when player controls are visible.",
) {
    dependsOn(
        settingsPatch,
        resourceMappingPatch,
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
        addResources("youtube", "layout.player.overlay.customPlayerOverlayOpacityResourcePatch")

        PreferenceScreen.PLAYER.addPreferences(
            TextPreference("argusos_player_overlay_opacity", inputType = InputType.NUMBER),
        )

        createPlayerOverviewMethodMatch.let {
            it.method.apply {
                val viewRegisterIndex = it[-1]
                val viewRegister =
                    getInstruction<OneRegisterInstruction>(viewRegisterIndex).registerA

                addInstruction(
                    viewRegisterIndex + 1,
                    "invoke-static { v$viewRegister }, " +
                            "$EXTENSION_CLASS_DESCRIPTOR->changeOpacity(Landroid/widget/ImageView;)V",
                )
            }
        }
    }
}
