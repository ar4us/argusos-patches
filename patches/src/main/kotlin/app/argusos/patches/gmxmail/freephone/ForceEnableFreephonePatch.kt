package app.argusos.patches.gmxmail.freephone

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

val forceEnableFreePhonePatch = bytecodePatch(
    name = "Force enable FreePhone",
    description = "Enables the FreePhone menu in the navigation drawer even on devices that don't support eSIM.",
) {
    compatibleWith("de.gmx.mobile.android.mail")

    execute {
        isEuiccEnabledFingerprint.method.returnEarly(true)
    }
}