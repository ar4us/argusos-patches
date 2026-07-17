package app.argusos.patches.tiktok.misc.extension

import app.argusos.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch("tiktok", initHook, jatoInitHook, storeRegionInitHook)
