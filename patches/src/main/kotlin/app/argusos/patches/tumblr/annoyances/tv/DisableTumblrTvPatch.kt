package app.argusos.patches.tumblr.annoyances.tv

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.tumblr.featureflags.addFeatureFlagOverride
import app.argusos.patches.tumblr.featureflags.overrideFeatureFlagsPatch

@Suppress("unused")
val disableTumblrTVPatch = bytecodePatch(
    name = "Disable Tumblr TV",
    description = "Removes the Tumblr TV navigation button from the bottom navigation bar.",
) {
    dependsOn(overrideFeatureFlagsPatch)

    compatibleWith("com.tumblr")

    apply {
        addFeatureFlagOverride("tumblrTvMobileNav", "false")
    }
}
