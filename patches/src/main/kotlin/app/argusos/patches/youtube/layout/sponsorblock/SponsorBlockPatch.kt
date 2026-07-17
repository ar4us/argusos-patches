package app.argusos.patches.youtube.layout.sponsorblock

import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.getInstruction
import app.revanced.patcher.immutableClassDef
import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.mapping.resourceMappingPatch
import app.argusos.patches.shared.misc.settings.preference.NonInteractivePreference
import app.argusos.patches.shared.misc.settings.preference.PreferenceCategory
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.playercontrols.addTopControl
import app.argusos.patches.youtube.misc.playercontrols.initializeTopControl
import app.argusos.patches.youtube.misc.playercontrols.injectVisibilityCheckCall
import app.argusos.patches.youtube.misc.playercontrols.playerControlsPatch
import app.argusos.patches.youtube.misc.playertype.playerTypeHookPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.patches.youtube.shared.getLayoutConstructorMethodMatch
import app.argusos.patches.youtube.shared.seekbarMethod
import app.argusos.patches.youtube.shared.getSeekbarOnDrawMethodMatch
import app.argusos.patches.youtube.video.information.onCreateHook
import app.argusos.patches.youtube.video.information.videoInformationPatch
import app.argusos.patches.youtube.video.information.videoTimeHook
import app.argusos.patches.youtube.video.videoid.hookBackgroundPlayVideoId
import app.argusos.patches.youtube.video.videoid.videoIdPatch
import app.argusos.util.*
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

private val sponsorBlockResourcePatch = resourcePatch {
    dependsOn(
        settingsPatch,
        resourceMappingPatch,
        addResourcesPatch,
        playerControlsPatch,
    )

    apply {
        addResources("youtube", "layout.sponsorblock.sponsorBlockResourcePatch")

        PreferenceScreen.SPONSORBLOCK.addPreferences(
            // SB setting is old code with lots of custom preferences and updating behavior.
            // Added as a preference group and not a fragment so the preferences are searchable.
            PreferenceCategory(
                key = "argusos_settings_screen_10_sponsorblock",
                sorting = PreferenceScreenPreference.Sorting.UNSORTED,
                preferences = emptySet(), // Preferences are added by custom class at runtime.
                tag = "app.argusos.extension.youtube.sponsorblock.ui.SponsorBlockPreferenceGroup",
            ),
            PreferenceCategory(
                key = "argusos_sb_stats",
                sorting = PreferenceScreenPreference.Sorting.UNSORTED,
                preferences = emptySet(), // Preferences are added by custom class at runtime.
                tag = "app.argusos.extension.youtube.sponsorblock.ui.SponsorBlockStatsPreferenceCategory",
            ),
            PreferenceCategory(
                key = "argusos_sb_about",
                sorting = PreferenceScreenPreference.Sorting.UNSORTED,
                preferences = setOf(
                    NonInteractivePreference(
                        key = "argusos_sb_about_api",
                        tag = "app.argusos.extension.youtube.sponsorblock.ui.SponsorBlockAboutPreference",
                        selectable = true,
                    ),
                ),
            ),
        )

        arrayOf(
            ResourceGroup(
                "layout",
                "argusos_sb_inline_sponsor_overlay.xml",
                "argusos_sb_new_segment.xml",
                "argusos_sb_skip_sponsor_button.xml",
            ),
            ResourceGroup(
                "drawable",
                "argusos_sb_adjust.xml",
                "argusos_sb_backward.xml",
                "argusos_sb_compare.xml",
                "argusos_sb_edit.xml",
                "argusos_sb_forward.xml",
                "argusos_sb_logo.xml",
                "argusos_sb_logo_bold.xml",
                "argusos_sb_publish.xml",
                "argusos_sb_voting.xml",
            ),
        ).forEach { resourceGroup ->
            copyResources("sponsorblock", resourceGroup)
        }

        addTopControl("sponsorblock")
    }
}

internal const val EXTENSION_SEGMENT_PLAYBACK_CONTROLLER_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/sponsorblock/SegmentPlaybackController;"
private const val EXTENSION_CREATE_SEGMENT_BUTTON_CONTROLLER_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/sponsorblock/ui/CreateSegmentButton;"
private const val EXTENSION_VOTING_BUTTON_CONTROLLER_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/sponsorblock/ui/VotingButton;"
private const val EXTENSION_SPONSORBLOCK_VIEW_CONTROLLER_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/sponsorblock/ui/SponsorBlockViewController;"

