package app.argusos.extension.youtube.patches.announcements.requests;

import app.argusos.extension.shared.requests.Requester;
import app.argusos.extension.shared.requests.Route;

import java.io.IOException;
import java.net.HttpURLConnection;

import static app.argusos.extension.shared.requests.Route.Method.GET;

public class AnnouncementsRoutes {
    private static final String ANNOUNCEMENTS_PROVIDER = "https://api.argusos.app/v5";
    public static final Route GET_LATEST_ANNOUNCEMENT_IDS = new Route(GET, "/announcements/latest/id");
    public static final Route GET_LATEST_ANNOUNCEMENTS = new Route(GET, "/announcements/latest");

    private AnnouncementsRoutes() {
    }

    public static HttpURLConnection getAnnouncementsConnectionFromRoute(Route route, String... params) throws IOException {
        return Requester.getConnectionFromRoute(ANNOUNCEMENTS_PROVIDER, route, params);
    }
}