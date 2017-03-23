package com.example.kaixin.experimentfour;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

/**
 * Created by kaixin on 2016/10/22.
 */

public class DynamicReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.kaixin.experimentfour.dynamicreceiver")) {
            String name = intent.getStringExtra("name");
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.dynamic);
            Intent intent1 = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("动态广播")
                    .setContentText(name)
                    .setTicker("您有一条新消息")
                    .setLargeIcon(bm)
                    .setSmallIcon(R.mipmap.dynamic)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify = builder.build();
            manager.notify(0, notify);
        }
    }
}
