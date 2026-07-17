package app.argusos.patches.reddit.customclients.sync.syncforreddit.extension.hooks

import app.argusos.patches.shared.misc.extension.activityOnCreateExtensionHook

internal val initHook = activityOnCreateExtensionHook(
    "Lcom/laurencedawson/reddit_sync/RedditApplication;"
)
