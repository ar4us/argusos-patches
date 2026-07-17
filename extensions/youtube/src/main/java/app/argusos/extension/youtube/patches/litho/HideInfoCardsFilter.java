package app.argusos.extension.youtube.patches.litho;

import app.argusos.extension.shared.patches.litho.FilterGroup.StringFilterGroup;
import app.argusos.extension.youtube.settings.Settings;
import app.argusos.extension.shared.patches.litho.Filter;

@SuppressWarnings("unused")
public final class HideInfoCardsFilter extends Filter {

    public HideInfoCardsFilter() {
        addIdentifierCallbacks(
                new StringFilterGroup(
                        Settings.HIDE_INFO_CARDS,
                        "info_card_teaser_overlay.e"
                )
        );
    }
}
