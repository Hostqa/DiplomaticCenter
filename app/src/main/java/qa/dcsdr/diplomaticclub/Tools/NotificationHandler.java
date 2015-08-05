package qa.dcsdr.diplomaticclub.Tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import qa.dcsdr.diplomaticclub.Activities.ArticleReader;
import qa.dcsdr.diplomaticclub.Activities.HomePageActivity;
import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 8/2/2015.
 */
public class NotificationHandler extends ParsePushBroadcastReceiver {


    private ParsingFactory parseApp;
    private int articleID;

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        try {
            JSONObject jsonObject = new JSONObject(intent.getExtras().get("com.parse.Data").toString());
            saveToDisk(context, jsonObject);
//            JSONObject a = new JSONObject(jsonObject.toString());
            String s = Locale.getDefault().getDisplayLanguage();
            String title = context.getResources().getString(R.string.title_activity_main);
            articleID = jsonObject.getInt("ArticleID");
            if (s.equalsIgnoreCase("english")) {
                String enMessage = jsonObject.getString("EN");
                generateNotification(context, title, enMessage);
            } else if (s.equalsIgnoreCase("العربية")) {
                String arMessage = jsonObject.getString("AR");
                generateNotification(context, title, arMessage);
            } else if (s.equalsIgnoreCase("français")) {
                String frMessage = jsonObject.getString("FR");
                generateNotification(context, title, frMessage);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveToDisk(Context context, JSONObject jsonObject) {
        final ArrayList<String> lines = new ArrayList<>();
        File file = new File(context.getFilesDir(), "SAVED_ALERTS"); //Getting a file within the dir.
        if (file.exists()) {
            final Scanner reader;
            try {
                reader = new Scanner(new FileInputStream(file), "UTF-8");
                while (reader.hasNextLine())
                    lines.add(reader.nextLine());
                reader.close();
                if (lines.size() >= 5) {
                    lines.remove(0);
                }
                lines.add(jsonObject.toString());
                final BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
                for (final String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            lines.add(jsonObject.toString());
            try {
                final BufferedWriter writer;
                writer = new BufferedWriter(new FileWriter(file, false));
                for (final String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private StringRequest getStringRequest(final NotificationManager notificationManager, final NotificationCompat.Builder builder, final Context context, final String url, final int ID, final String title, final String text) {
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
                intent.putExtra("CAT_TITLE", c.getTitle());
                intent.putExtra(context.getResources().getString(R.string.PARENT_CLASS_TAG),
                        context.getResources().getString(R.string.DISPLAY_FRAGMENT_TAG));
                intent.putExtra("URL", url);

                // create pending intent and add activity intent
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                // Add as notification
                notificationManager.notify(1, builder.build());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }


    public void generateNotification(Context context, String title, String text) {

        VolleySingleton volleySingleton = VolleySingleton.getsInstance();
        RequestQueue requestQueue = volleySingleton.getRequestQueue();
        String url = context.getResources().getString(R.string.SINGLE_ARTICLE_ID) + articleID;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.n_icon_1);
        builder.setLights(0xffff0000, 2000, 2000);
        builder.setAutoCancel(true);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        builder.setColor(context.getResources().getColor(R.color.colorPrimary));

        if (articleID != -1)
            requestQueue.add(getStringRequest(notificationManager, builder, context, url, articleID, title, text));
        else {
            // create intent to start your activity
            Intent intent = new Intent(context, HomePageActivity.class);

            // create pending intent and add activity intent
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            notificationManager.notify(1, builder.build());

        }

    }

}














