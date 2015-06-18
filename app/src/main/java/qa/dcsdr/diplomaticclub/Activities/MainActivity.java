package qa.dcsdr.diplomaticclub.Activities;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import qa.dcsdr.diplomaticclub.Adapters.HomePageAdapter;
import qa.dcsdr.diplomaticclub.Fragments.NavigationDrawerFragment;
import qa.dcsdr.diplomaticclub.Items.HomePageEntry;
import qa.dcsdr.diplomaticclub.R;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private RecyclerView rv;
    private List<HomePageEntry> categories;
    private HomePageAdapter adapter;
    private ImageLoader imageLoader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        this.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.categoriesRV);
        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);

        categories = new ArrayList<>(5);
        initializeData();
        adapter = new HomePageAdapter(categories, this);
        rv.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, true);

//        Runtime rt = Runtime.getRuntime();
//        long maxMemory = rt.maxMemory();
//
//        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        int memoryClass = am.getMemoryClass();
//        Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
//
//        Toast.makeText(this, "onCreate"+ "maxMemory:" + Long.toString(maxMemory), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "onCreate"+ "memoryClass:" + Long.toString(memoryClass), Toast.LENGTH_SHORT).show();

    }

    private void initializeData() {
        categories.add(new HomePageEntry("Research & Studies", R.drawable.research_studies_cat_compressed));
        categories.add(new HomePageEntry("Publications", R.drawable.publications_cat_compressed));
        categories.add(new HomePageEntry("Disputes Resolution", R.drawable.disputes_resolution_cat_compressed));
        categories.add(new HomePageEntry("Programs & Projects", R.drawable.programs_projects_cat_compressed));
        categories.add(new HomePageEntry("Events", R.drawable.events_cat_compressed));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
