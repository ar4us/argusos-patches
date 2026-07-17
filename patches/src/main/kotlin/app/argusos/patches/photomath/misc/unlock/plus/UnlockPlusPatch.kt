package app.argusos.patches.photomath.misc.unlock.plus

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.photomath.detection.signature.signatureDetectionPatch
import app.argusos.patches.photomath.misc.unlock.bookpoint.enableBookpointPatch
import app.argusos.util.returnEarly

@Suppress("unused")
val unlockPlusPatch = bytecodePatch("Unlock plus") {
    dependsOn(signatureDetectionPatch, enableBookpointPatch)

    compatibleWith("com.microblink.photomath")

    apply {
        isPlusUnlockedMethod.returnEarly(true)
    }
}
