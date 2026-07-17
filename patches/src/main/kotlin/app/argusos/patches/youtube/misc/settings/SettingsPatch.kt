package app.argusos.patches.youtube.misc.settings

import app.revanced.com.android.tools.smali.dexlib2.mutable.MutableClassDef
import app.revanced.com.android.tools.smali.dexlib2.mutable.MutableMethod
import app.revanced.com.android.tools.smali.dexlib2.mutable.MutableMethod.Companion.toMutable
import app.revanced.patcher.classDef
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.getInstruction
import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.argusos.patches.all.misc.packagename.setOrGetFallbackPackageName
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.boldIconsFeatureFlagMethodMatch
import app.argusos.patches.shared.misc.mapping.resourceMappingPatch
import app.argusos.patches.shared.misc.settings.overrideThemeColors
import app.argusos.patches.shared.misc.settings.preference.*
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference.Sorting
import app.argusos.patches.shared.misc.settings.settingsPatch
import app.argusos.patches.youtube.misc.check.checkEnvironmentPatch
import app.argusos.patches.youtube.misc.extension.sharedExtensionPatch
import app.argusos.patches.youtube.misc.fix.contentprovider.fixContentProviderPatch
import app.argusos.patches.youtube.misc.fix.playbackspeed.fixPlaybackSpeedWhilePlayingPatch
import app.argusos.patches.youtube.misc.playservice.is_19_34_or_greater
import app.argusos.patches.youtube.misc.playservice.is_20_31_or_greater
import app.argusos.patches.youtube.misc.playservice.versionCheckPatch
import app.argusos.util.*
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod
import com.android.tools.smali.dexlib2.immutable.ImmutableMethodParameter
import com.android.tools.smali.dexlib2.util.MethodUtil

private const val BASE_ACTIVITY_HOOK_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/shared/settings/BaseActivityHook;"
private const val YOUTUBE_ACTIVITY_HOOK_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/youtube/settings/YouTubeActivityHook;"

private val preferences = mutableSetOf<BasePreference>()

private val settingsResourcePatch = resourcePatch {
    dependsOn(
        resourceMappingPatch,
        settingsPatch(
            rootPreferences = listOf(
                IntentPreference(
                    titleKey = "argusos_settings_title",
                    summaryKey = null,
                    intent = newIntent("argusos_settings_intent"),
                ) to "settings_fragment",

                PreferenceCategory(
                    titleKey = "argusos_settings_title",
                    layout = "@layout/preference_group_title",
                    preferences = setOf(
                        IntentPreference(
                            titleKey = "argusos_settings_submenu_title",
                            summaryKey = null,
                            icon = "@drawable/argusos_settings_icon_dynamic",
                            layout = "@layout/preference_with_icon",
                            intent = newIntent("argusos_settings_intent"),
                        ),
                    ),
                ) to "settings_fragment_cairo",
            ),
            preferences = preferences,
        ),
    )

    apply {
        // Use same colors as stock YouTube.
        overrideThemeColors("@color/yt_white1", "@color/yt_black3")

        copyResources(
            "settings",
            ResourceGroup(
                "drawable",
                "argusos_settings_icon_dynamic.xml",
                "argusos_settings_icon.xml",
                "argusos_settings_icon_bold.xml",
                "argusos_settings_screen_00_about.xml",
                "argusos_settings_screen_00_about_bold.xml",
                "argusos_settings_screen_01_ads.xml",
                "argusos_settings_screen_01_ads_bold.xml",
                "argusos_settings_screen_02_alt_thumbnails.xml",
                "argusos_settings_screen_02_alt_thumbnails_bold.xml",
                "argusos_settings_screen_03_feed.xml",
                "argusos_settings_screen_03_feed_bold.xml",
                "argusos_settings_screen_04_general.xml",
                "argusos_settings_screen_04_general_bold.xml",
                "argusos_settings_screen_05_player.xml",
                "argusos_settings_screen_05_player_bold.xml",
                "argusos_settings_screen_06_shorts.xml",
                "argusos_settings_screen_06_shorts_bold.xml",
                "argusos_settings_screen_07_seekbar.xml",
                "argusos_settings_screen_07_seekbar_bold.xml",
                "argusos_settings_screen_08_swipe_controls.xml",
                "argusos_settings_screen_08_swipe_controls_bold.xml",
                "argusos_settings_screen_09_return_youtube_dislike.xml",
                "argusos_settings_screen_09_return_youtube_dislike_bold.xml",
                "argusos_settings_screen_10_sponsorblock.xml",
                "argusos_settings_screen_10_sponsorblock_bold.xml",
                "argusos_settings_screen_11_misc.xml",
                "argusos_settings_screen_11_misc_bold.xml",
                "argusos_settings_screen_12_video.xml",
                "argusos_settings_screen_12_video_bold.xml",
            ),
        )

        // Remove horizontal divider from the settings Preferences
        // To better match the appearance of the stock YouTube settings.
        document("res/values/styles.xml").use { document ->
            val childNodes = document.childNodes

            arrayOf(
                "Theme.YouTube.Settings",
                "Theme.YouTube.Settings.Dark",
            ).forEach { value ->
                val listDividerNode = document.createElement("item")
                listDividerNode.setAttribute("name", "android:listDivider")
                listDividerNode.appendChild(document.createTextNode("@null"))

                childNodes.findElementByAttributeValueOrThrow(
                    "name",
                    value,
                ).appendChild(listDividerNode)
            }
        }

        // Modify the manifest to enhance LicenseActivity behavior:
        // 1. Add a data intent filter with MIME type "text/plain".
        //    Some devices crash if undeclared data is passed to an intent,
        //    and this change appears to fix the issue.
        // 2. Add android:configChanges="orientation|screenSize|keyboardHidden".
        //    This prevents the activity from being recreated on configuration changes
        //    (e.g., screen rotation), preserving its current state and fragment.
        document("AndroidManifest.xml").use { document ->
            val licenseElement = document.childNodes.findElementByAttributeValueOrThrow(
                "android:name",
                "com.google.android.libraries.social.licenses.LicenseActivity",
            )

            licenseElement.setAttribute(
                "android:configChanges",
                "orientation|screenSize|keyboardHidden",
            )

            val mimeType = document.createElement("data")
            mimeType.setAttribute("android:mimeType", "text/plain")

            val intentFilter = document.createElement("intent-filter")
            intentFilter.appendChild(mimeType)

            licenseElement.appendChild(intentFilter)
        }
    }
}

