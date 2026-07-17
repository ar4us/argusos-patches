package app.argusos.extension.tiktok.feedfilter;

import app.argusos.extension.tiktok.settings.Settings;
import com.ss.android.ugc.aweme.feed.model.Aweme;

public class ShopFilter implements IFilter {
    private static final String SHOP_INFO = "placeholder_product_id";
    @Override
    public boolean getEnabled() {
        return Settings.HIDE_SHOP.get();
    }

    @Override
    public boolean getFiltered(Aweme item) {
        return item.getShareUrl().contains(SHOP_INFO);
    }
}
