package app.argusos.patches.youtube.video.quality

import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.playercontrols.*
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.util.ResourceGroup
import app.argusos.util.copyResources

private val videoQualityButtonResourcePatch = resourcePatch {
    dependsOn(playerControlsPatch)

    apply {
        copyResources(
            "qualitybutton",
            ResourceGroup(
                "drawable",
                "argusos_video_quality_dialog_button_rectangle.xml",
            ),
        )

        addBottomControl("qualitybutton")
    }
}

private const val QUALITY_BUTTON_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/videoplayer/VideoQualityDialogButton;"

val videoQualityDialogButtonPatch = bytecodePatch(
    description = "Adds the option to display video quality dialog button in the video player.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
        addResourcesPatch,
        rememberVideoQualityPatch,
        videoQualityButtonResourcePatch,
        playerControlsPatch,
    )

    apply {
        addResources("youtube", "video.quality.button.videoQualityDialogButtonPatch")

        PreferenceScreen.PLAYER.addPreferences(
            SwitchPreference("argusos_video_quality_dialog_button"),
        )

        initializeBottomControl(QUALITY_BUTTON_CLASS_DESCRIPTOR)
        injectVisibilityCheckCall(QUALITY_BUTTON_CLASS_DESCRIPTOR)
    }
}
