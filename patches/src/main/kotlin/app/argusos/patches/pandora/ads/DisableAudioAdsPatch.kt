package app.argusos.patches.pandora.ads

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val disableAudioAdsPatch = bytecodePatch("Disable Audio Ads") {
    compatibleWith("com.pandora.android")

    apply {
        getIsAdSupportedMethod.returnEarly(false)
        requestAudioAdMethod.returnEarly()
    }
}
