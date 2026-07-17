package app.argusos.patches.googlephotos.misc.gms

import app.revanced.patcher.extensions.methodReference
import app.revanced.patcher.patch.BytecodePatchContext
import app.revanced.patcher.patch.Option
import app.argusos.patches.googlephotos.misc.extension.extensionPatch
import app.argusos.patches.googlephotos.misc.gms.Constants.PHOTOS_PACKAGE_NAME
import app.argusos.patches.googlephotos.misc.gms.Constants.ARGUSOS_PHOTOS_PACKAGE_NAME
import app.argusos.patches.shared.misc.gms.gmsCoreSupportPatch
import app.argusos.util.getReference
import app.argusos.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import app.argusos.util.getReference
import app.argusos.util.indexOfFirstInstructionOrThrow

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    fromPackageName = PHOTOS_PACKAGE_NAME,
    toPackageName = ARGUSOS_PHOTOS_PACKAGE_NAME,
    getMainActivityOnCreateMethodToGetInsertIndex = BytecodePatchContext::homeActivityOnCreateMethod::get to {
        val index = homeActivityOnCreateMethod.indexOfFirstInstructionOrThrow {
            methodReference?.name == "getApplicationContext"
        }

        // Below the move-result-object instruction,
        // because the extension patch is used by the GmsCore support patch
        // which hooks the getApplicationContext call.
        index + 2
    },
    extensionPatch = extensionPatch,
    gmsCoreSupportResourcePatchFactory = ::gmsCoreSupportResourcePatch,
) {
    compatibleWith(PHOTOS_PACKAGE_NAME)
}

private fun gmsCoreSupportResourcePatch(
    gmsCoreVendorGroupIdOption: Option<String>,
) = app.argusos.patches.shared.misc.gms.gmsCoreSupportResourcePatch(
    fromPackageName = PHOTOS_PACKAGE_NAME,
    toPackageName = ARGUSOS_PHOTOS_PACKAGE_NAME,
    spoofedPackageSignature = "24bb24c05e47e0aefa68a58a766179d9b613a600",
    gmsCoreVendorGroupIdOption = gmsCoreVendorGroupIdOption,
)
