package qa.dcsdr.diplomaticclub.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import qa.dcsdr.diplomaticclub.Adapters.HomePagePagerAdapter;
import qa.dcsdr.diplomaticclub.Fragments.NavigationDrawerFragment;
import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;
import qa.dcsdr.diplomaticclub.Tools.HomePageViewPager;
import qa.dcsdr.diplomaticclub.Tools.MyApplication;
import qa.dcsdr.diplomaticclub.Tools.ParsingFactory;

/**
 * Created by Tamim on 6/20/2015.
 * This activity is the homepage. It loads all the settings the
 * user has selected and loads featured categories accordingly.
 */
public class HomePageActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static SearchView searchViewS;
    private Toolbar toolbar;

    private HomePageViewPager featured;
    private HomePageViewPager researchAndStudies;
    private HomePageViewPager publications;
    private HomePageViewPager disputes_resolution;
    private HomePageViewPager programsAndProjects;
    private HomePageViewPager events;

    private LinearLayout featuredLL;
    private LinearLayout researchAndStudiesLL;
    private LinearLayout publicationsLL;
    private LinearLayout disputes_resolutionLL;
    private LinearLayout programsAndProjectsLL;
    private LinearLayout eventsLL;

    private HomePagePagerAdapter featuredA;
    private HomePagePagerAdapter researchAndStudiesA;
    private HomePagePagerAdapter publicationsA;
    private HomePagePagerAdapter disputes_resolutionA;
    private HomePagePagerAdapter programsAndProjectsA;
    private HomePagePagerAdapter eventsA;

    private LinearLayout progressBar;
    private LinearLayout errorLayout;
    private TextView volleyErrorHomePage;
    private Button retryButtonHomePage;
    private Button openBookmarks;

    private RelativeLayout splash;

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ParsingFactory parseApp;

    private CircularProgressBar cpb;

    private HomePageViewPager[] hpvp;
    private LinearLayout[] hpll;
    private LinearLayout[] hpllM;
    private HomePageViewPager[] hpvpM;

    private Activity activity;

    private ArrayList<Article> articleList = new ArrayList<>();

    private SharedPreferences.OnSharedPreferenceChangeListener homepageListener;

    private int total = 0;
    private int totalPrime = 0;
    private SearchView searchView;

    private void sendXmlRequest(HomePagePagerAdapter[] hppa) {

        SharedPreferences sp = getSharedPreferences("HOMEPAGE_CHANGES", MODE_PRIVATE);
        String[] keys = {"FEATURED_ALL",
                "FEATURED_RESEARCH_AND_STUDIES_SELECTED",
                "FEATURED_PUBLICATIONS_SELECTED",
                "FEATURED_DISPUTES_RESOLUTION_SELECTED",
                "FEATURED_PROGRAMS_AND_PROJECTS_SELECTED",
                "FEATURED_EVENTS_SELECTED"};

        String[] subscriptions = {"FEATURED_ALL",
                "RESEARCH_AND_STUDIES",
                "PUBLICATIONS",
                "DISPUTES_RESOLUTION",
                "PROGRAMS_AND_PROJECTS",
                "EVENTS"};

        String[] urls = {getFeatured(1),
                getFeatured(2),
                getFeatured(5),
                getFeatured(3),
                getFeatured(6),
                getFeatured(4)};
        total = 0;

        for (int i = 0; i < hppa.length; i++) {
            if (!sp.getBoolean(keys[i], true)) {
                hpvpM[i].setVisibility(View.GONE);
                hpllM[i].setVisibility(View.GONE);
                ParsePush.unsubscribeInBackground(subscriptions[i]);
                continue;
            }
            total += 1;
            if (i!=0)
                ParsePush.subscribeInBackground(subscriptions[i]);
            requestQueue.add(getStringRequest(urls[i], i, hppa));
        }
    }

    private String getFeatured(int i) {
        return getString(R.string.FEATURED_PARTIAL_URL) + i + ".php";
    }

    private StringRequest getStringRequest(String url, final int p, final HomePagePagerAdapter[] hppa) {
        return new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseApp = new ParsingFactory(response, 1);
                parseApp.processSearchOrFeaturedXml(true);
                articleList = parseApp.getArticles();
                totalPrime += 1;
                if (totalPrime == total) {
                    progressBar.setVisibility(View.GONE);
                    volleyErrorHomePage.setVisibility(View.GONE);
                    splash.setVisibility(View.GONE);
                    cpb.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
                        setWindowColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
                hppa[p].setArticleList(articleList);
                hppa[p].notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                splash.setVisibility(View.GONE);
                getSupportActionBar().show();
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                progressBar.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                volleyErrorHomePage.setVisibility(View.VISIBLE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    volleyErrorHomePage.setText(R.string.error_timeout);
                } else if (error instanceof AuthFailureError) {
                    volleyErrorHomePage.setText(R.string.auth_failure);
                } else if (error instanceof ServerError) {
                    volleyErrorHomePage.setText(R.string.server_error);
                } else if (error instanceof NetworkError) {
                    volleyErrorHomePage.setText(R.string.network_error);
                } else if (error instanceof ParseError) {
                    volleyErrorHomePage.setText(R.string.parse_error);
                } else {
                    volleyErrorHomePage.setText(error.toString());
                }
                retryButtonHomePage.setVisibility(View.VISIBLE);
                openBookmarks.setVisibility(View.VISIBLE);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    public static void bringUpSearch() {
        searchViewS.setIconified(false);
        searchViewS.setIconified(false);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setWindowColor(int darkVibrantColor) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(darkVibrantColor);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setTitle(R.string.title_activity_main);
        activity = this;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
            setWindowColor(getResources().getColor(R.color.colorAccent));
        }
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        getSharedPreferences("LANGUAGE_CHANGE", MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);


        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        PushService.setDefaultPushCallback(this, HomePageActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        final Intent intent = getIntent();
        final String action = intent.getAction();

        // This is for intercepting a URL
        if (Intent.ACTION_VIEW.equals(action)) {
            String url = intent.getDataString();
            if (url.contains("post")) {
                String[] urlParsed = url.split("-");
                int id = Integer.parseInt(urlParsed[urlParsed.length - 1]);
            }
        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        progressBar = (LinearLayout) findViewById(R.id.progressBarLayout);
        errorLayout = (LinearLayout) findViewById(R.id.errorLayoutHomePage);
        volleyErrorHomePage = (TextView) findViewById(R.id.volleyErrorHomePage);
        retryButtonHomePage = (Button) findViewById(R.id.retryButtonHomePage);
        openBookmarks = (Button) findViewById(R.id.openBookmarks);
        cpb = (CircularProgressBar) findViewById(R.id.pbHeaderProgressHomePage);

        splash = (RelativeLayout) findViewById(R.id.splash);

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, true);
        getSupportActionBar().hide();
        initializeAdapters();

        final HomePagePagerAdapter[] hppa = {featuredA, researchAndStudiesA, publicationsA,
                disputes_resolutionA, programsAndProjectsA, eventsA};

        hpvp = new HomePageViewPager[]{researchAndStudies, publications,
                disputes_resolution, programsAndProjects, events};

        hpll = new LinearLayout[]{researchAndStudiesLL, publicationsLL,
                disputes_resolutionLL, programsAndProjectsLL, eventsLL};

        hpllM = new LinearLayout[]{featuredLL, researchAndStudiesLL, publicationsLL,
                disputes_resolutionLL, programsAndProjectsLL, eventsLL};

        hpvpM = new HomePageViewPager[]{featured, researchAndStudies, publications,
                disputes_resolution, programsAndProjects, events};

        cpb.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);

        sendXmlRequest(hppa);

        openBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (CategoryListActivity.getA() != null) CategoryListActivity.getA().finish();
                if (DisplayArticleListActivity.getA() != null) DisplayArticleListActivity.getA().finish();
                intent = new Intent(activity, BookmarksActivity.class);
                intent.putExtra("CAT_TITLE", getResources().getString(R.string.BOOKMARKS));
                intent.putExtra(getString(R.string.PARENT_CLASS_TAG), getString(R.string.DISPLAY_FRAGMENT_PARENT_TAG));
                String url = "LOCAL";
                intent.putExtra("URL", url);
                startActivity(intent);
            }
        });

        retryButtonHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpb.setVisibility(View.VISIBLE);

                retryButtonHomePage.setVisibility(View.GONE);
                openBookmarks.setVisibility(View.GONE);
                volleyErrorHomePage.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                sendXmlRequest(hppa);
            }
        });


        homepageListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                String[] keys = {"FEATURED_RESEARCH_AND_STUDIES_SELECTED",
                        "FEATURED_PUBLICATIONS_SELECTED",
                        "FEATURED_DISPUTES_RESOLUTION_SELECTED",
                        "FEATURED_PROGRAMS_AND_PROJECTS_SELECTED",
                        "FEATURED_EVENTS_SELECTED"};
                List<String> list = Arrays.asList(keys);
                int i = list.indexOf(key);
                int v = hpvp[i].getVisibility();
                if (v == View.GONE) {
                    sendXmlRequest(hppa);
                    hpvp[i].setVisibility(View.VISIBLE);
                    hpll[i].setVisibility(View.VISIBLE);

                } else {
                    hpvp[i].setVisibility(View.GONE);
                    hpll[i].setVisibility(View.GONE);
                }
            }
        };

        getSharedPreferences("HOMEPAGE_CHANGES", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(homepageListener);

    }

    private void initializeAdapters() {

        featured = (HomePageViewPager) findViewById(R.id.pager1);
        featuredA = new HomePagePagerAdapter(getSupportFragmentManager(), articleList);
        featuredA.setCategory(getString(R.string.ALL_FEATURED));
        featured.setAdapter(featuredA);

        researchAndStudiesLL = (LinearLayout) findViewById(R.id.featuredCategory2);
        researchAndStudies = (HomePageViewPager) findViewById(R.id.pager2);
        researchAndStudiesA = new HomePagePagerAdapter(getSupportFragmentManager(), articleList);
        researchAndStudiesA.setCategory(getString(R.string.FEATURED_RESEARCH_AND_STUDIES));
        researchAndStudies.setAdapter(researchAndStudiesA);

        publicationsLL = (LinearLayout) findViewById(R.id.featuredCategory3);
        publications = (HomePageViewPager) findViewById(R.id.pager3);
        publicationsA = new HomePagePagerAdapter(getSupportFragmentManager(), articleList);
        publicationsA.setCategory(getString(R.string.FEATURED_PUBLICATIONS));
        publications.setAdapter(publicationsA);

        disputes_resolutionLL = (LinearLayout) findViewById(R.id.featuredCategory4);
        disputes_resolution = (HomePageViewPager) findViewById(R.id.pager4);
        disputes_resolutionA = new HomePagePagerAdapter(getSupportFragmentManager(), articleList);
        disputes_resolutionA.setCategory(getString(R.string.FEATURED_DISPUTES_RESOLUTION));
        disputes_resolution.setAdapter(disputes_resolutionA);

        programsAndProjectsLL = (LinearLayout) findViewById(R.id.featuredCategory5);
        programsAndProjects = (HomePageViewPager) findViewById(R.id.pager5);
        programsAndProjectsA = new HomePagePagerAdapter(getSupportFragmentManager(), articleList);
        programsAndProjectsA.setCategory(getString(R.string.FEATURED_PROGRAMS_AND_PROJECTS));
        programsAndProjects.setAdapter(programsAndProjectsA);

        eventsLL = (LinearLayout) findViewById(R.id.featuredCategory6);
        events = (HomePageViewPager) findViewById(R.id.pager6);
        eventsA = new HomePagePagerAdapter(getSupportFragmentManager(), articleList);
        eventsA.setCategory(getString(R.string.FEATURED_EVENTS));
        events.setAdapter(eventsA);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView =
                (SearchView) menu.findItem(R.id.search_button).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setIconified(true);
                searchView.setIconified(true);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchViewS= searchView;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.search_button) {
//            hideKeyboard(getWindow().getDecorView().findViewById(android.R.id.content));
//            item.collapseActionView();
            onSearchRequested();
//            toolbar.collapseActionView();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.EXIT_MESSAGE))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.YES), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                    }
                })
                .setNegativeButton(getString(R.string.NO), null)
                .show();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("LANGUAGE_KEY")) {
            ((MyApplication) getApplication()).setLocale();
            restartActivity();
        }
    }

    public void setCurrentItem(String category, boolean isLeft) {
        int correctPosition;
        HomePageViewPager homePageViewPager;
        if (category.equals(getString(R.string.ALL_FEATURED)))
            homePageViewPager = featured;
        else if (category.equals(getString(R.string.FEATURED_RESEARCH_AND_STUDIES)))
            homePageViewPager = researchAndStudies;
        else if (category.equals(getString(R.string.FEATURED_PUBLICATIONS)))
            homePageViewPager = publications;
        else if (category.equals(getString(R.string.FEATURED_DISPUTES_RESOLUTION)))
            homePageViewPager = disputes_resolution;
        else if (category.equals(getString(R.string.FEATURED_PROGRAMS_AND_PROJECTS)))
            homePageViewPager = programsAndProjects;
        else
            homePageViewPager = events;
        correctPosition = getCorrectPosition(homePageViewPager.getCurrentItem(), isLeft);
        homePageViewPager.setCurrentItem(correctPosition, true);
    }

    private int getCorrectPosition(int currentItem, boolean isLeft) {
        return isLeft ? currentItem - 1 : currentItem + 1;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
