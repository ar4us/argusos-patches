package app.argusos.patches.memegenerator.misc.pro

import app.revanced.patcher.extensions.replaceInstructions
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.memegenerator.detection.license.licenseValidationPatch
import app.argusos.patches.memegenerator.detection.signature.signatureVerificationPatch

@Suppress("unused")
val unlockProPatch = bytecodePatch("Unlock pro") {
    dependsOn(signatureVerificationPatch, licenseValidationPatch)

    compatibleWith("com.zombodroid.MemeGenerator"("4.6364", "4.6370", "4.6375", "4.6377"))

    apply {
        isFreeVersionMethod.replaceInstructions(
            0,
            """
                sget-object p0, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;
                return-object p0
            """,
        )
    }
}
