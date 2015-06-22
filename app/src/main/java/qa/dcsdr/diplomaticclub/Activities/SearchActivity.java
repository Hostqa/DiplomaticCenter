package qa.dcsdr.diplomaticclub.Activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import qa.dcsdr.diplomaticclub.Fragments.DisplayArticleListFragment;
import qa.dcsdr.diplomaticclub.R;

public class SearchActivity extends ActionBarActivity {

    Fragment fragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent myIntent = getIntent();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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
            //use the query to search your data somehow
        }
    }

    private void performSearch(String query) {
        String resultsUrl = ""+query;
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentByTag("DisplayCategoriesFragment");
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragment =new DisplayArticleListFragment();
            Bundle args = new Bundle();
            String url = getString(R.string.SHOW_AUTHOR_ARTICLES_URL)+"1";
//            Toast.makeText(this,url,Toast.LENGTH_SHORT).show();
            args.putString("CAT_TITLE", getString(R.string.SEARCH_RESULT_TITLE));
            args.putString("URL",url);
            fragment.setArguments(args);
            ft.replace(android.R.id.content, fragment, "myFragmentTag");
            ft.commit();
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
