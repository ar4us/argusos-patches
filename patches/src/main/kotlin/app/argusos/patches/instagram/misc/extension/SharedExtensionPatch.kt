package app.argusos.patches.instagram.misc.extension

import app.argusos.patches.instagram.misc.extension.hooks.applicationInitHook
import app.argusos.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch(
    "instagram",
    applicationInitHook,
)
