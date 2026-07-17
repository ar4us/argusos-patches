package app.argusos.patches.spotify.misc.check

import app.argusos.patches.shared.misc.checks.checkEnvironmentPatch
import app.argusos.patches.spotify.misc.extension.sharedExtensionPatch
import app.argusos.patches.spotify.shared.mainActivityOnCreateMethod

internal val checkEnvironmentPatch = checkEnvironmentPatch(
    getMainActivityOnCreateMethod = { mainActivityOnCreateMethod },
    extensionPatch = sharedExtensionPatch,
    "com.spotify.music",
)
