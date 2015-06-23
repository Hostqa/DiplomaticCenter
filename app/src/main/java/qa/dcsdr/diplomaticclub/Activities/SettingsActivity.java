package qa.dcsdr.diplomaticclub.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import qa.dcsdr.diplomaticclub.Fragments.SettingsFragment;
import qa.dcsdr.diplomaticclub.R;
import qa.dcsdr.diplomaticclub.Tools.MyApplication;

/**
 * Created by Tamim on 6/20/2015.
 */
public class SettingsActivity extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(getString(R.string.SETTINGS));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//    getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new SettingsFragment())
                .commit();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Allow super to try and create a view first
        final View result = super.onCreateView(name, context, attrs);
        if (result != null) {
            return result;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // If we're running pre-L, we need to 'inject' our tint aware Views in place of the
            // standard framework versions
            switch (name) {
                case "EditText":
                    return new AppCompatEditText(this, attrs);
                case "Spinner":
                    return new AppCompatSpinner(this, attrs);
                case "CheckBox":
                    return new AppCompatCheckBox(this, attrs);
                case "RadioButton":
                    return new AppCompatRadioButton(this, attrs);
                case "CheckedTextView":
                    return new AppCompatCheckedTextView(this, attrs);
            }
        }

        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("LANGUAGE_KEY")) {
            ((MyApplication) getApplication()).setLocale();
            recreate();
            restartActivity();
        }
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
