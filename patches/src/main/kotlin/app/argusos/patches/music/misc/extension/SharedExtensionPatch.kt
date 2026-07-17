package app.argusos.patches.music.misc.extension

import app.argusos.patches.music.misc.extension.hooks.applicationInitHook
import app.argusos.patches.music.misc.extension.hooks.applicationInitOnCreateHook
import app.argusos.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch(
    "music",
    applicationInitHook,
    applicationInitOnCreateHook
)
