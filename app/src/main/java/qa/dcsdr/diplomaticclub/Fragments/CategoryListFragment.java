package qa.dcsdr.diplomaticclub.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import qa.dcsdr.diplomaticclub.Adapters.CategoryAdapter;
import qa.dcsdr.diplomaticclub.Items.CategoryDictionary;
import qa.dcsdr.diplomaticclub.Items.CategoryEntry;
import qa.dcsdr.diplomaticclub.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryListFragment extends Fragment {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private List<CategoryEntry> data;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DisputesResolutionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryListFragment newInstance(String param1, String param2) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryListFragment() {
        // Required empty public constructor
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
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        CategoryDictionary categoryDictionary = new CategoryDictionary(getActivity());
        String category = (String) getActivity().getIntent().getExtras().get("CAT_TITLE");
        adapter = new CategoryAdapter(getActivity(), categoryDictionary.getList(category));
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
