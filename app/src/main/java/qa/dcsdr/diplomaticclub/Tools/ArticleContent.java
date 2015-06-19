package qa.dcsdr.diplomaticclub.Tools;

import android.text.Html;
import android.util.Log;
import android.view.View;
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/18/2015.
 */
public class ArticleContent {

    private final RequestQueue requestQueue;
    private final VolleySingleton volleySingleton;
    private int id;
    private String url;

    public ArticleContent(int id, String url) {
        this.id = id;
        this.url = url+id;
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public String processXml(String data) {
        String content = "";
        boolean inEntry = false;
        String textValue = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(data));
            int eventType = xpp.getEventType();
            String item = "item";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag = xpp.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if (tag.toLowerCase().contains(item.toLowerCase())) {
                        inEntry = true;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    textValue = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG) {
                    if (inEntry) {
                        if (tag.toLowerCase().contains(item.toLowerCase())) {
                            inEntry = false;
                        }
                        if (tag.equalsIgnoreCase("content")) {
                            content = textValue;
                        }
                    }
                }
                eventType = xpp.next();
            }
            return content;

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public void sendXmlRequest(final View view) {
        final LinearLayout readerProgressBar = (LinearLayout) view.findViewById(R.id.readerProgressBar);
        final LinearLayout errorLayoutR = (LinearLayout) view.findViewById(R.id.errorLayoutR);
        final TextView volleyError = (TextView) view.findViewById(R.id.volleyErrorR);
        final Button retryButton = (Button) view.findViewById(R.id.retryButtonR);
        readerProgressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(this.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String content = processXml(response);
                TextView tv = (TextView) view.findViewById(R.id.articleContents);
                try {
                    Log.d("CONTENT", content);
                    Log.d("response", response);
                    tv.setText(Html.fromHtml(new ContentDecrypter().decrypt((content))));
                    readerProgressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                readerProgressBar.setVisibility(View.GONE);
                errorLayoutR.setVisibility(View.VISIBLE);
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

}