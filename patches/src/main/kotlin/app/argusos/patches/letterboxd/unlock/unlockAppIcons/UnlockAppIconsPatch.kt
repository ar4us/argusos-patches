package app.argusos.patches.letterboxd.unlock.unlockAppIcons

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val unlockAppIconsPatch = bytecodePatch("Unlock app icons") {
    compatibleWith("com.letterboxd.letterboxd")

    apply {
        getCanChangeAppIconMethod.returnEarly(true)
    }
}
