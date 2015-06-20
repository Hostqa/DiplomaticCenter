package qa.dcsdr.diplomaticclub.Items;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;

import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/20/2015.
 */
public class MyPreferenceCategory extends PreferenceCategory {

    public MyPreferenceCategory(Context context) {
        super(context);
        setLayoutResource(R.layout.category_preference_layout);
    }

    public MyPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.category_preference_layout);
    }
}