package app.argusos.patches.reddit.customclients.joeyforreddit.detection.piracy

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val disablePiracyDetectionPatch = bytecodePatch {
    apply {
        detectPiracyMethod.returnEarly()
    }
}
