package qa.dcsdr.diplomaticclub.Tools;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/18/2015.
 * This returns the content for a single article.
 */
public class ArticleContent {

    private final RequestQueue requestQueue;
    private final VolleySingleton volleySingleton;
    private final Context context;
    private String url;
    private Menu menu = null;

    public int getId() {
        return id;
    }

    public void setIdAndUrl(String url, int id) {
        this.id = id;
        this.url = url + id;
    }

    private int id;

    public void setUrl(String url) {
        this.url = url;
    }

    public ArticleContent(int id, String url, Context context) {
        this.id = id;
        this.url = url + id;
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
        this.context = context;
    }

    private String processXml(String data) {
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
            return "error";
        }
    }

    private Article processBookmark(String data) {
        boolean inEntry = false;
        String textValue = "";
        Article currentArticle = null;
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
                        currentArticle = new Article(id);
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
                            currentArticle.setContent(textValue);
                            currentArticle.setLength(textValue.length());
                        } else if (tag.equalsIgnoreCase("title"))
                            currentArticle.setTitle(textValue);
                        else if (tag.equalsIgnoreCase("link"))
                            currentArticle.setLink(textValue);
                        else if (tag.equalsIgnoreCase("photo"))
                            currentArticle.setPhoto(textValue);
                        else if (tag.equalsIgnoreCase("description"))
                            currentArticle.setDescription(textValue);
                        else if (tag.equalsIgnoreCase("date"))
                            currentArticle.setDate(textValue);
                        else if (tag.equalsIgnoreCase("writer"))
                            currentArticle.setAuthor(textValue);
                    }
                }
                eventType = xpp.next();
            }
            return currentArticle;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendXmlRequest(final View view) {
        final LinearLayout readerProgressBar = (LinearLayout) view.findViewById(R.id.readerProgressBar);
        final ScrollView articleScroll = (ScrollView) view.findViewById(R.id.articleScroll);
        articleScroll.setVisibility(View.GONE);
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
                    Log.d("C", content);
                    if (content.equals("error")) {
                        readerProgressBar.setVisibility(View.GONE);
                        errorLayoutR.setVisibility(View.VISIBLE);
                        volleyError.setText(context.getString(R.string.BAD_CONTENT));
                        volleyError.setVisibility(View.VISIBLE);
                        articleScroll.setVisibility(View.GONE);
                    } else if (content.contains("youtube")) {
                        readerProgressBar.setVisibility(View.GONE);
                        errorLayoutR.setVisibility(View.VISIBLE);
                        volleyError.setText(context.getString(R.string.BAD_CONTENT));
                        volleyError.setVisibility(View.VISIBLE);
                        articleScroll.setVisibility(View.GONE);
                    } else {
                        String d = new ContentDecrypter().decrypt((content));
                        if (d.contains("youtube")) {
                            readerProgressBar.setVisibility(View.GONE);
                            errorLayoutR.setVisibility(View.VISIBLE);
                            volleyError.setText(context.getString(R.string.BAD_CONTENT));
                            volleyError.setVisibility(View.VISIBLE);
                            articleScroll.setVisibility(View.GONE);
                        } else {
                            tv.setText(Html.fromHtml(d));
                            readerProgressBar.setVisibility(View.GONE);
                            articleScroll.setVisibility(View.VISIBLE);
                            if (menu != null)
                                showViews();
                        }
                    }
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

    public void bookmarkArticle() {
        StringRequest stringRequest = new StringRequest(this.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Article content = processBookmark(response);
                // Save the file here
                try {
                    File bmDir = context.getDir(context.getResources().getString(R.string.BOOKMARK_DIRECTORY), Context.MODE_PRIVATE);
                    File newBM = new File(bmDir, id + ""); //Getting a file within the dir.
                    FileOutputStream f = new FileOutputStream(newBM);
                    byte[] b = content.toStringWithoutContent().getBytes();
                    f.write(b);
                    f.close();
                    File newBMContent = new File(bmDir, id + "_content"); //Getting a file within the dir.
                    FileOutputStream f1 = new FileOutputStream(newBMContent);
                    byte[] b1 = content.getContent().getBytes();
                    f1.write(b1);
                    f1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    private void showViews() {
        menu.setGroupVisible(R.id.customizationGroup, true);
    }

}