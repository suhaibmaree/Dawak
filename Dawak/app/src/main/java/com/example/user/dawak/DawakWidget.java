package com.example.user.dawak;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class DawakWidget extends AppWidgetProvider {

    public static String medicines;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String medicine) {
        Log.d("medic", medicine);
        medicines = medicine;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dawak_widget);
        views.setTextViewText(R.id.appwidget_text, medicine);
        Intent intent = new Intent(context,ProfileActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId,views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, medicines);
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

