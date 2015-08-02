package qa.dcsdr.diplomaticclub.Tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import qa.dcsdr.diplomaticclub.Activities.HomePageActivity;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 8/2/2015.
 */
public class NotificationHandler extends ParsePushBroadcastReceiver {


    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        Log.d("EXTRAS", intent.getExtras().toString());
        try {
            JSONObject jsonObject = new JSONObject(intent.getExtras().get("com.parse.Data").toString());
            String s = Locale.getDefault().getDisplayLanguage();
            String title = context.getResources().getString(R.string.title_activity_main);
            Log.d("AAAAA", s);
            Log.d("ASDSADA", jsonObject.toString());
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


    public void generateNotification(Context context, String title, String text) {
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

        // create intent to start your activity
        Intent activityIntent = new Intent(context, HomePageActivity.class);

        // create pending intent and add activity intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

}














