package app.argusos.patches.inshorts.ad

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch("Hide ads") {
    compatibleWith("com.nis.app")

    apply {
        inshortsAdsMethod.returnEarly()
    }
}
