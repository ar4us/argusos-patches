package app.argusos.patches.reddit.customclients.joeyforreddit.api

import app.argusos.patches.reddit.customclients.spoofClientPatch
import app.argusos.patches.reddit.customclients.sync.detection.piracy.disablePiracyDetectionPatch
import app.argusos.util.returnEarly

val spoofClientPatch = spoofClientPatch(redirectUri = "https://127.0.0.1:65023/authorize_callback") { clientIdOption ->
    dependsOn(disablePiracyDetectionPatch)

    compatibleWith(
        "o.o.joey",
        "o.o.joey.pro",
        "o.o.joey.dev",
    )

    val clientId by clientIdOption

    apply {
        // region Patch client id.

        getClientIdMethod.returnEarly(clientId!!)

        // endregion

        // region Patch user agent.

        // Use a random user agent.
        val randomName = (0..100000).random()
        val userAgent = "$randomName:app.argusos.$randomName:v1.0.0 (by /u/argusos)"

        authUtilityUserAgentMethod.returnEarly(userAgent)

        // endregion
    }
}
