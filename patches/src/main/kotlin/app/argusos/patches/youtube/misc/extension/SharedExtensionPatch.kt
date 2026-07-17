package app.argusos.patches.youtube.misc.extension

import app.argusos.patches.shared.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.extension.hooks.*

val sharedExtensionPatch = sharedExtensionPatch("youtube",
    applicationInitHook, applicationInitOnCrateHook)