val settingsPatch = bytecodePatch(
    description = "Adds settings for ArgusOS to YouTube.",
) {
    dependsOn(
        sharedExtensionPatch,
        settingsResourcePatch,
        addResourcesPatch,
        versionCheckPatch,
        fixPlaybackSpeedWhilePlayingPatch,
        fixContentProviderPatch,
        // Currently there is no easy way to make a mandatory patch,
        // so for now this is a dependent of this patch.
        checkEnvironmentPatch,
    )

    apply {
        addResources("youtube", "misc.settings.settingsPatch")

        // Add an "about" preference to the top.
        preferences += NonInteractivePreference(
            key = "argusos_settings_screen_00_about",
            icon = "@drawable/argusos_settings_screen_00_about",
            iconBold = "@drawable/argusos_settings_screen_00_about_bold",
            layout = "@layout/preference_with_icon",
            summaryKey = null,
            tag = "app.argusos.extension.shared.settings.preference.ArgusOSAboutPreference",
            selectable = true,
        )

        if (is_19_34_or_greater) {
            PreferenceScreen.GENERAL.addPreferences(
                SwitchPreference("argusos_restore_old_settings_menus"),
            )
        }

        PreferenceScreen.GENERAL.addPreferences(
            SwitchPreference("argusos_settings_search_history"),
        )

        PreferenceScreen.GENERAL.addPreferences(
            if (is_20_31_or_greater) {
                PreferenceCategory(
                    titleKey = null,
                    sorting = Sorting.UNSORTED,
                    tag = "app.argusos.extension.shared.settings.preference.NoTitlePreferenceCategory",
                    preferences = setOf(
                        SwitchPreference("argusos_show_menu_icons"),
                        SwitchPreference("argusos_settings_disable_bold_icons"),
                    ),
                )
            } else {
                SwitchPreference("argusos_show_menu_icons")
            },
        )

        PreferenceScreen.MISC.addPreferences(
            TextPreference(
                key = null,
                titleKey = "argusos_pref_import_export_title",
                summaryKey = "argusos_pref_import_export_summary",
                inputType = InputType.TEXT_MULTI_LINE,
                tag = "app.argusos.extension.shared.settings.preference.ImportExportPreference",
            ),
            ListPreference(
                key = "argusos_language",
                tag = "app.argusos.extension.shared.settings.preference.SortedListPreference",
            ),
        )

        // Update shared dark mode status based on YT theme.
        // This is needed because YT allows forcing light/dark mode
        // which then differs from the system dark mode status.
        setThemeMethod.apply {
            findInstructionIndicesReversedOrThrow(Opcode.RETURN_OBJECT).forEach { index ->
                val register = getInstruction<OneRegisterInstruction>(index).registerA
                addInstructionsAtControlFlowLabel(
                    index,
                    "invoke-static { v$register }, ${YOUTUBE_ACTIVITY_HOOK_CLASS_DESCRIPTOR}->updateLightDarkModeStatus(Ljava/lang/Enum;)V",
                )
            }
        }

        // Add setting to force Cairo settings fragment on/off.
        cairoFragmentConfigMethodMatch.method.insertLiteralOverride(
            cairoFragmentConfigMethodMatch[0],
            "$YOUTUBE_ACTIVITY_HOOK_CLASS_DESCRIPTOR->useCairoSettingsFragment(Z)Z",
        )

        // Bold icon resources are found starting in 20.23, but many YT icons are not bold.
        // 20.31 is the first version that seems to have all the bold icons.
        if (is_20_31_or_greater) {
            boldIconsFeatureFlagMethodMatch.let {
                it.method.insertLiteralOverride(
                    it[0],
                    "$YOUTUBE_ACTIVITY_HOOK_CLASS_DESCRIPTOR->useBoldIcons(Z)Z",
                )
            }
        }

        modifyActivityForSettingsInjection(
            licenseActivityOnCreateMethod.classDef,
            licenseActivityOnCreateMethod,
            YOUTUBE_ACTIVITY_HOOK_CLASS_DESCRIPTOR,
            false,
        )
    }

    afterDependents {
        PreferenceScreen.close()
    }
}

