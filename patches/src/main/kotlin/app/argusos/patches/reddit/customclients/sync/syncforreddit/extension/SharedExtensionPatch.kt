package app.argusos.patches.reddit.customclients.sync.syncforreddit.extension

import app.argusos.patches.reddit.customclients.sync.syncforreddit.extension.hooks.initHook
import app.argusos.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch("syncforreddit", initHook)
