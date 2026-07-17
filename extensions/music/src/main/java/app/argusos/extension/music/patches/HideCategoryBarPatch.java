package app.argusos.extension.music.patches;

import static app.argusos.extension.shared.Utils.hideViewBy0dpUnderCondition;

import android.view.View;

import app.argusos.extension.music.settings.Settings;

@SuppressWarnings("unused")
public class HideCategoryBarPatch {

    /**
     * Injection point
     */
    public static void hideCategoryBar(View view) {
        hideViewBy0dpUnderCondition(Settings.HIDE_CATEGORY_BAR, view);
    }
}
