package com.example.android.cocktailsapp.utils;

import android.content.ContentValues;

import com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract.CocktailRecord;
import com.example.android.cocktailsapp.domain.Cocktail;

/**
 * Created by angelov on 6/26/2018.
 */

public final class ConverterUtils {

    public static ContentValues movieToContentValues(Cocktail cocktail) {
        ContentValues values = new ContentValues();

        values.put(CocktailRecord.DB_ID, cocktail.getId());
        values.put(CocktailRecord.TITLE, cocktail.getCocktailName());
        /*
        values.put(CocktailRecord.POSTER_PATH, cocktail.getPosterPath());
        values.put(CocktailRecord.VOTE_AVERAGE, cocktail.getVoteAverage());
        values.put(CocktailRecord.OVERVIEW, cocktail.getOverview());
        values.put(CocktailRecord.RELEASE_DATE, cocktail.getReleaseDate());
        */
        return values;
    }
}