/**
 * Modifies the activity to show ArgusOS settings instead of its original purpose.
 */
internal fun modifyActivityForSettingsInjection(
    activityOnCreateClass: MutableClassDef,
    activityOnCreateMethod: MutableMethod,
    extensionClassType: String,
    isYouTubeMusic: Boolean,
) {
    // Modify Activity and remove all existing layout code.
    // Must modify an existing activity and cannot add a new activity to the manifest,
    // as that fails for root installations.
    activityOnCreateMethod.addInstructions(
        0,
        """
            invoke-super { p0, p1 }, ${activityOnCreateClass.superclass}->onCreate(Landroid/os/Bundle;)V
            invoke-static { p0 }, $extensionClassType->initialize(Landroid/app/Activity;)V
            return-void
        """,
    )

    // Remove other methods as they will break as the onCreate method is modified above.
    activityOnCreateClass.apply {
        methods.removeIf { it != activityOnCreateMethod && !MethodUtil.isConstructor(it) }
    }

    // Override base context to allow using ArgusOS specific settings.
    ImmutableMethod(
        activityOnCreateClass.type,
        "attachBaseContext",
        listOf(ImmutableMethodParameter("Landroid/content/Context;", null, null)),
        "V",
        AccessFlags.PROTECTED.value,
        null,
        null,
        MutableMethodImplementation(3),
    ).toMutable().apply {
        addInstructions(
            """
                invoke-static { p1 }, $BASE_ACTIVITY_HOOK_CLASS_DESCRIPTOR->getAttachBaseContext(Landroid/content/Context;)Landroid/content/Context;
                move-result-object p1
                invoke-super { p0, p1 }, ${activityOnCreateClass.superclass}->attachBaseContext(Landroid/content/Context;)V
                return-void
            """,
        )
    }.let(activityOnCreateClass.methods::add)

    // Override finish() to intercept back gesture.
    ImmutableMethod(
        activityOnCreateClass.type,
        if (isYouTubeMusic) "finish" else "onBackPressed",
        emptyList(),
        "V",
        AccessFlags.PUBLIC.value,
        null,
        null,
        MutableMethodImplementation(3),
    ).toMutable().apply {
        // Slightly different hooks are needed, otherwise the back button can behave wrong.
        val extensionMethodName = if (isYouTubeMusic) "handleFinish" else "handleBackPress"
        val invokeFinishOpcode = if (isYouTubeMusic) "invoke-super" else "invoke-virtual"

        addInstructions(
            """
                invoke-static {}, $extensionClassType->$extensionMethodName()Z
                move-result v0
                if-nez v0, :search_handled
                $invokeFinishOpcode { p0 }, Landroid/app/Activity;->finish()V
                :search_handled
                return-void
            """,
        )
    }.let(activityOnCreateClass.methods::add)
}

/**
 * Creates an intent to open ArgusOS settings.
 */
fun newIntent(settingsName: String) = IntentPreference.Intent(
    data = settingsName,
    targetClass = "com.google.android.libraries.social.licenses.LicenseActivity",
) {
    // The package name change has to be reflected in the intent.
    setOrGetFallbackPackageName("com.google.android.youtube")
}

