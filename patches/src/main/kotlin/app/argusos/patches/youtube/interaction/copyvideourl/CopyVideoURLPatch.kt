package app.argusos.patches.youtube.interaction.copyvideourl

import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.youtube.misc.playercontrols.*
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.patches.youtube.video.information.videoInformationPatch
import app.argusos.util.ResourceGroup
import app.argusos.util.copyResources

private val copyVideoURLResourcePatch = resourcePatch {
    dependsOn(
        settingsPatch,
        playerControlsPatch,
        addResourcesPatch,
    )

    apply {
        addResources("youtube", "interaction.copyvideourl.copyVideoURLResourcePatch")

        PreferenceScreen.PLAYER.addPreferences(
            SwitchPreference("argusos_copy_video_url"),
            SwitchPreference("argusos_copy_video_url_timestamp"),
        )

        copyResources(
            "copyvideourl",
            ResourceGroup(
                resourceDirectoryName = "drawable",
                "argusos_yt_copy.xml",
                "argusos_yt_copy_timestamp.xml",
            ),
        )

        addBottomControl("copyvideourl")
    }
}

@Suppress("unused")
val copyVideoURLPatch = bytecodePatch(
    name = "Copy video URL",
    description = "Adds options to display buttons in the video player to copy video URLs.",
) {
    dependsOn(
        copyVideoURLResourcePatch,
        playerControlsPatch,
        videoInformationPatch,
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
        val extensionPlayerPackage = "Lapp/argusos/extension/youtube/videoplayer"
        val buttonsDescriptors = listOf(
            "$extensionPlayerPackage/CopyVideoURLButton;",
            "$extensionPlayerPackage/CopyVideoURLTimestampButton;",
        )

        buttonsDescriptors.forEach { descriptor ->
            initializeBottomControl(descriptor)
            injectVisibilityCheckCall(descriptor)
        }
    }
}
