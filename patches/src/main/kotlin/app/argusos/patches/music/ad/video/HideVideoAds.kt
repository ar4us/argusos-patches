package app.argusos.patches.music.ad.video

import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.music.misc.extension.sharedExtensionPatch
import app.argusos.patches.music.misc.settings.PreferenceScreen
import app.argusos.patches.music.misc.settings.settingsPatch
import app.argusos.patches.shared.misc.settings.preference.SwitchPreference

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/music/patches/HideVideoAdsPatch;"

@Suppress("unused")
val hideMusicVideoAdsPatch = bytecodePatch(
    name = "Hide music video ads",
    description = "Adds an option to hide ads that appear while listening to or streaming music videos, podcasts, or songs.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
        addResourcesPatch,
    )

    compatibleWith(
        "com.google.android.apps.youtube.music"(
            "7.29.52",
            "8.10.52",
            "8.37.56",
            "8.40.54",
        ),
    )

    apply {
        addResources("music", "ad.video.hideVideoAdsPatch")

        PreferenceScreen.ADS.addPreferences(
            SwitchPreference("argusos_music_hide_video_ads"),
        )

        navigate(showVideoAdsParentMethodMatch.immutableMethod)
            .to(showVideoAdsParentMethodMatch[0] + 1)
            .stop()
            .addInstructions(
                0,
                """
                    invoke-static { p1 }, $EXTENSION_CLASS_DESCRIPTOR->showVideoAds(Z)Z
                    move-result p1
                """,
            )
    }
}
