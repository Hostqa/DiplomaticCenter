package qa.dcsdr.diplomaticclub.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import qa.dcsdr.diplomaticclub.Activities.AboutUsActivity;
import qa.dcsdr.diplomaticclub.Activities.AuthorsActivity;
import qa.dcsdr.diplomaticclub.Activities.BookmarksActivity;
import qa.dcsdr.diplomaticclub.Activities.CategoryListActivity;
import qa.dcsdr.diplomaticclub.Activities.ContactUsActivity;
import qa.dcsdr.diplomaticclub.Activities.DisplayArticleListActivity;
import qa.dcsdr.diplomaticclub.Activities.HomePageActivity;
import qa.dcsdr.diplomaticclub.Activities.SettingsActivity;
import qa.dcsdr.diplomaticclub.Adapters.DrawerAdapter;
import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.Items.DrawerEntry;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 5/26/2015.
 * This is the fragment for the navigation drawer.
 */
public class NavigationDrawerFragment extends Fragment implements ClickListener {

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Handler mHandler = new Handler();
    private DrawerAdapter adapter;

    // The following is for showing the drawer when the user
    // opens the app for the very first time
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    public static final String PREF_FILE_NAME = "drawerpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private View containerView;
    private Runnable mPendingRunnable;

    private String HOME;
    private String ABOUT_US;
    private String AUTHORS;
    private String CATEGORIES;
    private String RES_AND_STUD;
    private String PUBLICATIONS;
    private String DISP_RESOL;
    private String PROG_AND_PROJ;
    private String EVENTS;
    private String BOOKMARKS;
    private String CONTACT_US;
    private String SETTINGS;

    private SharedPreferences.OnSharedPreferenceChangeListener drawerListener;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));

        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }

        HOME = getActivity().getResources().getString(R.string.HOME);
        ABOUT_US = getActivity().getResources().getString(R.string.ABOUT_US);
        AUTHORS = getActivity().getResources().getString(R.string.AUTHORS);
        CATEGORIES = getActivity().getResources().getString(R.string.CATEGORIES);
        RES_AND_STUD = getActivity().getResources().getString(R.string.RESEARCH_AND_STUDIES);
        PUBLICATIONS = getActivity().getResources().getString(R.string.PUBLICATIONS);
        DISP_RESOL = getActivity().getResources().getString(R.string.DISPUTES_RESOLUTION);
        PROG_AND_PROJ = getActivity().getResources().getString(R.string.PROGRAMS_AND_PROJECTS);
        EVENTS = getActivity().getResources().getString(R.string.EVENTS);
        BOOKMARKS = getActivity().getResources().getString(R.string.BOOKMARKS);
        CONTACT_US = getActivity().getResources().getString(R.string.CONTACT_US);
        SETTINGS = getActivity().getResources().getString(R.string.SETTINGS);

        drawerListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                String[] keys = {"RESEARCH_AND_STUDIES_SELECTED",
                        "PUBLICATIONS_SELECTED",
                        "DISPUTES_RESOLUTION_SELECTED",
                        "PROGRAMS_AND_PROJECTS_SELECTED",
                        "EVENTS_SELECTED"};
                List<String> list = Arrays.asList(keys);
                int i = list.indexOf(key);
                if (i != -1) {
                    adapter.delete(i);
                }
                adapter.notifyDataSetChanged();

            }
        };

        getActivity().getSharedPreferences("DRAWER_CHANGES", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(drawerListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        adapter = new DrawerAdapter(getActivity(), getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    public List<DrawerEntry> getData() {
        //load only static data inside a drawer
        List<DrawerEntry> data = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.drawer_items);
        for (int i = 0; i < titles.length; i++) {
            DrawerEntry current = new DrawerEntry();
            current.setTitle(titles[i % titles.length]);
            data.add(current);
        }
        return data;
    }

    public void setUp(int fragmentId, final DrawerLayout drawerLayout, Toolbar toolbar, boolean hb) {

        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }
//                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//                getActivity().invalidateOptionsMenu();
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                    mPendingRunnable = null;
                }

            }
        };

