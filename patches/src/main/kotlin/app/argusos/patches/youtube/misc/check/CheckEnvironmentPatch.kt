package app.argusos.patches.youtube.misc.check

import app.argusos.patches.shared.misc.checks.checkEnvironmentPatch
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.shared.mainActivityOnCreateMethod

internal val checkEnvironmentPatch = checkEnvironmentPatch(
    getMainActivityOnCreateMethod = { mainActivityOnCreateMethod },
    extensionPatch = sharedExtensionPatch,
    "com.google.android.youtube",
)
