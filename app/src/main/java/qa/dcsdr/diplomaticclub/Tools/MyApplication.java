package qa.dcsdr.diplomaticclub.Tools;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tamim on 6/10/2015.
 */
public class MyApplication extends Application{

    private static MyApplication sInstance;

    @Override
    public void onCreate(){
        super.onCreate();
        sInstance=this;
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
