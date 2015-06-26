package qa.dcsdr.diplomaticclub.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import qa.dcsdr.diplomaticclub.Activities.ArticleReader;
import qa.dcsdr.diplomaticclub.Adapters.ArticleAdapter;
import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.Items.HidingScrollListener;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;
import qa.dcsdr.diplomaticclub.Tools.ParsingFactory;

/**
 * Created by Tamim on 6/17/2015.
 * This is the fragment for displaying an article list.
 */
public class DisplayArticleListFragment extends Fragment implements ClickListener {

    private static final String STATE_ARTICLES = "state_articles";
    private String category;
    private ArticleAdapter rPubAdapter;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private ArrayList<Article> articleList = new ArrayList<>();
    private RecyclerView articlesRV;
    private TextView volleyError;
    private ParsingFactory parseApp;
    private ParsingFactory parseSearch;
    private Toolbar toolbar;
    private TextView noArticles;
    private Button retryButton;
    private LinearLayout linearLayout;
    private LinearLayout noBookmarksFound;
    private AppCompatActivity activity;
    private String url;
    private String title;

    private String getRequestUrl() {
        if (url != null)
            return url;
        else
            return (String) this.getActivity().getIntent().getExtras().get("URL");
    }

    private String getTitle() {
        return (String) this.getActivity().getIntent().getExtras().get("CAT_TITLE");
    }

    public static DisplayArticleListFragment newInstance(String category) {
        DisplayArticleListFragment fragment = new DisplayArticleListFragment();
        Bundle args = new Bundle();
        args.putString("CAT_ARGS", category);
        fragment.setArguments(args);
        return fragment;
    }

    public DisplayArticleListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            category = getArguments().getString("CAT_TITLE");
            title = getArguments().getString("CAT_TITLE");
            url = getArguments().getString("URL");
        }
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_display_article_list, container, false);
        noArticles = (TextView) view.findViewById(R.id.noArticles);
        final LinearLayout linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.GONE);
        retryButton = (Button) view.findViewById(R.id.retryButton);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.setProgressBarIndeterminateVisibility(true);
        DrawerLayout dl = (DrawerLayout) view.findViewById(R.id.drawer_layout_dal);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getChildFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_dal);
        if (drawerFragment != null && dl != null)
            drawerFragment.setUp(R.id.fragment_navigation_drawer_dal, dl, toolbar, true);
        // Check if this is a list of bookmarks or not
        if (getActivity().getIntent().getExtras().getString("URL", "").equals("LOCAL")) {
            rPubAdapter = new ArticleAdapter(getActivity(), true);
        } else {
            rPubAdapter = new ArticleAdapter(getActivity(), false);
        }
        rPubAdapter.setClickListener(this);
        articlesRV = (RecyclerView) view.findViewById(R.id.articleList);
        volleyError = (TextView) view.findViewById(R.id.volleyError);

        articlesRV.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }
            @Override
            public void onShow() {
                showViews();
            }
        });

        articlesRV.setLayoutManager(layoutManager);
        articlesRV.setAdapter(rPubAdapter);
        linlaHeaderProgress.setVisibility(View.VISIBLE);

        noBookmarksFound = (LinearLayout) view.findViewById(R.id.noBookmarksLayout);

        linearLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        linearLayout.setVisibility(View.GONE);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryButton.setVisibility(View.GONE);
                volleyError.setVisibility(View.GONE);
                linlaHeaderProgress.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                sendXmlRequest(view);
            }
        });

        if (savedInstanceState != null) {
            articleList = savedInstanceState.getParcelableArrayList(STATE_ARTICLES);
            rPubAdapter.setArticleList(articleList);
        } else if (getRequestUrl().equals("LOCAL")) {
            linlaHeaderProgress.setVisibility(View.GONE);
            articleList = getSavedBookmarks();
            rPubAdapter.setArticleList(articleList);
            if (articleList.size() == 1) {
                noBookmarksFound.setVisibility(View.VISIBLE);
            }
        } else {
            sendXmlRequest(view);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_display_article_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private ArrayList<Article> getSavedBookmarks() {
        ArrayList<Article> articleList = new ArrayList<>();
        File bmDir = getActivity().getDir(getString(R.string.BOOKMARK_DIRECTORY), Context.MODE_PRIVATE);
        File[] f = bmDir.listFiles();

        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }
        };

        Arrays.sort(f,comparator);

        for (int i = 0; i < f.length; i++) {
            try {
                if (f[i].getName().contains("content"))
                    continue;
                FileInputStream fis = new FileInputStream(f[i]);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                int id = Integer.parseInt(f[i].getName());
                Article newArticle = new Article(id);
                newArticle.setTitle(br.readLine());
                newArticle.setLink(br.readLine());
                newArticle.setPhoto(br.readLine());
                newArticle.setDescription(br.readLine());
                newArticle.setDate(br.readLine());
                newArticle.setAuthor(br.readLine());
                String length = br.readLine();
                newArticle.setLength(Integer.parseInt(length));
                articleList.add(newArticle);
                br.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return articleList;
    }

    private void hideViews() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    private void sendXmlRequest(final View view) {
        final LinearLayout linlaHeaderProgress = (LinearLayout)
                view.findViewById(R.id.linlaHeaderProgress);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryButton.setVisibility(View.GONE);
                volleyError.setVisibility(View.GONE);
                linlaHeaderProgress.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                sendXmlRequest(view);
            }
        });
        StringRequest stringRequest = new StringRequest(getRequestUrl(),
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                linlaHeaderProgress.setVisibility(View.GONE);
                volleyError.setVisibility(View.GONE);
                if (getRequestUrl().contains("search")) {
                    parseSearch = new ParsingFactory(response, 1);
                    parseSearch.processSearchOrFeaturedXml(false);
                    articleList = parseSearch.getArticles();
                }
                else {
                    parseApp = new ParsingFactory(response, 1);
                    parseApp.processXml();
                    articleList = parseApp.getArticles();
                }
                if (articleList.size() == 0) {
                    linlaHeaderProgress.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    noArticles.setVisibility(View.VISIBLE);
                }
                rPubAdapter.setArticleList(articleList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                linlaHeaderProgress.setVisibility(View.GONE);
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
                    volleyError.setText(error.toString() + ": " + getRequestUrl());
                }
                retryButton.setVisibility(View.VISIBLE);

            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_ARTICLES, articleList);
    }

    @Override
    public void itemClicked(View view, int position) {
        final Intent intent;
        intent = new Intent(getActivity(), ArticleReader.class);
        intent.putExtra("ARTICLE_LIST", articleList);
        intent.putExtra("POSITION", position);
        if (title != null)
            intent.putExtra("CAT_TITLE", title);
        else
            intent.putExtra("CAT_TITLE", getTitle());
        intent.putExtra(getString(R.string.PARENT_CLASS_TAG), getString(R.string.DISPLAY_FRAGMENT_TAG));
        intent.putExtra("URL", getRequestUrl());
        startActivity(intent);
    }

}
