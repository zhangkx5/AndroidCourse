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
import android.widget.RemoteViews;
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

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
            rv.setImageViewResource(R.id.appwidget_image, R.mipmap.dynamic);
            rv.setTextViewText(R.id.appwidget_text, name);
            Intent clickInt = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, clickInt, 0);
            rv.setOnClickPendingIntent(R.id.appwidget_image, pi);

            AppWidgetManager appWidgetManger = AppWidgetManager.getInstance(context);
            int[] appIds = appWidgetManger.getAppWidgetIds(new ComponentName(context, WidgetDemo.class));
            appWidgetManger.updateAppWidget(appIds, rv);
        }
    }
}
