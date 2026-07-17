package app.argusos.extension.tiktok.feedfilter;

import app.argusos.extension.tiktok.settings.Settings;
import com.ss.android.ugc.aweme.feed.model.Aweme;

public class AdsFilter implements IFilter {
    @Override
    public boolean getEnabled() {
        return Settings.REMOVE_ADS.get();
    }

    @Override
    public boolean getFiltered(Aweme item) {
        return item.isAd() || item.isWithPromotionalMusic();
    }
}
