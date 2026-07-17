package app.argusos.patches.youtube.layout.formfactor

import app.revanced.patcher.*
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.getInstruction
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.ListPreference
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.navigation.hookNavigationButtonCreated
import app.argusos.patches.youtube.misc.navigation.navigationBarHookPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/patches/ChangeFormFactorPatch;"

@Suppress("unused")
val changeFormFactorPatch = bytecodePatch(
    name = "Change form factor",
    description = "Adds an option to change the UI appearance to a phone, tablet, or automotive device.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
        addResourcesPatch,
        navigationBarHookPatch
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
        addResources("youtube", "layout.formfactor.changeFormFactorPatch")

        PreferenceScreen.GENERAL.addPreferences(
            ListPreference("argusos_change_form_factor"),
        )

        hookNavigationButtonCreated(EXTENSION_CLASS_DESCRIPTOR)

        val formFactorEnumConstructorClass = formFactorEnumConstructorMethod.definingClass

        val createPlayerRequestBodyWithModelMatch = firstMethodComposite {
            accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
            returnType("L")
            parameterTypes()
            instructions(
                field { name == "MODEL" && definingClass == "Landroid/os/Build;" },
                field { type == "I" && definingClass == formFactorEnumConstructorClass },
            )
        }

        createPlayerRequestBodyWithModelMatch.let {
            it.method.apply {
                val index = it[-1]
                val register = getInstruction<TwoRegisterInstruction>(index).registerA

                addInstructions(
                    index + 1,
                    """
                        invoke-static { v$register }, $EXTENSION_CLASS_DESCRIPTOR->getFormFactor(I)I
                        move-result v$register
                    """,
                )
            }
        }
    }
}
