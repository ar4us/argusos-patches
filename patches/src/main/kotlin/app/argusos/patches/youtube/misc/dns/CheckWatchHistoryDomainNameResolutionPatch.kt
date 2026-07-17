package app.argusos.patches.youtube.misc.dns

import app.revanced.patcher.patch.BytecodePatchContext
import app.argusos.patches.shared.misc.dns.checkWatchHistoryDomainNameResolutionPatch
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.shared.mainActivityOnCreateMethod

val checkWatchHistoryDomainNameResolutionPatch = checkWatchHistoryDomainNameResolutionPatch(
    block = {
        dependsOn(
            sharedExtensionPatch,
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
    },
    getMainActivityMethod = BytecodePatchContext::mainActivityOnCreateMethod::get,
)
