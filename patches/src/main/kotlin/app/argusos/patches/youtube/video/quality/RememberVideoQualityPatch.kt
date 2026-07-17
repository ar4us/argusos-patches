package app.argusos.patches.youtube.video.quality

import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.getInstruction
import app.revanced.patcher.immutableClassDef
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.ListPreference
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.playertype.playerTypeHookPatch
import app.argusos.patches.youtube.misc.playservice.versionCheckPatch
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.patches.youtube.shared.videoQualityChangedMethodMatch
import app.argusos.patches.youtube.video.information.onCreateHook
import app.argusos.patches.youtube.video.information.videoInformationPatch
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/patches/playback/quality/RememberVideoQualityPatch;"

val rememberVideoQualityPatch = bytecodePatch {
    dependsOn(
        sharedExtensionPatch,
        videoInformationPatch,
        playerTypeHookPatch,
        settingsPatch,
        addResourcesPatch,
        versionCheckPatch,
    )

    apply {
        addResources("youtube", "video.quality.rememberVideoQualityPatch")

        settingsMenuVideoQualityGroup.addAll(
            listOf(
                ListPreference(
                    key = "argusos_video_quality_default_mobile",
                    entriesKey = "argusos_video_quality_default_entries",
                    entryValuesKey = "argusos_video_quality_default_entry_values",
                ),
                ListPreference(
                    key = "argusos_video_quality_default_wifi",
                    entriesKey = "argusos_video_quality_default_entries",
                    entryValuesKey = "argusos_video_quality_default_entry_values",
                ),
                SwitchPreference("argusos_remember_video_quality_last_selected"),

                ListPreference(
                    key = "argusos_shorts_quality_default_mobile",
                    entriesKey = "argusos_shorts_quality_default_entries",
                    entryValuesKey = "argusos_shorts_quality_default_entry_values",
                ),
                ListPreference(
                    key = "argusos_shorts_quality_default_wifi",
                    entriesKey = "argusos_shorts_quality_default_entries",
                    entryValuesKey = "argusos_shorts_quality_default_entry_values",
                ),
                SwitchPreference("argusos_remember_shorts_quality_last_selected"),
                SwitchPreference("argusos_remember_video_quality_last_selected_toast"),
            ),
        )

        onCreateHook(EXTENSION_CLASS_DESCRIPTOR, "newVideoStarted")

        // Inject a call to remember the selected quality for Shorts.
        videoQualityItemOnClickParentMethod.immutableClassDef.getVideoQualityItemOnClickMethod().addInstruction(
            0,
            "invoke-static { p3 }, $EXTENSION_CLASS_DESCRIPTOR->userChangedShortsQuality(I)V",
        )

        // Inject a call to remember the user selected quality for regular videos.
        videoQualityChangedMethodMatch.let { match ->
            val index = match[-1]
            val register = match.method.getInstruction<TwoRegisterInstruction>(index).registerA

            match.method.addInstruction(
                index + 1,
                "invoke-static { v$register }, $EXTENSION_CLASS_DESCRIPTOR->userChangedQuality(I)V",
            )
        }
    }
}
