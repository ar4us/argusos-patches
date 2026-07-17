package app.argusos.patches.nunl.ads

import app.argusos.patches.shared.misc.extension.activityOnCreateExtensionHook

internal val mainActivityOnCreateHook = activityOnCreateExtensionHook(
    "/NUApplication;"
)
