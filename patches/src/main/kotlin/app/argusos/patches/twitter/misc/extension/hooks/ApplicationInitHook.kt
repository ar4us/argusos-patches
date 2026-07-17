package app.argusos.patches.twitter.misc.extension.hooks

import app.revanced.patcher.definingClass
import app.revanced.patcher.name
import app.argusos.patches.shared.misc.extension.extensionHook

internal val applicationInitHook = extensionHook {
    name("onCreate")
    definingClass("Lcom/twitter/app/TwitterApplication;")
}
