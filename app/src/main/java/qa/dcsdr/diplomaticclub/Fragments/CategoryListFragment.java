package qa.dcsdr.diplomaticclub.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import qa.dcsdr.diplomaticclub.Adapters.CategoryAdapter;
import qa.dcsdr.diplomaticclub.Items.CategoryDictionary;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/17/2015.
 * This is the fragment for the category list.
 */
public class CategoryListFragment extends Fragment {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;

    public static CategoryListFragment newInstance() {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        CategoryDictionary categoryDictionary = new CategoryDictionary(getActivity());
        String category = (String) getActivity().getIntent().getExtras().get("CAT_TITLE");
        adapter = new CategoryAdapter(getActivity(), categoryDictionary.getList(category));
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar()!=null) {
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            activity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
        DrawerLayout dl = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getChildFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_cl);
        if (drawerFragment != null && dl !=null) {
            drawerFragment.setUp(R.id.fragment_navigation_drawer_dal, dl, toolbar, true);
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.disputesResoltionRV);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


}
