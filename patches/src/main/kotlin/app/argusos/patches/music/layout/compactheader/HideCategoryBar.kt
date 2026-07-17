package app.argusos.patches.music.layout.compactheader

import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.getInstruction
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.music.misc.extension.sharedExtensionPatch
import app.argusos.patches.music.misc.settings.PreferenceScreen
import app.argusos.patches.music.misc.settings.settingsPatch
import app.argusos.patches.shared.misc.mapping.ResourceType
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

internal var chipCloud = -1L
    private set

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/music/patches/HideCategoryBarPatch;"

@Suppress("unused")
val hideCategoryBarPatch = bytecodePatch(
    name = "Hide category bar",
    description = "Adds an option to hide the category bar at the top of the homepage.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
        addResourcesPatch,
    )

    compatibleWith(
        "com.google.android.apps.youtube.music"(
            "7.29.52",
            "8.10.52",
            "8.37.56",
            "8.40.54",
        ),
    )

    apply {
        addResources("music", "layout.compactheader.hideCategoryBar")

        PreferenceScreen.GENERAL.addPreferences(
            SwitchPreference("argusos_music_hide_category_bar"),
        )

        chipCloud = ResourceType.LAYOUT["chip_cloud"]

        chipCloudMethodMatch.let {
            val targetIndex = it[-1]
            val targetRegister =
                it.method.getInstruction<OneRegisterInstruction>(targetIndex).registerA

            it.method.addInstruction(
                targetIndex + 1,
                "invoke-static { v$targetRegister }, $EXTENSION_CLASS_DESCRIPTOR->hideCategoryBar(Landroid/view/View;)V",
            )
        }
    }
}
