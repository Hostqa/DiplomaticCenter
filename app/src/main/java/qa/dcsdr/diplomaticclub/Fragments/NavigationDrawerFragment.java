package qa.dcsdr.diplomaticclub.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
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
import qa.dcsdr.diplomaticclub.Activities.Category;
import qa.dcsdr.diplomaticclub.Activities.ContactUsActivity;
import qa.dcsdr.diplomaticclub.Activities.HomePageActivity;
import qa.dcsdr.diplomaticclub.Activities.SettingsActivity;
import qa.dcsdr.diplomaticclub.Adapters.MyAdapter;
import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.Items.DrawerEntry;
import qa.dcsdr.diplomaticclub.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements ClickListener {

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Handler mHandler = new Handler();
    private MyAdapter adapter;

    // The following is for showing the drawer when the user opens the app for the very first time
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
        // Required empty public constructor
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
        adapter = new MyAdapter(getActivity(), getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Inflate the layout for this fragment
        return layout;
    }

    private List<DrawerEntry> getData() {
        //load only static data inside a drawer
        List<DrawerEntry> data = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.drawer_items);
        String[] categories = getResources().getStringArray(R.array.drawer_items);
        String[] keys = {"", "", "", "", "RESEARCH_AND_STUDIES_SELECTED",
                "PUBLICATIONS_SELECTED",
                "DISPUTES_RESOLUTION_SELECTED",
                "PROGRAMS_AND_PROJECTS_SELECTED",
                "EVENTS_SELECTED", "", "", ""};
        SharedPreferences sharedPref = getActivity().getSharedPreferences("DRAWER_CHANGES", Context.MODE_PRIVATE);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
        int j = 0;
        for (int i = 0; i < titles.length; i++) {
            if (i < 4 || i > 8 || sharedPref.getBoolean(keys[i], true)) {
                DrawerEntry current = new DrawerEntry();
                current.setTitle(titles[i % titles.length]);
                data.add(current);
            }
        }
        for (int a = 4; a < 9; a++) {
            if (!sharedPref.getBoolean(keys[a], true)) j++;
        }
        if (j == 5) {
            DrawerEntry nde = new DrawerEntry();
            nde.setTitle(getActivity().getResources().getString(R.string.NO_CATEGORIES));
            data.add(4, nde);
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
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                getActivity().invalidateOptionsMenu();
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                    mPendingRunnable = null;
                }

            }


//            @Override
//            public boolean onOptionsItemSelected(MenuItem item) {
//                if (item != null && item.getItemId() == android.R.id.home) {
//                    if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
//                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
//                    } else {
//                        mDrawerLayout.openDrawer(Gravity.RIGHT);
//                    }
//                }
//                return false;
//            }

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


    private int getRealIndex(int numToCheck, int currentBeingChecked) {
        if (currentBeingChecked == numToCheck) return 0;
        String[] keys = {"RESEARCH_AND_STUDIES_SELECTED",
                "PUBLICATIONS_SELECTED",
                "DISPUTES_RESOLUTION_SELECTED",
                "PROGRAMS_AND_PROJECTS_SELECTED",
                "EVENTS_SELECTED"};
        if ((getActivity().getSharedPreferences("DRAWER_CHANGES", Context.MODE_PRIVATE).getBoolean(keys[currentBeingChecked], true))) {
            return 1 + getRealIndex(numToCheck, currentBeingChecked + 1);
        } else
            return getRealIndex(numToCheck, currentBeingChecked + 1);
    }


    @Override
    public void itemClicked(View view, final int position) {
        final List<String> categories = Arrays.asList((getResources().getStringArray(R.array.drawer_items)));
//        String numOfEntries = "NUM_OF_ENTRIES";
//        int l= getActivity().getSharedPreferences("DRAWER_CHANGES", Context.MODE_PRIVATE).getInt(numOfEntries, 5);
        RelativeLayout rl = (RelativeLayout) view;
        TextView tv = (TextView) rl.findViewById(R.id.listText);
        final String title = tv.getText() + "";
        mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                int cont = 0;
                Intent intent = null;
                if (title.equals(HOME)) {
                    intent = new Intent(getActivity(), HomePageActivity.class);
                } else if (title.equals(ABOUT_US)) {
                    intent = new Intent(getActivity(), AboutUsActivity.class);
                } else if (title.equals(AUTHORS)) {
                    intent = new Intent(getActivity(), AuthorsActivity.class);
                } else if (title.equals(CATEGORIES) || (title.equals(getString(R.string.NO_CATEGORIES)))) {
                    return;
                } else if (title.equals(RES_AND_STUD)) {
                    intent = new Intent(getActivity(), Category.ResearchAndStudies.class);
                    intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                            RES_AND_STUD);
                } else if (title.equals(PUBLICATIONS)) {
                    intent = new Intent(getActivity(), Category.Publications.class);
                    intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                            PUBLICATIONS);
                } else if (title.equals(DISP_RESOL)) {
                    intent = new Intent(getActivity(), Category.DisputesResolution.class);
                    intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                            DISP_RESOL);
                } else if (title.equals(PROG_AND_PROJ)) {
                    intent = new Intent(getActivity(), Category.ProgramsAndProjects.class);
                    intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                            PROG_AND_PROJ);
                } else if (title.equals(EVENTS)) {
                    intent = new Intent(getActivity(), Category.Events.class);
                    intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                            EVENTS);
                } else if (title.equals(BOOKMARKS)) {
                    intent = new Intent(getActivity(), BookmarksActivity.class);
                    intent.putExtra("CAT_TITLE", getResources().getString(R.string.BOOKMARKS));
                    intent.putExtra(getActivity().getString(R.string.PARENT_CLASS_TAG), getActivity().getString(R.string.DISPLAY_FRAGMENT_PARENT_TAG));
                    String url = "LOCAL";
                    intent.putExtra("URL", url);
                } else if (title.equals(CONTACT_US)) {
                    intent = new Intent(getActivity(), ContactUsActivity.class);
                } else if (title.equals(SETTINGS)) {
                    intent = new Intent(getActivity(), SettingsActivity.class);
                }
                startActivity(intent);
            }
        };
        mDrawerLayout.closeDrawer(Gravity.START);
    }

}