package app.argusos.patches.photomath.misc.annoyances

import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.photomath.detection.signature.signatureDetectionPatch

@Suppress("unused")
val hideUpdatePopupPatch = bytecodePatch(
    name = "Hide update popup",
    description = "Prevents the update popup from showing up.",
) {
    dependsOn(signatureDetectionPatch)

    compatibleWith("com.microblink.photomath")

    apply {
        hideUpdatePopupMethod.addInstructions(
            2, // Insert after the null check.
            "return-void",
        )
    }
}
