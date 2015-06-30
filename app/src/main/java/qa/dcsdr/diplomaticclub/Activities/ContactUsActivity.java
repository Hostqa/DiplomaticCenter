package qa.dcsdr.diplomaticclub.Activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import qa.dcsdr.diplomaticclub.Fragments.NavigationDrawerFragment;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/25/2015.
 * This activity displays the map for the user and all the necessary
 * contact information.
 */
public class ContactUsActivity extends AppCompatActivity {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private Toolbar toolbar;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private EditText name;
    private EditText email;
    private EditText subject;
    private EditText message;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, true);

        setUpMap();

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        subject = (EditText) findViewById(R.id.subject);
        message = (EditText) findViewById(R.id.message);
        submit = (Button) findViewById(R.id.submitForm);

        final Context a = this;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() == 0)
                    Toast.makeText(a, ContactUsActivity.this.getString(R.string.PLEASE_ENTER_NAME), Toast.LENGTH_SHORT).show();
                else if (email.getText().length() == 0)
                    Toast.makeText(a, ContactUsActivity.this.getString(R.string.PLEASE_ENTER_EMAIL), Toast.LENGTH_SHORT).show();
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches())
                    Toast.makeText(a, ContactUsActivity.this.getString(R.string.PLEASE_ENTER_EMAIL_CORRECT), Toast.LENGTH_SHORT).show();
                else if (subject.getText().length() == 0)
                    Toast.makeText(a, ContactUsActivity.this.getString(R.string.PLEASE_ENTER_SUBJECT), Toast.LENGTH_SHORT).show();
                else if (message.getText().length() == 0)
                    Toast.makeText(a, ContactUsActivity.this.getString(R.string.PLEASE_ENTER_MESSAGE), Toast.LENGTH_SHORT).show();
                else {
                    String postUrl = getString(R.string.CONTACT_US_FORM_URL);
                    HashMap<String, String> formParam = new HashMap<String, String>();
                    formParam.put("name", name.getText().toString());
                    formParam.put("email", email.getText().toString());
                    formParam.put("subject", subject.getText().toString());
                    formParam.put("message", message.getText().toString());

                    performPostCall(postUrl, formParam);
                }
            }
        });
    }

    /**
     * Submits the POST request for the Contact Us page.
     * @param requestURL
     * @param formParam
     */
    private void performPostCall(String requestURL, final HashMap<String, String> formParam) {
        final Context a = this;
        StringRequest sr = new StringRequest(Request.Method.POST,
                requestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toLowerCase().contains("success")) {
                            Toast.makeText(a, getString(R.string.MESSAGE_SUCCESS_CONTACT_US), Toast.LENGTH_SHORT).show();
                            name.setText("");
                            email.setText("");
                            subject.setText("");
                            message.setText("");
                        }
                        else {
                            Toast.makeText(a, getString(R.string.ERROR_CONTACT_US), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(a, getString(R.string.ERROR_CONTACT_US), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return formParam;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(sr);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * Sets up the correct pin for DCSDR.
     */
    private void setUpMap() {
        LatLng latLng = new LatLng(25.3667129, 51.5290834);
        MarkerOptions marker = new MarkerOptions().position(latLng).title(getResources().getString(R.string.APP_TITLE));
        if (mMap==null) return;
        mMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }

}