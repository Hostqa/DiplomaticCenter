package qa.dcsdr.diplomaticclub.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import qa.dcsdr.diplomaticclub.Activities.AboutUsActivity;
import qa.dcsdr.diplomaticclub.Activities.AuthorsActivity;
import qa.dcsdr.diplomaticclub.Activities.Category;
import qa.dcsdr.diplomaticclub.Activities.ContactUsActivity;
import qa.dcsdr.diplomaticclub.Activities.HomePageActivity;
import qa.dcsdr.diplomaticclub.Adapters.MyAdapter;
import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.Items.DrawerEntry;
import qa.dcsdr.diplomaticclub.R;

import static qa.dcsdr.diplomaticclub.Tools.DrawerItemNumbers.DrawerItems.ABOUT_US;
import static qa.dcsdr.diplomaticclub.Tools.DrawerItemNumbers.DrawerItems.AUTHORS;
import static qa.dcsdr.diplomaticclub.Tools.DrawerItemNumbers.DrawerItems.BOOKMARKS;
import static qa.dcsdr.diplomaticclub.Tools.DrawerItemNumbers.DrawerItems.CATEGORIES;
import static qa.dcsdr.diplomaticclub.Tools.DrawerItemNumbers.DrawerItems.CONTACT_US;
import static qa.dcsdr.diplomaticclub.Tools.DrawerItemNumbers.DrawerItems.DISP_RESOL;
import static qa.dcsdr.diplomaticclub.Tools.DrawerItemNumbers.DrawerItems.EVENTS;
import static qa.dcsdr.diplomaticclub.Tools.DrawerItemNumbers.DrawerItems.HOME;
import static qa.dcsdr.diplomaticclub.Tools.DrawerItemNumbers.DrawerItems.PROG_AND_PROJ;
import static qa.dcsdr.diplomaticclub.Tools.DrawerItemNumbers.DrawerItems.PUBLICATIONS;
import static qa.dcsdr.diplomaticclub.Tools.DrawerItemNumbers.DrawerItems.RES_AND_STUD;
import static qa.dcsdr.diplomaticclub.Tools.DrawerItemNumbers.DrawerItems.SETTINGS;

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
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

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


    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void itemClicked(View view, final int position) {
        final List<String> categories = Arrays.asList((getResources().getStringArray(R.array.categories)));
        mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent=null;
                switch (position) {
                    case HOME:
                        intent = new Intent(getActivity(), HomePageActivity.class);
                        break;
                    case ABOUT_US:
                        intent = new Intent(getActivity(), AboutUsActivity.class);
                        break;
                    case AUTHORS:
                        intent = new Intent(getActivity(), AuthorsActivity.class);
                        break;
                    case CATEGORIES:
                        return;
                    case RES_AND_STUD:
                        intent = new Intent(getActivity(), Category.ResearchAndStudies.class);
                        intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                                categories.get(position-4));
                        break;
                    case PUBLICATIONS:
                        intent = new Intent(getActivity(), Category.Publications.class);
                        intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                                categories.get(position-4));
                        break;
                    case DISP_RESOL:
                        intent = new Intent(getActivity(), Category.DisputesResolution.class);
                        intent.putExtra("BACK_TO_CAT", categories.get(position - 4));
                        intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                                categories.get(position - 4));
                        break;
                    case PROG_AND_PROJ:
                        intent = new Intent(getActivity(), Category.ProgramsAndProjects.class);
                        intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                                categories.get(position-4));
                        break;
                    case EVENTS:
                        intent = new Intent(getActivity(), Category.Events.class);
                        intent.putExtra(getActivity().getString(R.string.CAT_TITLE_TAG),
                                categories.get(position-4));
                        break;
                    case BOOKMARKS:
                        intent = new Intent(getActivity(), HomePageActivity.class);
                        break;
                    case CONTACT_US:
                        intent = new Intent(getActivity(), ContactUsActivity.class);
                        break;
                    case SETTINGS:
                        intent = new Intent(getActivity(), HomePageActivity.class);
                        break;
                }
                startActivity(intent);
            }
        };
        mDrawerLayout.closeDrawer(Gravity.START);
    }

}