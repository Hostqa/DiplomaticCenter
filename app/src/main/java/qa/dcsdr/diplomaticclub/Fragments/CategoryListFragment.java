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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Toolbar toolbar;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;

    private OnFragmentInteractionListener mListener;
    private List<CategoryEntry> data;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisputesResolutionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryListFragment newInstance(String param1, String param2) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

//    public List<CategoryEntry> getDsata() {
//
//
//        CategoryEntry intNeg = new CategoryEntry();
//        intNeg.setCategoryDescription("International Negotiations Description");
//        intNeg.setCategoryImageId(R.drawable.sample_category_pic);
//        intNeg.setCategoryTitle(getString(R.string.DisputesResolution));
//        List list1 = new ArrayList<String>();
//        list1.add("Principles of Negotiations");
//        list1.add("Art of Diplomacy");
//        list1.add("International Arbitration");
//        list1.add("Mediation");
//        intNeg.setSubCategories(list1);
//
//        CategoryEntry conAn = new CategoryEntry();
//        conAn.setCategoryDescription("Conflict Analysis Description");
//        conAn.setCategoryImageId(R.drawable.sample_category_pic);
//        conAn.setCategoryTitle("Conflict Analysis");
//        List list2 = new ArrayList<String>();
//        list2.add("Ethnic Conflicts");
//        list2.add("Religious Conflicts");
//        list2.add("Political Conflicts");
//        list2.add("Economic Conflicts");
//        conAn.setSubCategories(list2);
//
//        CategoryEntry riskReg = new CategoryEntry();
//        riskReg.setCategoryDescription("Risk Register Description");
//        riskReg.setCategoryImageId(R.drawable.sample_category_pic);
//        riskReg.setCategoryTitle("Risk Register");
//        List list3 = new ArrayList<String>();
//        list3.add("Security Risks");
//        list3.add("Environmental Risks");
//        list3.add("Political Risks");
//        list3.add("Economic Risks");
//        riskReg.setSubCategories(list3);
//
//        CategoryEntry peaceK = new CategoryEntry();
//        peaceK.setCategoryDescription("Peace Keeping & Building Description");
//        peaceK.setCategoryImageId(R.drawable.sample_category_pic);
//        peaceK.setCategoryTitle("Peace Keeping & Building");
//        List list4 = new ArrayList<String>();
//        list4.add("Peace Keeping Charters");
//        list4.add("Peace Keeping");
//        list4.add("Experiences of Peace Keeping");
//        list4.add("Experiences of Peace-Building");
//        peaceK.setSubCategories(list4);
//
//        List<CategoryEntry> data1 = new ArrayList();
//        data1.clear();
//        data1.add(intNeg);
//        data1.add(conAn);
//        data1.add(riskReg);
//        data1.add(peaceK);
//
//        return data1;
//    }
//

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
