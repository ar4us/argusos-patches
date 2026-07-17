package app.argusos.patches.kleinanzeigen.hide_pur

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val hidePurPatch = bytecodePatch(
    name = "Hide Pur",
    description = "Hides Pur (Ad Free Subscription) from Settings Menu.",
) {
    compatibleWith("com.ebay.kleinanzeigen")

    apply {
        getShowAdFreeSubscriptionFingerprint.returnEarly(false)
    }
}
