package qa.dcsdr.diplomaticclub.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.DisplayMetrics;

import java.util.Locale;

import qa.dcsdr.diplomaticclub.Activities.HomePageActivity;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/20/2015.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public void change(String key, boolean value, boolean current) {
        if (current)
            getActivity().getSharedPreferences("DRAWER_CHANGES", Context.MODE_PRIVATE).edit().putBoolean(key, true).apply();
        else
            getActivity().getSharedPreferences("DRAWER_CHANGES", Context.MODE_PRIVATE).edit().putBoolean(key, false).apply();
    }

    private void restartActivity() {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("LANGUAGE_KEY")) {
            Locale myLocale = new Locale((sharedPreferences.getString(key,"en")));
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(getActivity(), HomePageActivity.class);
            refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(refresh);
            getActivity().getSharedPreferences("LANGUAGE_CHANGE", Context.MODE_PRIVATE).edit().putString(key, sharedPreferences.getString(key,"en")).apply();

//            Intent mStartActivity = new Intent(getActivity(), HomePageActivity.class);
//            int mPendingIntentId = 123456;
//            PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
//            AlarmManager mgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//            System.exit(0);
        } else {
            boolean current = sharedPreferences.getBoolean(key, true);
            if (key.equals("RESEARCH_AND_STUDIES_SELECTED")) {
                change("RESEARCH_AND_STUDIES_SELECTED", true, current);
            } else if (key.equals("PUBLICATIONS_SELECTED")) {
                change("PUBLICATIONS_SELECTED", true, current);
            } else if (key.equals("DISPUTES_RESOLUTION_SELECTED")) {
                change("DISPUTES_RESOLUTION_SELECTED", true, current);
            } else if (key.equals("PROGRAMS_AND_PROJECTS_SELECTED")) {
                change("PROGRAMS_AND_PROJECTS_SELECTED", true, current);
            } else if (key.equals("EVENTS_SELECTED")) {
                change("EVENTS_SELECTED", true, current);
            }
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
