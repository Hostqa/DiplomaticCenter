package qa.dcsdr.diplomaticclub.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;
import qa.dcsdr.diplomaticclub.Tools.ArticleContent;

/**
 * Created by Tamim on 8/7/2015.
 * Opens the app through a URL from a browser.
 */
public class OpenLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolleySingleton volleySingleton = VolleySingleton.getsInstance();
        RequestQueue requestQueue = volleySingleton.getRequestQueue();
        String IRL = getIntent().getData().toString();
        if (IRL.contains("post")) {
            String[] s = getIntent().getData().toString().split("-");
            int ID = Integer.valueOf(s[s.length - 1]);
            String url = getResources().getString(R.string.SINGLE_ARTICLE_ID) + ID;
            requestQueue.add(getStringRequest(this, url, ID));
        } else {
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }
    }

    private StringRequest getStringRequest(final Context context, final String url, final int ID) {
        return new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArticleContent articleContent = new ArticleContent(ID, url, context);
                Article c = articleContent.processXmlSingle(response, ID);
                ArrayList<Article> list = new ArrayList<>();
                list.add(c);

                // create intent to start your activity
                Intent intent = new Intent(context, ArticleReader.class);
                intent.putExtra("ARTICLE_LIST", list);
                intent.putExtra("POSITION", 0);
                intent.putExtra("CAT_TITLE", context.getResources().getString(R.string.APP_TITLE));
                intent.putExtra(context.getResources().getString(R.string.PARENT_CLASS_TAG),
                        context.getResources().getString(R.string.DISPLAY_FRAGMENT_TAG));
                intent.putExtra("URL", url);
                context.startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

}
