package qa.dcsdr.diplomaticclub.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.DisplayMetrics;

import com.parse.ParsePush;

import java.util.HashMap;
import java.util.Locale;

import qa.dcsdr.diplomaticclub.Activities.HomePageActivity;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/20/2015.
 * This is the fragment for the settings page.
 */
public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {


    HashMap<String, String> hashMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        initMap();
    }

    private void initMap() {
       hashMap = new HashMap<>();
        String[] keys = {
                "FEATURED_RESEARCH_AND_STUDIES_SELECTED",
                "FEATURED_PUBLICATIONS_SELECTED",
                "FEATURED_DISPUTES_RESOLUTION_SELECTED",
                "FEATURED_PROGRAMS_AND_PROJECTS_SELECTED",
                "FEATURED_EVENTS_SELECTED"};

        String[] subscriptions = {
                "RESEARCH_AND_STUDIES",
                "PUBLICATIONS",
                "DISPUTES_RESOLUTION",
                "PROGRAMS_AND_PROJECTS",
                "EVENTS"};
        for (int i = 0; i < keys.length; i++)
            hashMap.put(keys[i], subscriptions[i]);
    }


    private void changeFeatured(String key, boolean current) {

        if (current) {
            getActivity().getSharedPreferences("HOMEPAGE_CHANGES",
                    Context.MODE_PRIVATE).edit().putBoolean(key, true).apply();
            ParsePush.subscribeInBackground(hashMap.get(key));

        }
        else {
            getActivity().getSharedPreferences("HOMEPAGE_CHANGES",
                    Context.MODE_PRIVATE).edit().putBoolean(key, false).apply();
            ParsePush.unsubscribeInBackground(hashMap.get(key));
        }
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (key.contains("FEATURED_")) {
            boolean current = sharedPreferences.getBoolean(key, true);
            if (key.equals("FEATURED_RESEARCH_AND_STUDIES_SELECTED")) {
                changeFeatured("FEATURED_RESEARCH_AND_STUDIES_SELECTED", current);

            } else if (key.equals("FEATURED_PUBLICATIONS_SELECTED")) {
                changeFeatured("FEATURED_PUBLICATIONS_SELECTED", current);

            } else if (key.equals("FEATURED_DISPUTES_RESOLUTION_SELECTED")) {
                changeFeatured("FEATURED_DISPUTES_RESOLUTION_SELECTED", current);

            } else if (key.equals("FEATURED_PROGRAMS_AND_PROJECTS_SELECTED")) {
                changeFeatured("FEATURED_PROGRAMS_AND_PROJECTS_SELECTED", current);

            } else if (key.equals("FEATURED_EVENTS_SELECTED")) {
                changeFeatured("FEATURED_EVENTS_SELECTED", current);
            }
        } else if (key.equals("LANGUAGE_KEY")) {
            Locale myLocale = new Locale((sharedPreferences.getString(key, "en")));
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(getActivity(), HomePageActivity.class);
            refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(refresh);
            getActivity().getSharedPreferences("LANGUAGE_CHANGE", Context.MODE_PRIVATE)
                    .edit().putString(key, sharedPreferences.getString(key, "en")).apply();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

}
