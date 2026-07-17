package app.argusos.patches.music.misc.litho.filter

import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.patch.BytecodePatchContext
import app.argusos.patches.music.misc.extension.sharedExtensionPatch
import app.argusos.patches.music.shared.conversionContextToStringMethod
import app.argusos.patches.shared.misc.litho.filter.EXTENSION_CLASS_DESCRIPTOR
import app.argusos.patches.shared.misc.litho.filter.lithoFilterPatch
import app.argusos.patches.shared.misc.litho.filter.protobufBufferReferenceLegacyMethod
import app.argusos.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.Opcode

val lithoFilterPatch = lithoFilterPatch(
    componentCreateInsertionIndex = {
        // No supported version clobbers p2 so we can just do our things before the return instruction.
        indexOfFirstInstructionOrThrow(Opcode.RETURN_OBJECT)
    },
    getConversionContextToStringMethod = BytecodePatchContext::conversionContextToStringMethod::get,
    insertProtobufHook = {
        protobufBufferReferenceLegacyMethod.addInstruction(
            0,
            "invoke-static { p2 }, $EXTENSION_CLASS_DESCRIPTOR->setProtoBuffer(Ljava/nio/ByteBuffer;)V",
        )
    },
) {
    dependsOn(sharedExtensionPatch)
}
