package app.argusos.patches.photomath.detection.deviceid

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.photomath.detection.signature.signatureDetectionPatch
import app.argusos.util.returnEarly
import kotlin.random.Random

@Suppress("unused")
val spoofDeviceIDPatch = bytecodePatch(
    name = "Spoof device ID",
    description = "Spoofs device ID to mitigate manual bans by developers.",
) {
    dependsOn(signatureDetectionPatch)

    compatibleWith("com.microblink.photomath")

    apply {
        getDeviceIdMethod.returnEarly(Random.nextLong().toString(16))
    }
}
