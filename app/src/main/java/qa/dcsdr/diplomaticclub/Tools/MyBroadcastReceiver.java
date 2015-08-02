package qa.dcsdr.diplomaticclub.Tools;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 8/2/2015.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("AAAA","ASFASFFA");
        String title = "Diplomatic Center";
        String text = intent.getExtras().toString();
        Log.d("AAAA",text);
        generateNotification(context, title, text);

    }

    public void generateNotification(Context context, String title, String text) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.n_icon_1);
        builder.setAutoCancel(true);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        notificationManager.notify("MyTag", 0, builder.build());
    }

}
