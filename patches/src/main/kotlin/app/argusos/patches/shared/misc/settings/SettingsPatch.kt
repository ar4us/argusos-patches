package app.argusos.patches.shared.misc.settings

import app.revanced.patcher.firstImmutableClassDef
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.argusos.patches.all.misc.resources.addResource
import app.argusos.patches.all.misc.resources.addResources
import app.argusos.patches.all.misc.resources.addResourcesPatch
import app.argusos.patches.shared.misc.extension.EXTENSION_CLASS_DESCRIPTOR
import app.argusos.patches.shared.layout.branding.addBrandLicensePatch
import app.argusos.patches.shared.misc.settings.preference.BasePreference
import app.argusos.patches.shared.misc.settings.preference.PreferenceCategory
import app.argusos.patches.shared.misc.settings.preference.PreferenceScreenPreference
import app.argusos.util.ResourceGroup
import app.argusos.util.copyResources
import app.argusos.util.getNode
import app.argusos.util.insertFirst
import app.argusos.util.returnEarly
import org.w3c.dom.Node

private var lightThemeColor: String? = null
private var darkThemeColor: String? = null

/**
 * Sets the default theme colors used in various ArgusOS specific settings menus.
 * By default, these colors are white and black, but instead can be set to the
 * same color the target app uses for its own settings.
 */
fun overrideThemeColors(lightThemeColorString: String?, darkThemeColorString: String) {
    lightThemeColor = lightThemeColorString
    darkThemeColor = darkThemeColorString
}

private val settingsColorPatch = bytecodePatch {
    afterDependents {
        val extensionClassDef = firstImmutableClassDef(EXTENSION_CLASS_DESCRIPTOR)
        if (lightThemeColor != null) {
            extensionClassDef.getThemeLightColorResourceNameMethod().returnEarly(lightThemeColor!!)
        }
        if (darkThemeColor != null) {
            extensionClassDef.getThemeDarkColorResourceNameMethod().returnEarly(darkThemeColor!!)
        }
    }
}

/**
 * A resource patch that adds settings to a settings fragment.
 *
 * @param rootPreferences List of intent preferences and the name of the fragment file to add it to.
 *                        File names that do not exist are ignored and not processed.
 * @param preferences A set of preferences to add to the ArgusOS fragment.
 */
fun settingsPatch(
    rootPreferences: List<Pair<BasePreference, String>>? = null,
    preferences: Set<BasePreference>,
) = resourcePatch {
    dependsOn(
        addResourcesPatch,
        settingsColorPatch,
        addBrandLicensePatch,
    )

    apply {
        copyResources(
            "settings",
            ResourceGroup(
                "xml",
                "argusos_prefs.xml",
                "argusos_prefs_icons.xml",
                "argusos_prefs_icons_bold.xml",
            ),
            ResourceGroup(
                "menu",
                "argusos_search_menu.xml",
            ),
            ResourceGroup(
                "drawable",
                // CustomListPreference resources.
                "argusos_ic_dialog_alert.xml",
                // Search resources.
                "argusos_settings_arrow_time.xml",
                "argusos_settings_arrow_time_bold.xml",
                "argusos_settings_custom_checkmark.xml",
                "argusos_settings_custom_checkmark_bold.xml",
                "argusos_settings_search_icon.xml",
                "argusos_settings_search_icon_bold.xml",
                "argusos_settings_search_remove.xml",
                "argusos_settings_search_remove_bold.xml",
                "argusos_settings_toolbar_arrow_left.xml",
                "argusos_settings_toolbar_arrow_left_bold.xml",
            ),
            ResourceGroup(
                "layout",
                "argusos_custom_list_item_checked.xml",
                // Color picker.
                "argusos_color_dot_widget.xml",
                "argusos_color_picker.xml",
                // Search.
                "argusos_preference_search_history_item.xml",
                "argusos_preference_search_history_screen.xml",
                "argusos_preference_search_no_result.xml",
                "argusos_preference_search_result_color.xml",
                "argusos_preference_search_result_group_header.xml",
                "argusos_preference_search_result_list.xml",
                "argusos_preference_search_result_regular.xml",
                "argusos_preference_search_result_switch.xml",
                "argusos_settings_with_toolbar.xml",
            ),
        )

        addResources("shared", "misc.settings.settingsResourcePatch")
    }

    afterDependents {
        fun Node.addPreference(preference: BasePreference) {
            preference.serialize(ownerDocument) { resource ->
                // TODO: Currently, resources can only be added to "values", which may not be the correct place.
                //  It may be necessary to ask for the desired resourceValue in the future.
                addResource("values", resource)
            }.let { preferenceNode ->
                insertFirst(preferenceNode)
            }
        }

        // Add the root preference to an existing fragment if needed.
        rootPreferences?.let {
            var modified = false

            it.forEach { (intent, fileName) ->
                val preferenceFileName = "res/xml/$fileName.xml"
                if (get(preferenceFileName).exists()) {
                    document(preferenceFileName).use { document ->
                        document.getNode("PreferenceScreen").addPreference(intent)
                    }
                    modified = true
                }
            }

            if (!modified) throw PatchException("No declared preference files exists: $rootPreferences")
        }

        // Add all preferences to the ArgusOS fragment.
        document("res/xml/argusos_prefs_icons.xml").use { document ->
            val argusosPreferenceScreenNode = document.getNode("PreferenceScreen")
            preferences.forEach { argusosPreferenceScreenNode.addPreference(it) }
        }

        // Because the icon preferences require declaring a layout resource,
        // there is no easy way to change to the Android default preference layout
        // after the preference is inflated.
        // Using two different preference files is the simplest and most robust solution.
        fun removeIconsAndLayout(preferences: Collection<BasePreference>, removeAllIconsAndLayout: Boolean) {
            preferences.forEach { preference ->
                preference.icon = null
                if (removeAllIconsAndLayout) {
                    preference.iconBold = null
                    preference.layout = null
                }

                if (preference is PreferenceCategory) {
                    removeIconsAndLayout(preference.preferences, removeAllIconsAndLayout)
                } else if (preference is PreferenceScreenPreference) {
                    removeIconsAndLayout(preference.preferences, removeAllIconsAndLayout)
                }
            }
        }

        // Bold icons.
        removeIconsAndLayout(preferences, false)
        document("res/xml/argusos_prefs_icons_bold.xml").use { document ->
            val argusosPreferenceScreenNode = document.getNode("PreferenceScreen")
            preferences.forEach { argusosPreferenceScreenNode.addPreference(it) }
        }

        removeIconsAndLayout(preferences, true)

        document("res/xml/argusos_prefs.xml").use { document ->
            val argusosPreferenceScreenNode = document.getNode("PreferenceScreen")
            preferences.forEach { argusosPreferenceScreenNode.addPreference(it) }
        }
    }
}
