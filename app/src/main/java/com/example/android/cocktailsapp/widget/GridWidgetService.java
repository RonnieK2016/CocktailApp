package com.example.android.cocktailsapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.cocktailsapp.Constants;
import com.example.android.cocktailsapp.R;
import com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract;
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.utils.ConverterUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract.FAVOURITE_COCKTAILS_COLUMNS;
import static com.example.android.cocktailsapp.widget.CocktailAppWidget.COCKTAIL_WIDGET_ID_INTENT_TAG;

/**
 * Created by angelov on 10/5/2018.
 */

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = 0;

        if(intent.hasExtra(COCKTAIL_WIDGET_ID_INTENT_TAG)) {
            appWidgetId = intent.getIntExtra(COCKTAIL_WIDGET_ID_INTENT_TAG, 0);
        }

        return new GridRemoteViewsFactory(this.getApplicationContext(), appWidgetId);
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Cocktail> cocktails;
    private int mWidgetId;

    public GridRemoteViewsFactory(Context applicationContext, int widgetId) {
        mContext = applicationContext;
        mWidgetId = widgetId;
        cocktails = new ArrayList<>();
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        // Get all plant info ordered by creation time
        if (cocktails != null) cocktails.clear();

        Cursor cursor = mContext.getContentResolver().query(
                FavouriteCocktailsDbContract.CocktailRecord.CONTENT_URI,
                FAVOURITE_COCKTAILS_COLUMNS,
                null,
                null,
                FavouriteCocktailsDbContract.CocktailRecord.ID + " DESC"
        );

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            int count = 0;

            do {
                cocktails.add(ConverterUtils.readCursorAsCocktail(cursor));
                count++;
            }
            while (count < 3 && cursor.moveToNext());
        }
    }

    @Override
    public void onDestroy() {
        cocktails.clear();
    }

    @Override
    public int getCount() {
        if (cocktails == null) return 0;
        return cocktails.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {

        final Cocktail selectedCocktail = cocktails.get(position);

        final RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.cocktail_app_widget_grid_item);

        try {
            Bitmap b = Picasso.with(mContext)
                .load(selectedCocktail.getImageUrl()).get();

            if(b != null) {
                views.setImageViewBitmap(R.id.cocktail_widget_image, b);
            }
            else {
                views.setImageViewResource(R.id.cocktail_widget_image, R.drawable.ic_cocktail_placeholder);
            }
            views.setContentDescription(R.id.cocktail_widget_image, selectedCocktail.getCocktailName());
        } catch (IOException e) {
            views.setImageViewResource(R.id.cocktail_widget_image, R.drawable.ic_cocktail_placeholder);
            e.printStackTrace();
        }

        views.setTextViewText(R.id.cocktail_widget_name, selectedCocktail.getCocktailName());

        Bundle extras = new Bundle();
        extras.putParcelable(Constants.COCKTAIL_DETAIL_INTENT_TAG, selectedCocktail);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.cocktail_widget_image, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

