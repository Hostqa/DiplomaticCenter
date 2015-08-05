package qa.dcsdr.diplomaticclub.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

import qa.dcsdr.diplomaticclub.Adapters.RecentAlertsAdapter;
import qa.dcsdr.diplomaticclub.Items.Alert;
import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.R;


/**
 * Created by Tamim on 6/17/2015.
 * This is the fragment for the authors list.
 */
public class RecentAlertsFragment extends Fragment implements ClickListener {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView noRecentAlerts;
    public RecentAlertsAdapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoryTest.
     */
    public static RecentAlertsFragment newInstance() {
        RecentAlertsFragment fragment = new RecentAlertsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RecentAlertsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.activity_recent_alerts, container, false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        noRecentAlerts = (TextView) view.findViewById(R.id.noRecentAlerts);
        recyclerView = (RecyclerView) view.findViewById(R.id.alertsList);
        adapter = new RecentAlertsAdapter(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ArrayList alerts = getListOfSavedAlerts();
        Collections.reverse(alerts);
        adapter.setList(alerts);
        if (alerts.size()>0)
            hideNoAlerts();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        if (activity.getSupportActionBar() != null)
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout dl = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getChildFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, dl, toolbar, true);


        return view;
    }

    private void hideNoAlerts() {
        noRecentAlerts.setVisibility(View.GONE);
    }

    private void showNoAlerts() {
        noRecentAlerts.setVisibility(View.VISIBLE);
    }

    private ArrayList<Alert> getListOfSavedAlerts() {
        final ArrayList<Alert> lines = new ArrayList<>();
        File file = new File(getActivity().getFilesDir(), "SAVED_ALERTS"); //Getting a file within the dir.
        if (file.exists()) {
            final Scanner reader;
            try {
                reader = new Scanner(new FileInputStream(file), "UTF-8");
                while (reader.hasNextLine()) {
                    String s = reader.nextLine();
                    Log.d("READERAA", s);
                    Log.d("READERAA", "AAA");
                    JSONObject jsonObject = new JSONObject(s);
                    Alert a = getAlertFromObject(jsonObject);
                    lines.add(a);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lines;
    }


    public void clearAllAlerts() {
        final File file = new File(getActivity().getFilesDir(), "SAVED_ALERTS"); //Getting a file within the dir.
        if (file.exists()) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(getString(R.string.CLEAR_ALL_MESSAGE))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.YES), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (!file.delete()) {
                                Log.d("Clear All", "Cannot delete file: " + file.getName());
                                return;
                            }
                            ArrayList<Alert> alerts = new ArrayList<>();
                            adapter.setList(alerts);
                            showNoAlerts();
                        }
                    })
                    .setNegativeButton(getString(R.string.NO), null)
                    .show();
        }
    }

    private Alert getAlertFromObject(JSONObject jsonObject) throws JSONException {
        String s = Locale.getDefault().getDisplayLanguage();
        int articleID = jsonObject.getInt("ArticleID");
        String message = "";
        if (s.equalsIgnoreCase("english")) {
            message = jsonObject.getString("EN");
        } else if (s.equalsIgnoreCase("العربية")) {
            message = jsonObject.getString("AR");
        } else if (s.equalsIgnoreCase("français")) {
            message = jsonObject.getString("FR");
        }
        Alert newAlert = new Alert(message);
        Log.d("articleID", articleID + "");
        newAlert.setArticleID(articleID);
        return newAlert;
    }


    @Override
    public void itemClicked(View view, int position) {
        final Intent intent;
    }

}