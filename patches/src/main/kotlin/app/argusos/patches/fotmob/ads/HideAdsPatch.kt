package app.argusos.patches.fotmob.ads

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch(
    name = "Hide ads",
) {
    compatibleWith("com.mobilefootie.wc2010")

    apply {
        shouldDisplayAdsMethod.returnEarly(false)
    }
}