//        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
//            mDrawerLayout.openDrawer(containerView);
//            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (hb) {
            // This places the "Hamburger" icon in the toolbar
            mDrawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    mDrawerToggle.syncState();
                }
            });
        }
    }

    private static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    private static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void itemClicked(View view, final int position) {
        RelativeLayout rl = (RelativeLayout) view;
        TextView tv = (TextView) rl.findViewById(R.id.listText);
        final String title = tv.getText() + "";
        mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (title.equals(HOME)) {
                    intent = new Intent(getActivity(), HomePageActivity.class);
                } else if (title.equals(ABOUT_US)) {
                    intent = new Intent(getActivity(), AboutUsActivity.class);
                } else if (title.equals(AUTHORS)) {
                    intent = new Intent(getActivity(), AuthorsActivity.class);
                } else if (title.equals(CATEGORIES) ||
                        (title.equals(getString(R.string.NO_CATEGORIES)))) {
                    return;
                } else if (title.equals(RES_AND_STUD)) {
                    if (!getActivity().getTitle().toString().equals(RES_AND_STUD)) {
                        if (CategoryListActivity.getA() != null)
                            CategoryListActivity.getA().finish();
                        intent = new Intent(getActivity(), CategoryListActivity.class);
                        intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                                RES_AND_STUD);
                    } else return;
                } else if (title.equals(PUBLICATIONS)) {
                    if (!getActivity().getTitle().toString().equals(PUBLICATIONS)) {
                        if (CategoryListActivity.getA() != null)
                            CategoryListActivity.getA().finish();
                        intent = new Intent(getActivity(), CategoryListActivity.class);
                        intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                                PUBLICATIONS);
                    } else return;
                } else if (title.equals(DISP_RESOL)) {
                    if (!getActivity().getTitle().toString().equals(DISP_RESOL)) {
                        if (CategoryListActivity.getA() != null)
                            CategoryListActivity.getA().finish();
                        intent = new Intent(getActivity(), CategoryListActivity.class);
                        intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                                DISP_RESOL);
                    } else return;
                } else if (title.equals(PROG_AND_PROJ)) {
                    if (!getActivity().getTitle().toString().equals(PROG_AND_PROJ)) {
                        if (CategoryListActivity.getA() != null)
                            CategoryListActivity.getA().finish();
                        intent = new Intent(getActivity(), CategoryListActivity.class);
                        intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                                PROG_AND_PROJ);
                    } else return;
                } else if (title.equals(EVENTS)) {
                    if (!getActivity().getTitle().toString().equals(EVENTS)) {

                        if (CategoryListActivity.getA() != null)
                            CategoryListActivity.getA().finish();
                        intent = new Intent(getActivity(), CategoryListActivity.class);
                        intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                                EVENTS);
                    } else return;

                } else if (title.equals(BOOKMARKS)) {
                    if (!getActivity().getTitle().toString().equals(BOOKMARKS)) {
                        if (CategoryListActivity.getA() != null)
                            CategoryListActivity.getA().finish();
                        if (DisplayArticleListActivity.getA() != null)
                            DisplayArticleListActivity.getA().finish();
                        intent = new Intent(getActivity(), BookmarksActivity.class);
                        intent.putExtra("CAT_TITLE", getResources().getString(R.string.BOOKMARKS));
                        intent.putExtra(getActivity().getString(R.string.PARENT_CLASS_TAG),
                                getActivity().getString(R.string.DISPLAY_FRAGMENT_PARENT_TAG));
                        String url = "LOCAL";
                        intent.putExtra("URL", url);
                    } else return;
                } else if (title.equals(CONTACT_US)) {
                    intent = new Intent(getActivity(), ContactUsActivity.class);
                } else if (title.equals(SETTINGS)) {
                    intent = new Intent(getActivity(), SettingsActivity.class);
                }
                startActivity(intent);
            }
        };
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

}