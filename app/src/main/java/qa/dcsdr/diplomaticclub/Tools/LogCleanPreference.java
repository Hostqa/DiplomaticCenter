package qa.dcsdr.diplomaticclub.Tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;

import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/21/2015.
 */
public class LogCleanPreference extends ListPreference {

    private int mClickedDialogEntryIndex;
    private Context mContext;

    public LogCleanPreference(Context ctxt) {
        this(ctxt, null);
    }

    public LogCleanPreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);
        mContext = ctxt;
        setNegativeButtonText(ctxt.getString(R.string.CANCEL));
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        if (getEntries() == null || getEntries() == null) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array.");
        }

        mClickedDialogEntryIndex = findIndexOfValue(getValue());
        builder.setSingleChoiceItems(getEntries(), mClickedDialogEntryIndex,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        if (which == 1) {
                            // Show AlertDialog
                            new android.support.v7.app.AlertDialog.Builder(mContext)
                                    .setMessage(mContext.getString(R.string.ARABIC_SUPPORT))
                                    .setCancelable(true)
                                    .setPositiveButton(mContext.getString(R.string.YES), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog1, int id) {
                                            // Save preference and close dialog
                                            mClickedDialogEntryIndex = which;
                                            LogCleanPreference.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                                            dialog.dismiss();

                                        }
                                    })
                                    .setNegativeButton(mContext.getString(R.string.NO), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            return;
                                        }
                                    })
                                    .show();
                        } else {
                            // Save preference and close dialog
                            mClickedDialogEntryIndex = which;
                            LogCleanPreference.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            dialog.dismiss();
                        }
                    }
                });

        builder.setPositiveButton(null, null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {

        CharSequence[] mEntryValues = getEntryValues();

        if (positiveResult && mClickedDialogEntryIndex >= 0 && mEntryValues != null) {
            String value = mEntryValues[mClickedDialogEntryIndex].toString();
            if (callChangeListener(value)) {
                setValue(value);
            }
        }
    }
}