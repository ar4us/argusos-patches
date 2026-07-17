package app.argusos.patches.threads

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.meta.ads.adInjectorMethod
import app.argusos.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch("Hide ads") {
    compatibleWith("com.instagram.barcelona"("382.0.0.51.85"))

    apply {
        adInjectorMethod.returnEarly(false)
    }
}
