package app.argusos.patches.googlenews.misc.extension

import app.argusos.patches.googlenews.misc.extension.hooks.startActivityInitHook
import app.argusos.patches.shared.misc.extension.sharedExtensionPatch

val extensionPatch = sharedExtensionPatch(startActivityInitHook)
