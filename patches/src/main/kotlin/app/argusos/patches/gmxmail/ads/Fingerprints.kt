package app.argusos.patches.gmxmail.ads

import app.revanced.patcher.fingerprint

internal val getAdvertisementStatusFingerprint = fingerprint {
    custom { method, classDef ->
        method.name == "getAdvertisementStatus" && classDef.endsWith("/PayMailManager;")
    }
}