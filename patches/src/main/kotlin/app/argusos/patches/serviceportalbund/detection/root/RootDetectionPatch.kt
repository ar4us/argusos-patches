package app.argusos.patches.serviceportalbund.detection.root

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val removeRootDetectionPatch = bytecodePatch(
    name = "Remove root detection",
    description = "Removes the check for root permissions and unlocked bootloader.",
) {
    compatibleWith("at.gv.bka.serviceportal")

    apply {
        rootDetectionMethod.returnEarly()
    }
}
