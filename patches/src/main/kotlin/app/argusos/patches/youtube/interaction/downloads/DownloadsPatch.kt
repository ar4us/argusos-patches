package app.argusos.patches.youtube.interaction.downloads

import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.addInstructionsWithLabels
import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference.Sorting
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.shared.misc.settings.preference.TextPreference
import app.argusos.patches.youtube.misc.playercontrols.addBottomControl
import app.argusos.patches.youtube.misc.playercontrols.initializeBottomControl
import app.argusos.patches.youtube.misc.playercontrols.injectVisibilityCheckCall
import app.argusos.patches.youtube.misc.playercontrols.playerControlsPatch
import app.argusos.patches.youtube.misc.settings.PreferenceScreen
import app.argusos.patches.youtube.misc.settings.settingsPatch
import app.argusos.patches.youtube.shared.mainActivityOnCreateMethod
import app.argusos.patches.youtube.video.information.videoInformationPatch
import app.argusos.util.ResourceGroup
import app.argusos.util.copyResources

private val downloadsResourcePatch = resourcePatch {
    dependsOn(
        playerControlsPatch,
        settingsPatch,
        addResourcesPatch,
    )

    apply {
        addResources("youtube", "interaction.downloads.downloadsResourcePatch")

        PreferenceScreen.PLAYER.addPreferences(
            PreferenceScreenPreference(
                key = "argusos_external_downloader_screen",
                sorting = Sorting.UNSORTED,
                preferences = setOf(
                    SwitchPreference("argusos_external_downloader"),
                    SwitchPreference("argusos_external_downloader_action_button"),
                    TextPreference(
                        "argusos_external_downloader_name",
                        tag = "app.argusos.extension.youtube.settings.preference.ExternalDownloaderPreference",
                    ),
                ),
            ),
        )

        copyResources(
            "downloads",
            ResourceGroup("drawable", "argusos_yt_download_button.xml"),
        )

        addBottomControl("downloads")
    }
}

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/patches/DownloadsPatch;"

internal const val BUTTON_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/videoplayer/ExternalDownloadButton;"

@Suppress("unused")
val downloadsPatch = bytecodePatch(
    name = "Downloads",
    description = "Adds support to download videos with an external downloader app " +
            "using the in-app download button or a video player action button.",
) {
    dependsOn(
        downloadsResourcePatch,
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
        initializeBottomControl(BUTTON_DESCRIPTOR)
        injectVisibilityCheckCall(BUTTON_DESCRIPTOR)

        // Main activity is used to launch downloader intent.
        mainActivityOnCreateMethod.addInstruction(
            0,
            "invoke-static/range { p0 .. p0 }, ${EXTENSION_CLASS_DESCRIPTOR}->setMainActivity(Landroid/app/Activity;)V",
        )

        offlineVideoEndpointMethod.apply {
            addInstructionsWithLabels(
                0,
                """
                    invoke-static/range {p3 .. p3}, $EXTENSION_CLASS_DESCRIPTOR->inAppDownloadButtonOnClick(Ljava/lang/String;)Z
                    move-result v0
                    if-eqz v0, :show_native_downloader
                    return-void
                    :show_native_downloader
                    nop
                """,
            )
        }
    }
}
