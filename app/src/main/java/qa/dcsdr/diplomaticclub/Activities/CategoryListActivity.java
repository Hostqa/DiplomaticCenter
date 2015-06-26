package qa.dcsdr.diplomaticclub.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import qa.dcsdr.diplomaticclub.Fragments.CategoryListFragment;
import qa.dcsdr.diplomaticclub.R;

public class CategoryListActivity extends AppCompatActivity {

    private Fragment fragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        String title = ((String)myIntent.getExtras().get("CAT_TITLE"));
        setTitle(title);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentByTag("CategoryList");
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragment =new CategoryListFragment();
            ft.replace(android.R.id.content, fragment, "CategoryList");
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

}