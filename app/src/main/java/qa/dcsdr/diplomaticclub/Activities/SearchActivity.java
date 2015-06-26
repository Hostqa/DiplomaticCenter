package qa.dcsdr.diplomaticclub.Activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import qa.dcsdr.diplomaticclub.Fragments.DisplayArticleListFragment;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/25/2015.
 * This activity activates the search functionality on the homepage.
 */
public class SearchActivity extends AppCompatActivity {

    private Fragment fragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            performSearch(query);
        }
    }

    /**
     *  Perform the search and return the results for the user.
     */
    private void performSearch(String query) {
        String resultsUrl = getString(R.string.SEARCH_URL)+query;
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentByTag("DisplayCategoriesFragment");
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragment =new DisplayArticleListFragment();
            Bundle args = new Bundle();
            args.putString("CAT_TITLE", getString(R.string.SEARCH_RESULT_TITLE));
            args.putString("URL",resultsUrl);
            fragment.setArguments(args);
            ft.replace(android.R.id.content, fragment, "myFragmentTag");
            ft.commit();
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
