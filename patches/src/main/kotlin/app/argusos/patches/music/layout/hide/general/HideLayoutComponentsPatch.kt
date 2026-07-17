package app.argusos.patches.music.layout.hide.general

import app.argusos.patches.music.misc.litho.filter.lithoFilterPatch
import app.argusos.patches.music.misc.settings.settingsPatch
import app.argusos.patches.shared.layout.hide.general.hideLayoutComponentsPatch
import app.argusos.patches.music.misc.settings.PreferenceScreen

val hideLayoutComponentsPatch = hideLayoutComponentsPatch(
    lithoFilterPatch = lithoFilterPatch,
    settingsPatch = settingsPatch,
    generalSettingsScreen = PreferenceScreen.GENERAL,
    filterClasses = setOf("Lapp/argusos/extension/shared/patches/litho/CustomFilter;"),
    compatibleWithPackages = arrayOf(
        "com.google.android.apps.youtube.music" to setOf(
            "7.29.52",
            "8.10.52",
            "8.37.56",
            "8.40.54"
        )
    ),
)
