package app.argusos.patches.nothingx.misc.extension

import app.revanced.patcher.definingClass
import app.revanced.patcher.name
import app.argusos.patches.shared.misc.extension.extensionHook
import app.argusos.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch(
    extensionName = "nothingx",
    extensionHook {
        name("onCreate")
        definingClass("BaseApplication")
    },
)
