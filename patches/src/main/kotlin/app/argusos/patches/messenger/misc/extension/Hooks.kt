package app.argusos.patches.messenger.misc.extension

import app.argusos.patches.shared.misc.extension.activityOnCreateExtensionHook

internal val messengerApplicationOnCreateHook = activityOnCreateExtensionHook(
    "/MessengerApplication;"
)
