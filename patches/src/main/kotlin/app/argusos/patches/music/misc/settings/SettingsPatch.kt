package app.argusos.patches.music.misc.settings

import app.revanced.patcher.classDef
import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.argusos.patches.all.misc.packagename.setOrGetFallbackPackageName
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.music.misc.extension.sharedExtensionPatch
import app.argusos.patches.music.misc.gms.Constants.MUSIC_PACKAGE_NAME
import app.argusos.patches.music.playservice.is_8_40_or_greater
import app.argusos.patches.music.playservice.versionCheckPatch
import app.argusos.patches.shared.boldIconsFeatureFlagMethodMatch
import app.argusos.patches.shared.misc.mapping.resourceMappingPatch
import app.argusos.patches.shared.misc.settings.preference.*
import app.argusos.patches.shared.misc.settings.settingsPatch
import app.argusos.patches.youtube.misc.settings.modifyActivityForSettingsInjection
import app.argusos.util.copyXmlNode
import app.argusos.util.inputStreamFromBundledResource
import app.argusos.util.insertLiteralOverride

private const val MUSIC_ACTIVITY_HOOK_CLASS_DESCRIPTOR =
    "Lapp/argusos/extension/music/settings/MusicActivityHook;"

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
                ) to "settings_headers"
            ),
            preferences = preferences
        )
    )

    apply {

        // Set the style for the ArgusOS settings to follow the style of the music settings,
        // namely: action bar height, menu item padding and remove horizontal dividers.
        val targetResource = "values/styles.xml"
        inputStreamFromBundledResource(
            "settings/music",
            targetResource,
        )!!.let { inputStream ->
            "resources".copyXmlNode(
                document(inputStream),
                document("res/$targetResource"),
            ).close()
        }

        // Remove horizontal dividers from the music settings.
        val styleFile = get("res/values/styles.xml")
        styleFile.writeText(
            styleFile.readText()
                .replace(
                    "allowDividerAbove\">true",
                    "allowDividerAbove\">false"
                ).replace(
                    "allowDividerBelow\">true",
                    "allowDividerBelow\">false"
                )
        )
    }
}

val settingsPatch = bytecodePatch(
    description = "Adds settings for ArgusOS to YouTube Music."
) {
    dependsOn(
        sharedExtensionPatch,
        settingsResourcePatch,
        addResourcesPatch,
        versionCheckPatch
    )

    apply {
        addResources("music", "misc.settings.settingsPatch")
        addResources("shared", "misc.debugging.enableDebuggingPatch")

        // Add an "About" preference to the top.
        preferences += NonInteractivePreference(
            key = "argusos_settings_music_screen_0_about",
            summaryKey = null,
            tag = "app.argusos.extension.shared.settings.preference.ArgusOSAboutPreference",
            selectable = true,
        )

        PreferenceScreen.GENERAL.addPreferences(
            SwitchPreference("argusos_settings_search_history")
        )

        if (is_8_40_or_greater) {
            PreferenceScreen.GENERAL.addPreferences(
                SwitchPreference("argusos_settings_disable_bold_icons")
            )
        }

        PreferenceScreen.MISC.addPreferences(
            TextPreference(
                key = null,
                titleKey = "argusos_pref_import_export_title",
                summaryKey = "argusos_pref_import_export_summary",
                inputType = InputType.TEXT_MULTI_LINE,
                tag = "app.argusos.extension.shared.settings.preference.ImportExportPreference",
            )
        )

        modifyActivityForSettingsInjection(
            googleApiActivityMethod.classDef,
            googleApiActivityMethod,
            MUSIC_ACTIVITY_HOOK_CLASS_DESCRIPTOR,
            true
        )

        if (is_8_40_or_greater) {
            boldIconsFeatureFlagMethodMatch.let {
                it.method.insertLiteralOverride(
                    it[0],
                    "$MUSIC_ACTIVITY_HOOK_CLASS_DESCRIPTOR->useBoldIcons(Z)Z"
                )
            }
        }
    }

    afterDependents {
        PreferenceScreen.close()
    }
}

/**
 * Creates an intent to open ArgusOS settings.
 */
fun newIntent(settingsName: String) = IntentPreference.Intent(
    data = settingsName,
    targetClass = "com.google.android.gms.common.api.GoogleApiActivity"
) {
    // The package name change has to be reflected in the intent.
    setOrGetFallbackPackageName(MUSIC_PACKAGE_NAME)
}

object PreferenceScreen : BasePreferenceScreen() {
    val ADS = Screen(
        key = "argusos_settings_music_screen_1_ads",
        summaryKey = null
    )
    val GENERAL = Screen(
        key = "argusos_settings_music_screen_2_general",
        summaryKey = null
    )
    val PLAYER = Screen(
        key = "argusos_settings_music_screen_3_player",
        summaryKey = null
    )
    val MISC = Screen(
        key = "argusos_settings_music_screen_4_misc",
        summaryKey = null
    )

    override fun commit(screen: PreferenceScreenPreference) {
        preferences += screen
    }
}
