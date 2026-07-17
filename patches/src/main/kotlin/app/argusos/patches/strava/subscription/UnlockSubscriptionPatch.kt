package app.argusos.patches.strava.subscription

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val unlockSubscriptionFeaturesPatch = bytecodePatch(
    name = "Unlock subscription features",
    description = "Unlocks \"Routes\", \"Matched Runs\" and \"Segment Efforts\".",
) {
    compatibleWith("com.strava")

    apply {
        getSubscribedMethod.returnEarly(true)
    }
}
