package app.argusos.patches.reddit.customclients.slide.api

import app.argusos.patches.reddit.customclients.spoofClientPatch
import app.argusos.util.returnEarly

val spoofClientPatch = spoofClientPatch(redirectUri = "http://www.ccrama.me") { clientIdOption ->
    compatibleWith("me.ccrama.redditslide")

    val clientId by clientIdOption

    apply {
        getClientIdMethod.returnEarly(clientId!!)
    }
}
