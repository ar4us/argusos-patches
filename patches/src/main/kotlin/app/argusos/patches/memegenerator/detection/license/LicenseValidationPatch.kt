package app.argusos.patches.memegenerator.detection.license

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

val licenseValidationPatch = bytecodePatch(
    description = "Disables Firebase license validation.",
) {

    apply {
        licenseValidationMethod.returnEarly(true)
    }
}