object PreferenceScreen : BasePreferenceScreen() {
    // Sort screens in the root menu by key, to not scatter related items apart
    // (sorting key is set in argusos_prefs.xml).
    // If no preferences are added to a screen, the screen will not be added to the settings.
    val ADS = Screen(
        key = "argusos_settings_screen_01_ads",
        summaryKey = null,
        icon = "@drawable/argusos_settings_screen_01_ads",
        iconBold = "@drawable/argusos_settings_screen_01_ads_bold",
        layout = "@layout/preference_with_icon",
    )
    val ALTERNATIVE_THUMBNAILS = Screen(
        key = "argusos_settings_screen_02_alt_thumbnails",
        summaryKey = null,
        icon = "@drawable/argusos_settings_screen_02_alt_thumbnails",
        iconBold = "@drawable/argusos_settings_screen_02_alt_thumbnails_bold",
        layout = "@layout/preference_with_icon",
        sorting = Sorting.UNSORTED,
    )
    val FEED = Screen(
        key = "argusos_settings_screen_03_feed",
        summaryKey = null,
        icon = "@drawable/argusos_settings_screen_03_feed",
        iconBold = "@drawable/argusos_settings_screen_03_feed_bold",
        layout = "@layout/preference_with_icon",
    )
    val GENERAL = Screen(
        key = "argusos_settings_screen_04_general",
        summaryKey = null,
        icon = "@drawable/argusos_settings_screen_04_general",
        iconBold = "@drawable/argusos_settings_screen_04_general_bold",
        layout = "@layout/preference_with_icon",
    )
    val PLAYER = Screen(
        key = "argusos_settings_screen_05_player",
        summaryKey = null,
        icon = "@drawable/argusos_settings_screen_05_player",
        iconBold = "@drawable/argusos_settings_screen_05_player_bold",
        layout = "@layout/preference_with_icon",
    )
    val SHORTS = Screen(
        key = "argusos_settings_screen_06_shorts",
        summaryKey = null,
        icon = "@drawable/argusos_settings_screen_06_shorts",
        iconBold = "@drawable/argusos_settings_screen_06_shorts_bold",
        layout = "@layout/preference_with_icon",
    )
    val SEEKBAR = Screen(
        key = "argusos_settings_screen_07_seekbar",
        summaryKey = null,
        icon = "@drawable/argusos_settings_screen_07_seekbar",
        iconBold = "@drawable/argusos_settings_screen_07_seekbar_bold",
        layout = "@layout/preference_with_icon",
    )
    val SWIPE_CONTROLS = Screen(
        key = "argusos_settings_screen_08_swipe_controls",
        summaryKey = null,
        icon = "@drawable/argusos_settings_screen_08_swipe_controls",
        iconBold = "@drawable/argusos_settings_screen_08_swipe_controls_bold",
        layout = "@layout/preference_with_icon",
        sorting = Sorting.UNSORTED,
    )
    val RETURN_YOUTUBE_DISLIKE = Screen(
        key = "argusos_settings_screen_09_return_youtube_dislike",
        summaryKey = null,
        icon = "@drawable/argusos_settings_screen_09_return_youtube_dislike",
        iconBold = "@drawable/argusos_settings_screen_09_return_youtube_dislike_bold",
        layout = "@layout/preference_with_icon",
        sorting = Sorting.UNSORTED,
    )
    val SPONSORBLOCK = Screen(
        key = "argusos_settings_screen_10_sponsorblock",
        summaryKey = null,
        icon = "@drawable/argusos_settings_screen_10_sponsorblock",
        iconBold = "@drawable/argusos_settings_screen_10_sponsorblock_bold",
        layout = "@layout/preference_with_icon",
        sorting = Sorting.UNSORTED,
    )
    val MISC = Screen(
        key = "argusos_settings_screen_11_misc",
        summaryKey = null,
        icon = "@drawable/argusos_settings_screen_11_misc",
        iconBold = "@drawable/argusos_settings_screen_11_misc_bold",
        layout = "@layout/preference_with_icon",
    )
    val VIDEO = Screen(
        key = "argusos_settings_screen_12_video",
        summaryKey = null,
        icon = "@drawable/argusos_settings_screen_12_video",
        iconBold = "@drawable/argusos_settings_screen_12_video_bold",
        layout = "@layout/preference_with_icon",
        sorting = Sorting.BY_KEY,
    )

    override fun commit(screen: PreferenceScreenPreference) {
        preferences += screen
    }
}
