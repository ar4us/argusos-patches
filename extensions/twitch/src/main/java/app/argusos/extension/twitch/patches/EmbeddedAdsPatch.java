package app.argusos.extension.twitch.patches;

import app.argusos.extension.twitch.api.RequestInterceptor;

@SuppressWarnings("unused")
public class EmbeddedAdsPatch {
    public static RequestInterceptor createRequestInterceptor() {
        return new RequestInterceptor();
    }
}