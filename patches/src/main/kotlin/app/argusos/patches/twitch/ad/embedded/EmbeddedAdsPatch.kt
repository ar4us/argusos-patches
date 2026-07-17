package app.argusos.patches.twitch.ad.embedded

import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.shared.misc.settings.preference.ListPreference
import app.argusos.patches.twitch.ad.video.blockVideoAdsPatch
import app.argusos.patches.twitch.misc.extension.sharedExtensionPatch
import app.argusos.patches.twitch.misc.settings.PreferenceScreen
import app.argusos.patches.twitch.misc.settings.settingsPatch

@Suppress("unused")
val blockEmbeddedAdsPatch = bytecodePatch(
    name = "Block embedded ads",
    description = "Blocks embedded stream ads using services like Luminous or PurpleAdBlocker.",
) {
    dependsOn(
        blockVideoAdsPatch,
        sharedExtensionPatch,
        settingsPatch,
    )

    compatibleWith("tv.twitch.android.app"("16.9.1", "25.3.0"))

    apply {
        addResources("twitch", "ad.embedded.embeddedAdsPatch")

        PreferenceScreen.ADS.SURESTREAM.addPreferences(
            ListPreference("argusos_block_embedded_ads"),
        )

        // Inject OkHttp3 application interceptor.
        createsUsherClientMethod.addInstructions(
            3,
            """
                invoke-static  {}, Lapp/argusos/extension/twitch/patches/EmbeddedAdsPatch;->createRequestInterceptor()Lapp/argusos/extension/twitch/api/RequestInterceptor;
                move-result-object v2
                invoke-virtual {v0, v2}, Lokhttp3/OkHttpClient${"$"}Builder;->addInterceptor(Lokhttp3/Interceptor;)Lokhttp3/OkHttpClient${"$"}Builder;
            """,
        )
    }
}
