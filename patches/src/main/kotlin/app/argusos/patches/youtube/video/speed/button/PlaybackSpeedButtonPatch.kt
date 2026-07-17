package app.argusos.patches.youtube.video.speed.button

import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.playercontrols.*
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.patches.youtube.video.information.userSelectedPlaybackSpeedHook
import app.argusos.patches.youtube.video.information.videoInformationPatch
import app.argusos.patches.youtube.video.information.videoSpeedChangedHook
import app.argusos.patches.youtube.video.speed.custom.customPlaybackSpeedPatch
import app.argusos.util.ResourceGroup
import app.argusos.util.copyResources

private val playbackSpeedButtonResourcePatch = resourcePatch {
    dependsOn(playerControlsPatch)

    apply {
        copyResources(
            "speedbutton",
            ResourceGroup(
                "drawable",
                "argusos_playback_speed_dialog_button_rectangle.xml"
            )
        )

        addBottomControl("speedbutton")
    }
}

private const val SPEED_BUTTON_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/videoplayer/PlaybackSpeedDialogButton;"

val playbackSpeedButtonPatch = bytecodePatch(
    description = "Adds the option to display playback speed dialog button in the video player.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
        addResourcesPatch,
        customPlaybackSpeedPatch,
        playbackSpeedButtonResourcePatch,
        playerControlsPatch,
        videoInformationPatch,
    )

    apply {
        addResources("youtube", "video.speed.button.playbackSpeedButtonPatch")

        PreferenceScreen.PLAYER.addPreferences(
            SwitchPreference("argusos_playback_speed_dialog_button"),
        )

        initializeBottomControl(SPEED_BUTTON_CLASS_DESCRIPTOR)
        injectVisibilityCheckCall(SPEED_BUTTON_CLASS_DESCRIPTOR)

        videoSpeedChangedHook(SPEED_BUTTON_CLASS_DESCRIPTOR, "videoSpeedChanged")
        userSelectedPlaybackSpeedHook(SPEED_BUTTON_CLASS_DESCRIPTOR, "videoSpeedChanged")
    }
}
