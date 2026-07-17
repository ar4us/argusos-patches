package app.argusos.patches.youtube.misc.loopvideo.button

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

private val loopVideoButtonResourcePatch = resourcePatch {
    dependsOn(playerControlsResourcePatch)

    apply {
        copyResources(
            "loopvideobutton",
            ResourceGroup(
                "drawable",
                "argusos_loop_video_button_on.xml",
                "argusos_loop_video_button_off.xml"
            )
        )

        addBottomControl("loopvideobutton")
    }
}

private const val LOOP_VIDEO_BUTTON_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/videoplayer/LoopVideoButton;"

internal val loopVideoButtonPatch = bytecodePatch(
    description = "Adds the option to display loop video button in the video player.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
        addResourcesPatch,
        loopVideoButtonResourcePatch,
        playerControlsPatch,
    )

    apply {
        addResources("youtube", "misc.loopvideo.button.loopVideoButtonPatch")

        PreferenceScreen.PLAYER.addPreferences(
            SwitchPreference("argusos_loop_video_button"),
        )

        // Initialize the button using standard approach.
        initializeBottomControl(LOOP_VIDEO_BUTTON_CLASS_DESCRIPTOR)
        injectVisibilityCheckCall(LOOP_VIDEO_BUTTON_CLASS_DESCRIPTOR)
    }
}
