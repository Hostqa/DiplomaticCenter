package qa.dcsdr.diplomaticclub.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Created by Tamim on 6/10/2015.
 */
public class MyApplication extends android.app.Application{

    private static MyApplication sInstance;

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    public void onCreate(){
        super.onCreate();
        setLocale();
        sInstance=this;
    }

    public void setLocale() {
        Locale locale = getLocaleFromPref();
        Configuration config = getBaseContext().getResources().getConfiguration();
        overwriteConfigurationLocale(config, locale);
    }

    private void overwriteConfigurationLocale(Configuration config, Locale locale) {
        config.locale = locale;
        getBaseContext().getResources()
                .updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private Locale getLocaleFromPref() {
        Locale locale = Locale.getDefault();
        String language = getPreferences().getString("LANGUAGE_KEY", "");
        if (!language.equals("")) {
            locale = new Locale(language);
            Locale.setDefault(locale);
        }
        return locale;
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
