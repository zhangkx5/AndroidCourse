package com.example.kaixin.experimentfour;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetDemo extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent clickInt = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, clickInt, 0);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
        rv.setOnClickPendingIntent(R.id.appwidget_image, pi);
        appWidgetManager.updateAppWidget(appWidgetIds, rv);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
        if (intent.getAction().equals("com.example.kaixin.experimentfour.staticreceiver")) {
            String name = intent.getStringExtra("name");
            int imageId = intent.getIntExtra("imageId", 0);
            rv.setImageViewResource(R.id.appwidget_image, imageId);
            rv.setTextViewText(R.id.appwidget_text, name);
            Intent clickInt = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, clickInt, 0);
            rv.setOnClickPendingIntent(R.id.appwidget_image, pi);

            AppWidgetManager appWidgetManger = AppWidgetManager.getInstance(context);
            int[] appIds = appWidgetManger.getAppWidgetIds(new ComponentName(context, WidgetDemo.class));
            appWidgetManger.updateAppWidget(appIds, rv);
        }
    }
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

