package app.argusos.patches.cieid.restrictions.root

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val bypassRootChecksPatch = bytecodePatch(
    name = "Bypass root checks",
    description = "Removes the restriction to use the app with root permissions or on a custom ROM.",
) {
    compatibleWith("it.ipzs.cieid")

    apply {
        checkRootMethod.returnEarly()
    }
}
