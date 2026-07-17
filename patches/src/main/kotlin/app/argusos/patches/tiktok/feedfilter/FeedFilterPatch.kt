package app.argusos.patches.tiktok.feedfilter

import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.instructions
import app.revanced.patcher.patch.bytecodePatch
import app.argusos.patches.tiktok.misc.extension.sharedExtensionPatch
import app.argusos.patches.tiktok.misc.settings.settingsPatch
import app.argusos.patches.tiktok.misc.settings.settingsStatusLoadMethod
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

private const val EXTENSION_CLASS_DESCRIPTOR = "Lapp/argusos/extension/tiktok/feedfilter/FeedItemsFilter;"

@Suppress("unused")
val feedFilterPatch = bytecodePatch(
    name = "Feed filter",
    description = "Removes ads, livestreams, stories, image videos " +
        "and videos with a specific amount of views or likes from the feed.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsPatch,
    )

    compatibleWith(
        "com.ss.android.ugc.trill"("36.5.4"),
        "com.zhiliaoapp.musically"("36.5.4"),
    )

    apply {
        arrayOf(
            feedApiServiceLIZMethod to "$EXTENSION_CLASS_DESCRIPTOR->filter(Lcom/ss/android/ugc/aweme/feed/model/FeedItemList;)V",
            followFeedMethod to "$EXTENSION_CLASS_DESCRIPTOR->filter(Lcom/ss/android/ugc/aweme/follow/presenter/FollowFeedList;)V",
        ).forEach { (method, filterSignature) ->
            val returnInstruction = method.instructions.first { it.opcode == Opcode.RETURN_OBJECT }
            val register = (returnInstruction as OneRegisterInstruction).registerA
            method.addInstruction(
                returnInstruction.location.index,
                "invoke-static { v$register }, $filterSignature",
            )
        }

        settingsStatusLoadMethod.addInstruction(
            0,
            "invoke-static {}, Lapp/argusos/extension/tiktok/settings/SettingsStatus;->enableFeedFilter()V",
        )
    }
}
