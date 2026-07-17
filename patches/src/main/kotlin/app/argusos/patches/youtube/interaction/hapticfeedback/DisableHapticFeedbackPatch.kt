package app.argusos.patches.youtube.interaction.hapticfeedback

import app.revanced.patcher.extensions.ExternalLabel
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.addInstructionsWithLabels
import app.revanced.patcher.extensions.fieldReference
import app.revanced.patcher.extensions.getInstruction
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.all.misc.transformation.IMethodCall
import app.argusos.patches.all.misc.transformation.filterMapInstruction35c
import app.argusos.patches.all.misc.transformation.transformInstructionsPatch
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

private const val EXTENSION_CLASS_DESCRIPTOR_PREFIX =
    "Lapp/argusos/extension/youtube/patches/DisableHapticFeedbackPatch"

private const val EXTENSION_CLASS_DESCRIPTOR = "$EXTENSION_CLASS_DESCRIPTOR_PREFIX;"

@Suppress("unused")
val disableHapticFeedbackPatch = bytecodePatch(
    name = "Disable haptic feedback",
    description = "Adds an option to disable haptic feedback in the player for various actions.",
) {
    dependsOn(
        settingsPatch,
        addResourcesPatch,
        transformInstructionsPatch(
            filterMap = { classDef, _, instruction, instructionIndex ->
                filterMapInstruction35c<MethodCall>(
                    EXTENSION_CLASS_DESCRIPTOR_PREFIX,
                    classDef,
                    instruction,
                    instructionIndex,
                )
            },
            transform = { method, entry ->
                val (methodType, instruction, instructionIndex) = entry
                methodType.replaceInvokeVirtualWithExtension(
                    EXTENSION_CLASS_DESCRIPTOR,
                    method,
                    instruction,
                    instructionIndex,
                )
            },
        ),
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
        addResources("youtube", "misc.hapticfeedback.disableHapticFeedbackPatch")

        PreferenceScreen.PLAYER.addPreferences(
            PreferenceScreenPreference(
                "argusos_disable_haptic_feedback",
                preferences = setOf(
                    SwitchPreference("argusos_disable_haptic_feedback_chapters"),
                    SwitchPreference("argusos_disable_haptic_feedback_precise_seeking"),
                    SwitchPreference("argusos_disable_haptic_feedback_seek_undo"),
                    SwitchPreference("argusos_disable_haptic_feedback_tap_and_hold"),
                    SwitchPreference("argusos_disable_haptic_feedback_zoom"),
                ),
            ),
        )

        arrayOf(
            markerHapticsMethod to "disableChapterVibrate",
            scrubbingHapticsMethod to "disablePreciseSeekingVibrate",
            seekUndoHapticsMethod to "disableSeekUndoVibrate",
            zoomHapticsMethod to "disableZoomVibrate",
        ).forEach { (method, methodName) ->
            method.addInstructionsWithLabels(
                0,
                """
                    invoke-static {}, $EXTENSION_CLASS_DESCRIPTOR->$methodName()Z
                    move-result v0
                    if-eqz v0, :vibrate
                    return-void
                """,
                ExternalLabel("vibrate", method.getInstruction(0)),
            )
        }

        val vibratorField = tapAndHoldHapticsHandlerMethodMatch.let {
            it.immutableMethod.getInstruction(it[-1]).fieldReference!!
        }
        // Function, because it can be the same method as getTapAndHoldSpeedMethodMatch.
        getTapAndHoldHapticsMethodMatch(vibratorField).let {
            it.method.apply {
                val index = it[0]
                val register = getInstruction<TwoRegisterInstruction>(index).registerA

                addInstructions(
                    index + 1,
                    """
                        invoke-static { v$register }, ${EXTENSION_CLASS_DESCRIPTOR}->disableTapAndHoldVibrate(Ljava/lang/Object;)Ljava/lang/Object;
                        move-result-object v$register
                    """
                )
            }
        }
    }
}

@Suppress("unused")
private enum class MethodCall(
    override val definedClassName: String,
    override val methodName: String,
    override val methodParams: Array<String>,
    override val returnType: String,
) : IMethodCall {
    VibrationEffect(
        "Landroid/os/Vibrator;",
        "vibrate",
        arrayOf("Landroid/os/VibrationEffect;"),
        "V",
    ),
    VibrationMilliseconds(
        "Landroid/os/Vibrator;",
        "vibrate",
        arrayOf("J"),
        "V",
    ),
}