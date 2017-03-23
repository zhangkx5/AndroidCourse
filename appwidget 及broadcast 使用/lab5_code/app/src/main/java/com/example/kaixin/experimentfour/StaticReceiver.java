package com.example.kaixin.experimentfour;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by kaixin on 2016/10/21.
 */
public class StaticReceiver extends BroadcastReceiver{
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.kaixin.experimentfour.staticreceiver")) {
            String name = intent.getStringExtra("name");
            int imageId = intent.getIntExtra("imageId", 0);
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(),imageId);
            Intent intent1 = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("静态广播")
                    .setContentText(name)
                    .setTicker("您有一条新消息")
                    .setLargeIcon(bm)
                    .setSmallIcon(imageId)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify = builder.build();
            manager.notify(0, notify);
            //Toast.makeText(context,"staticreceiver",Toast.LENGTH_SHORT);
        }
    }
}