@Suppress("unused")
val sponsorBlockPatch = bytecodePatch(
    name = "SponsorBlock",
    description = "Adds options to enable and configure SponsorBlock, which can skip undesired video segments such as sponsored content.",
) {
    dependsOn(
        sharedExtensionPatch,
        resourceMappingPatch,
        videoIdPatch,
        // Required to skip segments on time.
        videoInformationPatch,
        // Used to prevent SponsorBlock from running on Shorts because SponsorBlock does not yet support Shorts.
        playerTypeHookPatch,
        playerControlsPatch,
        sponsorBlockResourcePatch,
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
        // Hook the video time methods.
        videoTimeHook(
            EXTENSION_SEGMENT_PLAYBACK_CONTROLLER_CLASS_DESCRIPTOR,
            "setVideoTime",
        )

        hookBackgroundPlayVideoId(
            EXTENSION_SEGMENT_PLAYBACK_CONTROLLER_CLASS_DESCRIPTOR +
                    "->setCurrentVideoId(Ljava/lang/String;)V",
        )

        // Set seekbar draw rectangle.
        val rectangleFieldName: FieldReference
        seekbarMethod.immutableClassDef.rectangleFieldInvalidatorMethodMatch.let {
            it.method.apply {
                val rectangleIndex = indexOfFirstInstructionReversedOrThrow(
                    it[0],
                ) {
                    getReference<FieldReference>()?.type == "Landroid/graphics/Rect;"
                }
                rectangleFieldName =
                    getInstruction<ReferenceInstruction>(rectangleIndex).reference as FieldReference
            }
        }

        // Seekbar drawing.

        // Cannot match using original immutable class because
        // class may have been modified by other patches
        seekbarMethod.immutableClassDef.getSeekbarOnDrawMethodMatch().let {
            it.method.apply {
                // Set seekbar thickness.
                val thicknessIndex = it[-1]
                val thicknessRegister =
                    getInstruction<OneRegisterInstruction>(thicknessIndex).registerA
                addInstruction(
                    thicknessIndex + 1,
                    "invoke-static { v$thicknessRegister }, " +
                            "$EXTENSION_SEGMENT_PLAYBACK_CONTROLLER_CLASS_DESCRIPTOR->setSeekbarThickness(I)V",
                )

                // Find the drawCircle call and draw the segment before it.
                val drawCircleIndex = indexOfFirstInstructionReversedOrThrow {
                    getReference<MethodReference>()?.name == "drawCircle"
                }
                val drawCircleInstruction = getInstruction<FiveRegisterInstruction>(drawCircleIndex)
                val canvasInstanceRegister = drawCircleInstruction.registerC
                val centerYRegister = drawCircleInstruction.registerE

                addInstruction(
                    drawCircleIndex,
                    "invoke-static { v$canvasInstanceRegister, v$centerYRegister }, " +
                            "$EXTENSION_SEGMENT_PLAYBACK_CONTROLLER_CLASS_DESCRIPTOR->" +
                            "drawSegmentTimeBars(Landroid/graphics/Canvas;F)V",
                )

                // Set seekbar bounds.
                addInstructions(
                    0,
                    """
                        move-object/from16 v0, p0
                        iget-object v0, v0, $rectangleFieldName
                        invoke-static { v0 }, $EXTENSION_SEGMENT_PLAYBACK_CONTROLLER_CLASS_DESCRIPTOR->setSeekbarRectangle(Landroid/graphics/Rect;)V
                    """,
                )
            }
        }

        // Change visibility of the buttons.
        initializeTopControl(EXTENSION_CREATE_SEGMENT_BUTTON_CONTROLLER_CLASS_DESCRIPTOR)
        injectVisibilityCheckCall(EXTENSION_CREATE_SEGMENT_BUTTON_CONTROLLER_CLASS_DESCRIPTOR)

        initializeTopControl(EXTENSION_VOTING_BUTTON_CONTROLLER_CLASS_DESCRIPTOR)
        injectVisibilityCheckCall(EXTENSION_VOTING_BUTTON_CONTROLLER_CLASS_DESCRIPTOR)

        // Append the new time to the player layout.
        appendTimeMethodMatch.let {
            it.method.apply {
                val index = it[-1]
                val register = getInstruction<OneRegisterInstruction>(index).registerA

                addInstructions(
                    index + 1,
                    """
                        invoke-static { v$register }, $EXTENSION_SEGMENT_PLAYBACK_CONTROLLER_CLASS_DESCRIPTOR->appendTimeWithoutSegments(Ljava/lang/String;)Ljava/lang/String;
                        move-result-object v$register
                    """,
                )
            }
        }

        // Initialize the player controller.
        onCreateHook(EXTENSION_SEGMENT_PLAYBACK_CONTROLLER_CLASS_DESCRIPTOR, "initialize")

        // Initialize the SponsorBlock view.
        getLayoutConstructorMethodMatch().immutableClassDef.controlsOverlayMethodMatch.let {
            val checkCastIndex = it[-1]

            it.method.apply {
                val frameLayoutRegister =
                    getInstruction<OneRegisterInstruction>(checkCastIndex).registerA
                addInstruction(
                    checkCastIndex + 1,
                    "invoke-static {v$frameLayoutRegister}, $EXTENSION_SPONSORBLOCK_VIEW_CONTROLLER_CLASS_DESCRIPTOR->initialize(Landroid/view/ViewGroup;)V",
                )
            }
        }

        adProgressTextViewVisibilityMethodMatch.let {
            val setVisibilityIndex = it[0]
            val register =
                it.method.getInstruction<FiveRegisterInstruction>(setVisibilityIndex).registerD

            it.method.addInstructionsAtControlFlowLabel(
                setVisibilityIndex,
                "invoke-static { v$register }, $EXTENSION_SEGMENT_PLAYBACK_CONTROLLER_CLASS_DESCRIPTOR->setAdProgressTextVisibility(I)V",
            )
        }
    }
}
