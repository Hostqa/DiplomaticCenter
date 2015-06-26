package qa.dcsdr.diplomaticclub.Fragments;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.ArrayList;

import qa.dcsdr.diplomaticclub.Adapters.AuthorAdapter;
import qa.dcsdr.diplomaticclub.Items.Author;
import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;
import qa.dcsdr.diplomaticclub.Tools.ParsingFactory;


/**
 * Created by Tamim on 6/17/2015.
 * This is the fragment for the authors list.
 */
public class AuthorListFragment extends Fragment implements ClickListener {

    private static final String STATE_ARTICLES = "state_articles";
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ArrayList<Author> authorList = new ArrayList<>();
    private RecyclerView authorsRV;
    private TextView volleyError;
    private ParsingFactory parseApp;
    private Toolbar toolbar;
    private TextView noArticles;
    private Button retryButton;
    private LinearLayout linearLayout;
    private AuthorAdapter authorsAdapter;
    private String url;
    private LinearLayout progressHeader;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoryTest.
     */
    public static AuthorListFragment newInstance() {
        AuthorListFragment fragment = new AuthorListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AuthorListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.activity_authors, container, false);
        noArticles = (TextView) view.findViewById(R.id.noArticles);
        progressHeader = (LinearLayout) view.findViewById(R.id.progressHeader);
        retryButton=(Button)view.findViewById(R.id.retryButton);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        authorsAdapter= new AuthorAdapter(getActivity());
        authorsRV = (RecyclerView) view.findViewById(R.id.authorList);

        authorsRV.setLayoutManager(layoutManager);
        authorsRV.setAdapter(authorsAdapter);
        volleyError = (TextView) view.findViewById(R.id.volleyError);
        url = getResources().getString(R.string.AUTHOR_LIST_URL);
        toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        if (activity.getSupportActionBar()!=null)
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.setProgressBarIndeterminateVisibility(true);
        DrawerLayout dl = (DrawerLayout) view.findViewById(R.id.drawer_layout_aa);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getChildFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_aa);
        drawerFragment.setUp(R.id.fragment_navigation_drawer_aa, dl, toolbar, true);

        progressHeader.setVisibility(View.VISIBLE);
        linearLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        linearLayout.setVisibility(View.GONE);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryButton.setVisibility(View.GONE);
                volleyError.setVisibility(View.GONE);
                progressHeader.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                sendXmlRequest(url);
            }
        });

        if (savedInstanceState != null) {
            authorList = savedInstanceState.getParcelableArrayList(STATE_ARTICLES);
            authorsAdapter.setAuthorList(authorList);
        } else {
            sendXmlRequest(url);
        }

        return view;
    }

    private void sendXmlRequest(String url) {
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressHeader.setVisibility(View.GONE);
                volleyError.setVisibility(View.GONE);
                parseApp = new ParsingFactory(response,0);
                parseApp.processAuthorXml();
                authorList = parseApp.getAuthors();
                if (authorList.size() == 0) {
                    progressHeader.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    noArticles.setVisibility(View.VISIBLE);
                }
                authorsAdapter.setAuthorList(authorList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressHeader.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                volleyError.setVisibility(View.VISIBLE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    volleyError.setText(R.string.error_timeout);
                } else if (error instanceof AuthFailureError) {
                    volleyError.setText(R.string.auth_failure);
                } else if (error instanceof ServerError) {
                    volleyError.setText(R.string.server_error);
                } else if (error instanceof NetworkError) {
                    volleyError.setText(R.string.network_error);
                } else if (error instanceof ParseError) {
                    volleyError.setText(R.string.parse_error);
                } else {
                    volleyError.setText(error.toString());
                }
                retryButton.setVisibility(View.VISIBLE);
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_ARTICLES, authorList);
    }

    @Override
    public void itemClicked(View view, int position) {
        final Intent intent;
    }

}