package app.argusos.extension.instagram.misc.share.privacy;

import app.argusos.extension.shared.privacy.LinkSanitizer;

@SuppressWarnings("unused")
public final class SanitizeSharingLinksPatch {
    private static final LinkSanitizer sanitizer = new LinkSanitizer("igsh");

    /**
     * Injection point.
     */
    public static String sanitizeSharingLink(String url) {
        return sanitizer.sanitizeURLString(url);
    }
}
