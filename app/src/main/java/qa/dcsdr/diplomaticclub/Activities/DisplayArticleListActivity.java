package qa.dcsdr.diplomaticclub.Activities;

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


public class DisplayArticleListActivity extends ActionBarActivity {

    Fragment fragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent myIntent = getIntent();
        String title = (String) myIntent.getExtras().get("CAT_TITLE");
        setTitle(title);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentByTag("DisplayCategoriesFragment");
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragment =new DisplayArticleListFragment();
            Bundle args = new Bundle();
            args.putString("CAT_TITLE",title);
            fragment.setArguments(args);
            ft.replace(android.R.id.content, fragment, "myFragmentTag");
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_article_list, menu);
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

    @Override
    public Intent getSupportParentActivityIntent() {
        Intent parentIntent= getIntent();
        String className = parentIntent.getStringExtra(getString(R.string.PARENT_CLASS_TAG));
        Intent newIntent=null;
        try {
            newIntent = new Intent(this, Class.forName(getString(R.string.FRAGMENTS_ADDRESS) + className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newIntent;
    }

    // TODO: Low Priority: Try and make parent have only one instance
    @Override
    public void onBackPressed() {
        finish();
    }

}
