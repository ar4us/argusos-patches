package app.argusos.extension.tiktok.feedfilter;

import app.argusos.extension.tiktok.settings.Settings;
import com.ss.android.ugc.aweme.feed.model.Aweme;

public class ImageVideoFilter implements IFilter {
    @Override
    public boolean getEnabled() {
        return Settings.HIDE_IMAGE.get();
    }

    @Override
    public boolean getFiltered(Aweme item) {
        return item.isImage() || item.isPhotoMode();
    }
}
