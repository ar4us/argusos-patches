package app.argusos.patches.googlenews.misc.gms

import app.revanced.patcher.extensions.methodReference
import app.revanced.patcher.patch.BytecodePatchContext
import app.revanced.patcher.patch.Option
import app.argusos.patches.googlenews.misc.extension.extensionPatch
import app.argusos.patches.googlenews.misc.gms.Constants.MAGAZINES_PACKAGE_NAME
import app.argusos.patches.googlenews.misc.gms.Constants.ARGUSOS_MAGAZINES_PACKAGE_NAME
import app.argusos.patches.shared.misc.gms.gmsCoreSupportPatch
import app.argusos.patches.shared.misc.gms.gmsCoreSupportResourcePatch
import app.argusos.util.indexOfFirstInstructionOrThrow

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    fromPackageName = MAGAZINES_PACKAGE_NAME,
    toPackageName = ARGUSOS_MAGAZINES_PACKAGE_NAME,
    getMainActivityOnCreateMethodToGetInsertIndex = BytecodePatchContext::magazinesActivityOnCreateMethod::get to {
        val getApplicationContextIndex =
            magazinesActivityOnCreateMethod.indexOfFirstInstructionOrThrow {
                methodReference?.name == "getApplicationContext"
            }

        getApplicationContextIndex + 2 // Below the move-result-object instruction.
    },
    extensionPatch = extensionPatch,
    gmsCoreSupportResourcePatchFactory = ::gmsCoreSupportResourcePatch,
) {
    // Remove version constraint,
    // once https://github.com/ArgusOS/argusos-patches/pull/3111#issuecomment-2240877277 is resolved.
    compatibleWith(MAGAZINES_PACKAGE_NAME("5.108.0.644447823"))
}

private fun gmsCoreSupportResourcePatch(
    gmsCoreVendorGroupIdOption: Option<String>,
) = gmsCoreSupportResourcePatch(
    fromPackageName = MAGAZINES_PACKAGE_NAME,
    toPackageName = ARGUSOS_MAGAZINES_PACKAGE_NAME,
    spoofedPackageSignature = "24bb24c05e47e0aefa68a58a766179d9b613a666",
    gmsCoreVendorGroupIdOption = gmsCoreVendorGroupIdOption,
)
