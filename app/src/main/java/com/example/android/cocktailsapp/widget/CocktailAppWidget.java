package com.example.android.cocktailsapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.android.cocktailsapp.R;
import com.example.android.cocktailsapp.activities.CocktailDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class CocktailAppWidget extends AppWidgetProvider {

    static final String COCKTAIL_WIDGET_ID_INTENT_TAG = "COCKTAIL_WIDGET_ID_INTENT_TAG";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, GridWidgetService.class);
        intent.putExtra(COCKTAIL_WIDGET_ID_INTENT_TAG, appWidgetId);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cocktail_widget_main_layout);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        Intent appIntent = new Intent(context, CocktailDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);

        ComponentName component = new ComponentName(context, CocktailAppWidget.class);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_grid_view);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(component, views);
    }

    public static void updateCocktailWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        CocktailWidgetService.startActionUpdateCocktailWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
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

