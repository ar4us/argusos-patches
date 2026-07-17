package app.argusos.patches.twitch.ad.audio

import app.revanced.patcher.extensions.ExternalLabel
import app.revanced.patcher.extensions.addInstructionsWithLabels
import app.revanced.patcher.extensions.getInstruction
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference
import app.argusos.patches.twitch.misc.extension.sharedExtensionPatch
import app.argusos.patches.twitch.misc.settings.PreferenceScreen
import app.argusos.patches.twitch.misc.settings.settingsPatch

@Suppress("unused")
val blockAudioAdsPatch = bytecodePatch(
    name = "Block audio ads",
    description = "Blocks audio ads in streams and VODs.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
        addResourcesPatch,
    )

    compatibleWith("tv.twitch.android.app"("16.9.1", "25.3.0"))

    apply {
        addResources("twitch", "ad.audio.audioAdsPatch")

        PreferenceScreen.ADS.CLIENT_SIDE.addPreferences(
            SwitchPreference("argusos_block_audio_ads"),
        )

        // Block playAds call
        audioAdsPresenterPlayMethod.addInstructionsWithLabels(
            0,
            """
                    invoke-static { }, Lapp/argusos/extension/twitch/patches/AudioAdsPatch;->shouldBlockAudioAds()Z
                    move-result v0
                    if-eqz v0, :show_audio_ads
                    return-void
                """,
            ExternalLabel("show_audio_ads", audioAdsPresenterPlayMethod.getInstruction(0)),
        )
    }
}
