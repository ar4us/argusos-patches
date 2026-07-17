package app.argusos.patches.strava.misc.extension

import app.revanced.patcher.definingClass
import app.revanced.patcher.name
import app.argusos.patches.shared.misc.extension.extensionHook

internal val applicationOnCreateHook = extensionHook {
    name("onCreate")
    definingClass("/StravaApplication;")
}
