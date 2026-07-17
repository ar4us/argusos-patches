package app.argusos.patches.memegenerator.detection.signature

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

val signatureVerificationPatch = bytecodePatch(
    description = "Disables detection of incorrect signature.",
) {

    apply {
        verifySignatureMethod.returnEarly(true)
    }
}
