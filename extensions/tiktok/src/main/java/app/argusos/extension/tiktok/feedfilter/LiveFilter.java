package app.argusos.extension.tiktok.feedfilter;

import app.argusos.extension.tiktok.settings.Settings;
import com.ss.android.ugc.aweme.feed.model.Aweme;

public class LiveFilter implements IFilter {
    @Override
    public boolean getEnabled() {
        return Settings.HIDE_LIVE.get();
    }

    @Override
    public boolean getFiltered(Aweme item) {
        return item.isLive() || item.isLiveReplay();
    }
}
