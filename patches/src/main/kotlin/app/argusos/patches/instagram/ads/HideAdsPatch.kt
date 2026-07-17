package app.argusos.patches.instagram.ads

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.meta.ads.adInjectorMethod
import app.argusos.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch("Hide ads") {
    compatibleWith("com.instagram.android")

    apply {
        adInjectorMethod.returnEarly(false)
    }
}
