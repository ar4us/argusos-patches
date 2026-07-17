package app.argusos.patches.reddit.customclients.boostforreddit.misc.extension

import app.argusos.patches.reddit.customclients.boostforreddit.misc.extension.hooks.initHook
import app.argusos.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch("boostforreddit", initHook)
