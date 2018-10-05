package com.example.android.cocktailsapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.cocktailsapp.R;

/**
 * Created by angelov on 10/5/2018.
 */

public class CocktailWidgetService extends IntentService {

    private static final String ACTION_UPDATE_COCKTAIL_WIDGETS = "ACTION_UPDATE_COCKTAIL_WIDGETS";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public CocktailWidgetService() {
        super(CocktailWidgetService.class.getSimpleName());
    }


    public static void startActionUpdateCocktailWidgets(Context context) {
        Intent intent = new Intent(context, CocktailWidgetService.class);
        intent.setAction(ACTION_UPDATE_COCKTAIL_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_COCKTAIL_WIDGETS.equals(action)) {
                handleActionUpdateCocktailWidgets();
            }
        }
    }

    private void handleActionUpdateCocktailWidgets() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, CocktailAppWidget.class));

        CocktailAppWidget.updateCocktailWidgets(this, appWidgetManager, appWidgetIds);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);

    }

}
