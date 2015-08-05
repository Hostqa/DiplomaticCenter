package qa.dcsdr.diplomaticclub.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import qa.dcsdr.diplomaticclub.Fragments.RecentAlertsFragment;
import qa.dcsdr.diplomaticclub.R;

public class RecentAlerts extends ActionBarActivity {

    private Toolbar toolbar;
    private Fragment fragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.RECENT_ALERTS));
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentByTag("RECENT_ALERTS");
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragment =new RecentAlertsFragment();
            ft.replace(android.R.id.content, fragment, "RECENT_ALERTS");
            ft.commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recent_alerts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.clearAll) {
            clearAllAlerts();
        }
        else if (id == R.id.refresh) {
            refreshAlerts();
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private void refreshAlerts() {
        ((RecentAlertsFragment) fragment).refreshAlerts();
    }

    private void clearAllAlerts() {
        ((RecentAlertsFragment) fragment).clearAllAlerts();
    }

}
