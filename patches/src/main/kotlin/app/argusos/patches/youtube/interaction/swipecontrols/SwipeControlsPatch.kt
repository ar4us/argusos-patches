package app.argusos.patches.youtube.interaction.swipecontrols

import app.revanced.com.android.tools.smali.dexlib2.mutable.MutableMethod.Companion.toMutable
import app.revanced.patcher.classDef
import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.InputType
import app.argusos.patches.shared.misc.settings.preference.ListPreference
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.shared.misc.settings.preference.TextPreference
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.playertype.playerTypeHookPatch
import app.argusos.patches.youtube.misc.playservice.is_19_43_or_greater
import app.argusos.patches.youtube.misc.playservice.is_20_22_or_greater
import app.argusos.patches.youtube.misc.playservice.is_20_34_or_greater
import app.argusos.patches.youtube.misc.playservice.versionCheckPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.patches.youtube.shared.mainActivityConstructorMethod
import app.argusos.util.*
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod

internal const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/swipecontrols/SwipeControlsHostActivity;"

private val swipeControlsResourcePatch = resourcePatch {
    dependsOn(
        settingsPatch,
        addResourcesPatch,
        versionCheckPatch,
    )

    apply {
        addResources("youtube", "interaction.swipecontrols.swipeControlsResourcePatch")

        // If fullscreen swipe is enabled in newer versions the app can crash.
        // It likely is caused by conflicting experimental flags that are never enabled together.
        // Flag was completely removed in 20.34+
        if (is_19_43_or_greater && !is_20_22_or_greater) {
            PreferenceScreen.SWIPE_CONTROLS.addPreferences(
                SwitchPreference("argusos_swipe_change_video"),
            )
        }

        PreferenceScreen.SWIPE_CONTROLS.addPreferences(
            SwitchPreference("argusos_swipe_brightness"),
            SwitchPreference("argusos_swipe_volume"),
            SwitchPreference("argusos_swipe_press_to_engage"),
            SwitchPreference("argusos_swipe_haptic_feedback"),
            SwitchPreference("argusos_swipe_save_and_restore_brightness"),
            SwitchPreference("argusos_swipe_lowest_value_enable_auto_brightness"),
            ListPreference("argusos_swipe_overlay_style"),
            TextPreference(
                "argusos_swipe_overlay_background_opacity",
                inputType = InputType.NUMBER
            ),
            TextPreference(
                "argusos_swipe_overlay_progress_brightness_color",
                tag = "app.argusos.extension.shared.settings.preference.ColorPickerWithOpacitySliderPreference",
                inputType = InputType.TEXT_CAP_CHARACTERS,
            ),
            TextPreference(
                "argusos_swipe_overlay_progress_volume_color",
                tag = "app.argusos.extension.shared.settings.preference.ColorPickerWithOpacitySliderPreference",
                inputType = InputType.TEXT_CAP_CHARACTERS,
            ),
            TextPreference("argusos_swipe_text_overlay_size", inputType = InputType.NUMBER),
            TextPreference("argusos_swipe_overlay_timeout", inputType = InputType.NUMBER),
            TextPreference("argusos_swipe_threshold", inputType = InputType.NUMBER),
            TextPreference("argusos_swipe_volume_sensitivity", inputType = InputType.NUMBER),
        )

        copyResources(
            "swipecontrols",
            ResourceGroup(
                "drawable",
                "argusos_ic_sc_brightness_auto.xml",
                "argusos_ic_sc_brightness_full.xml",
                "argusos_ic_sc_brightness_high.xml",
                "argusos_ic_sc_brightness_low.xml",
                "argusos_ic_sc_brightness_medium.xml",
                "argusos_ic_sc_volume_high.xml",
                "argusos_ic_sc_volume_low.xml",
                "argusos_ic_sc_volume_mute.xml",
                "argusos_ic_sc_volume_normal.xml",
            ),
        )
    }
}

@Suppress("unused")
val swipeControlsPatch = bytecodePatch(
    name = "Swipe controls",
    description = "Adds options to enable and configure volume and brightness swipe controls.",
) {
    dependsOn(
        sharedExtensionPatch,
        playerTypeHookPatch,
        swipeControlsResourcePatch,
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
        val wrapperClass = swipeControlsHostActivityMethod.classDef
        val targetClass = mainActivityConstructorMethod.classDef

        // Inject the wrapper class from the extension into the class hierarchy of MainActivity.
        wrapperClass.setSuperClass(targetClass.superclass)
        targetClass.setSuperClass(wrapperClass.type)

        // Ensure all classes and methods in the hierarchy are non-final, so we can override them in the extension.
        traverseClassHierarchy(targetClass) {
            accessFlags = accessFlags and AccessFlags.FINAL.value.inv()
            transformMethods {
                ImmutableMethod(
                    definingClass,
                    name,
                    parameters,
                    returnType,
                    accessFlags and AccessFlags.FINAL.value.inv(),
                    annotations,
                    hiddenApiRestrictions,
                    implementation,
                ).toMutable()
            }
        }

        // region patch to enable/disable swipe to change video.

        if (is_19_43_or_greater && !is_20_34_or_greater) {
            swipeChangeVideoMethodMatch.let {
                it.method.insertLiteralOverride(
                    it[-1],
                    "$EXTENSION_CLASS_DESCRIPTOR->allowSwipeChangeVideo(Z)Z",
                )
            }
        }

        // endregion
    }
}
