package app.argusos.patches.protonvpn.delay

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val removeDelayPatch = bytecodePatch(
    name = "Remove delay",
    description = "Removes the delay when changing servers.",
) {
    compatibleWith("ch.protonvpn.android")

    apply {
        longDelayMethod.returnEarly(0)
        shortDelayMethod.returnEarly(0)
    }
}
