package com.example.user.dawak;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

public class WidgetService extends IntentService {

    public static final String ACTION = "com.example.user.dawak.action.display_medicines";

    public WidgetService() {
        super("WidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION.equals(action)) {
                final  String medicines = intent.getStringExtra("medicines");
                handleActionFill(medicines);

            }
        }
    }

    public static void startActionDisplay(Context context, String medicines) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION);
        intent.putExtra("medicines", medicines);
        context.startService(intent);
    }

    private void handleActionFill(String medicines) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(this.getPackageName(), DawakWidget.class.getName()));
        for (int i = 0; i < ids.length; i++) {
            DawakWidget.updateAppWidget(this, appWidgetManager, ids[i], medicines);
        }
    }

}
