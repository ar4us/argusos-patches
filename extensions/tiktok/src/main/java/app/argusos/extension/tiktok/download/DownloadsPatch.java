package app.argusos.extension.tiktok.download;

import app.argusos.extension.tiktok.settings.Settings;

@SuppressWarnings("unused")
public class DownloadsPatch {
    public static String getDownloadPath() {
        return Settings.DOWNLOAD_PATH.get();
    }

    public static boolean shouldRemoveWatermark() {
        return Settings.DOWNLOAD_WATERMARK.get();
    }
}
