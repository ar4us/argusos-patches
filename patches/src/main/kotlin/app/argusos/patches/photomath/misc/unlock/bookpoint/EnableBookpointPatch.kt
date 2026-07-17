package app.argusos.patches.photomath.misc.unlock.bookpoint

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

val enableBookpointPatch = bytecodePatch(
    description = "Enables textbook access",
) {

    apply {
        isBookpointEnabledMethod.returnEarly(true) // TODO: CHECK IF THIS IS FINE IN REPLACEMENT OF replaceInstructions
    }
}
