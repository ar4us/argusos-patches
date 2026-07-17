package app.argusos.patches.iconpackstudio.misc.pro

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val unlockProPatch = bytecodePatch("Unlock pro") {
    compatibleWith("ginlemon.iconpackstudio"("2.2 build 016"))

    apply {
        checkProMethod.returnEarly(true)
    }
}
