package app.argusos.patches.gmxmail.layout

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

val hidePremiumUpgradeButtonPatch = bytecodePatch(
    name = "Hide Premium upgrade button",
    description = "Hides the Premium upgrade button in the navigation drawer.",
) {
    compatibleWith("de.gmx.mobile.android.mail")

    execute {
        isUpsellingPossibleFingerprint.method.returnEarly(false)
    }
}
