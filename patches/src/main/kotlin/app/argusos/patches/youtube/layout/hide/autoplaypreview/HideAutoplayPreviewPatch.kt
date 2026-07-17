package app.argusos.patches.youtube.layout.hide.autoplaypreview

import app.revanced.patcher.extensions.ExternalLabel
import app.revanced.patcher.extensions.addInstructionsWithLabels
import app.revanced.patcher.extensions.getInstruction
import app.revanced.patcher.extensions.methodReference
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.shared.misc.mapping.resourceMappingPatch
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.patches.youtube.shared.getLayoutConstructorMethodMatch
import app.argusos.util.indexOfFirstInstructionOrThrow
import app.argusos.util.indexOfFirstResourceIdOrThrow
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import kotlin.collections.first

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/patches/HideAutoplayPreviewPatch;"

@Suppress("unused")
val hideAutoplayPreviewPatch = bytecodePatch(
    name = "Hide autoplay preview",
    description = "Adds an option to hide the autoplay preview at the end of videos.",
) {
    dependsOn(
        settingsPatch,
        sharedExtensionPatch,
        resourceMappingPatch
    )

    compatibleWith(
        "com.google.android.youtube"(
            "20.14.43",
            "20.21.37",
            "20.26.46",
            "20.31.42",
            "20.37.48",
            "20.40.45"
        )
    )

    apply {
        addResources("youtube", "layout.hide.autoplaypreview.hideAutoplayPreviewPatch")

        PreferenceScreen.PLAYER.addPreferences(
            SwitchPreference("argusos_hide_autoplay_preview")
        )

        getLayoutConstructorMethodMatch().method.apply {
            val constIndex = indexOfFirstResourceIdOrThrow("autonav_preview_stub")
            val constRegister = getInstruction<OneRegisterInstruction>(constIndex).registerA
            val gotoIndex = indexOfFirstInstructionOrThrow(constIndex) {
                val parameterTypes = methodReference?.parameterTypes
                opcode == Opcode.INVOKE_VIRTUAL &&
                        parameterTypes?.size == 2 &&
                        parameterTypes.first() == "Landroid/view/ViewStub;"
            } + 1

            addInstructionsWithLabels(
                constIndex,
                """
                    invoke-static {}, $EXTENSION_CLASS_DESCRIPTOR->hideAutoplayPreview()Z
                    move-result v$constRegister
                    if-nez v$constRegister, :hidden
                """,
                ExternalLabel("hidden", getInstruction(gotoIndex)),
            )
        }
    }
}
