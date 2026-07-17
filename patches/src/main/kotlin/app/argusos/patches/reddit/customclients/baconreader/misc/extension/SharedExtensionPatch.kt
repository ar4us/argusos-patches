package app.argusos.patches.reddit.customclients.baconreader.misc.extension

import app.argusos.patches.reddit.customclients.baconreader.misc.extension.hooks.initHook
import app.argusos.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch("baconreader", initHook)
