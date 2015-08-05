package qa.dcsdr.diplomaticclub.Tools;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Tamim on 8/4/2015.
 */
public class Analytics {

    private Context context;
    private Tracker tracker;
    private GoogleAnalytics analytics;

    public Analytics(Context context) {
        this.context = context;
        analytics = GoogleAnalytics.getInstance(this.context);
        tracker=analytics.newTracker("UA-65956782-1"); //
    }

    public void send(String screenName, String action) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction(action)
                .setLabel("Submit")
                .build());
    }

}
