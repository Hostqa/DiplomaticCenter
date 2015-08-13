package qa.dcsdr.diplomaticclub.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;

import java.util.Locale;

/**
 * Created by Tamim on 6/10/2015.
 * This is the Application class used by the app.
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

        Parse.initialize(this, "XnkWkczDKDLj9Sx7Sw4moO4iW3pwa6EISLCLzMP0", "WmJ2IsBjZ1AaAEbQ3hWIbdMAkcbTBG6S5Vzot9jV");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        ParsePush.subscribeInBackground("BREAKING_NEWS");
        ParsePush.subscribeInBackground("NEW_DCSDR_EVENTS");
        // TODO: REMOVE THE FOLLOWING LINES IN THE NEXT UPDATE:
        ParsePush.unsubscribeInBackground("NEW_EVENTS");
        ParsePush.unsubscribeInBackground("Events");
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
