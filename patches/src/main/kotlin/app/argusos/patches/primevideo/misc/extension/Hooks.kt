package app.argusos.patches.primevideo.misc.extension

import app.argusos.patches.shared.misc.extension.activityOnCreateExtensionHook

internal val applicationInitHook = activityOnCreateExtensionHook(
    "/SplashScreenActivity;"
)
