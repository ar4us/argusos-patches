package app.argusos.patches.instagram.misc.extension.hooks

import app.argusos.patches.shared.misc.extension.activityOnCreateExtensionHook

internal val applicationInitHook = activityOnCreateExtensionHook(
    "/InstagramAppShell;"
)
