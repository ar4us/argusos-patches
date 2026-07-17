package app.argusos.patches.spotify.misc.extension

import app.argusos.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch(
    "spotify",
    mainActivityOnCreateHook,
    loadOrbitLibraryHook
)
