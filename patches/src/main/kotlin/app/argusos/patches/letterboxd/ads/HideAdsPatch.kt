package app.argusos.patches.letterboxd.ads

import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch("Hide ads") {
    compatibleWith("com.letterboxd.letterboxd")

    apply {
        admobHelperSetShowAdsMethod.addInstruction(0, "const p1, 0x0")
        listOf(admobHelperShouldShowAdsMethod, filmFragmentShowAdsMethod, memberExtensionShowAdsMethod).forEach {
            it.returnEarly()
        }
    }
}
