package app.argusos.patches.peacocktv.ads

import app.revanced.patcher.patch.bytecodePatch
import app.argusos.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch(
    name = "Hide ads",
    description = "Hides all video ads.",
) {
    compatibleWith("com.peacocktv.peacockandroid")

    apply {
        mediaTailerAdServiceMethod.returnEarly(false)
    }
}
