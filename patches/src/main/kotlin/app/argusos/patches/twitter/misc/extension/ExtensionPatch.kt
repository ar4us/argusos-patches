package app.argusos.patches.twitter.misc.extension

import app.argusos.patches.shared.misc.extension.sharedExtensionPatch
import app.argusos.patches.twitter.misc.extension.hooks.applicationInitHook

val sharedExtensionPatch = sharedExtensionPatch("twitter", applicationInitHook)
