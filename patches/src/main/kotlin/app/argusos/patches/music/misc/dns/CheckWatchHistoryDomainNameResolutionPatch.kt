package app.argusos.patches.music.misc.dns

import app.argusos.patches.music.misc.extension.sharedExtensionPatch
import app.argusos.patches.music.shared.mainActivityOnCreateMethod
import app.argusos.patches.shared.misc.dns.checkWatchHistoryDomainNameResolutionPatch

val checkWatchHistoryDomainNameResolutionPatch = checkWatchHistoryDomainNameResolutionPatch(
    block = {
        dependsOn(
            sharedExtensionPatch
        )

        compatibleWith(
            "com.google.android.apps.youtube.music"(
                "7.29.52",
                "8.10.52",
                "8.37.56",
                "8.40.54"
            )
        )
    },

    getMainActivityMethod = { mainActivityOnCreateMethod }
)
