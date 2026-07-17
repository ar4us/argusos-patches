package app.argusos.patches.spotify.misc.lyrics

import app.revanced.patcher.gettingFirstImmutableMethod
import app.revanced.patcher.gettingFirstImmutableMethodDeclaratively
import app.revanced.patcher.parameterTypes
import app.revanced.patcher.patch.BytecodePatchContext
import app.revanced.patcher.returnType
import app.argusos.util.getReference
import app.argusos.util.indexOfFirstInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

internal val BytecodePatchContext.httpClientBuilderMethod by gettingFirstImmutableMethod("client == null", "scheduler == null")
