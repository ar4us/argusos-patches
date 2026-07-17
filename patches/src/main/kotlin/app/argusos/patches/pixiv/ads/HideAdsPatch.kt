package app.argusos.patches.pixiv.ads

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch("Hide ads") {
    compatibleWith("jp.pxv.android"("6.141.1"))

    apply {
        shouldShowAdsMethod.returnEarly(false)
    }
}
