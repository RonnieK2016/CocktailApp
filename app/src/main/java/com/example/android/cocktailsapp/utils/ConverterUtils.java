package com.example.android.cocktailsapp.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract.CocktailRecord;
import com.example.android.cocktailsapp.domain.Cocktail;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by angelov on 6/26/2018.
 */

public final class ConverterUtils {

    private static final String ARRAY_ITEM_SEPARATOR = ";";

    public static ContentValues cocktailToContentValues(Cocktail cocktail) {
        ContentValues values = new ContentValues();

        values.put(CocktailRecord.DB_ID, cocktail.getId());
        values.put(CocktailRecord.COCKTAIL_NAME, cocktail.getCocktailName());
        values.put(CocktailRecord.CATEGORY, cocktail.getCategory());
        values.put(CocktailRecord.ALCOHOLIC, cocktail.getAlcoholic());
        values.put(CocktailRecord.COCKTAIL_IMAGE_URL, cocktail.getImageUrl());
        values.put(CocktailRecord.INSTRUCTIONS, cocktail.getInstructions());
        values.put(CocktailRecord.INGREDIENTS, arrayOfStringsToString(cocktail.getIngredients()));
        values.put(CocktailRecord.MEASUREMENTS, arrayOfStringsToString(cocktail.getMeasurements()));
        return values;
    }

    public static Cocktail readCursorAsCocktail(Cursor cursor) {

        int remoteDbId = cursor.getInt(cursor.getColumnIndex(CocktailRecord.DB_ID));
        int localDbId = cursor.getInt(cursor.getColumnIndex(CocktailRecord.ID));
        String cocktailName = cursor.getString(cursor.getColumnIndex(CocktailRecord.COCKTAIL_NAME));
        String category = cursor.getString(cursor.getColumnIndex(CocktailRecord.CATEGORY));
        String alcoholic = cursor.getString(cursor.getColumnIndex(CocktailRecord.ALCOHOLIC));
        String imageUrl = cursor.getString(cursor.getColumnIndex(CocktailRecord.COCKTAIL_IMAGE_URL));
        String instructions = cursor.getString(cursor.getColumnIndex(CocktailRecord.INSTRUCTIONS));
        String[] ingredients = stringToArrayOfString(cursor.getString(cursor.getColumnIndex(CocktailRecord.INGREDIENTS)));
        String[] measurements = stringToArrayOfString(cursor.getString(cursor.getColumnIndex(CocktailRecord.MEASUREMENTS)));

        return new Cocktail(remoteDbId, localDbId, cocktailName, category, alcoholic, imageUrl, instructions, ingredients, measurements);
    }

    private static String arrayOfStringsToString(String[] inputArray) {
        StringBuilder result = new StringBuilder();
        if(!ArrayUtils.isEmpty(inputArray)) {
            for(int i = 0; i < inputArray.length; i++) {
                result.append(inputArray[i]);
                if(i < (inputArray.length - 1)) {
                    result.append(ARRAY_ITEM_SEPARATOR);
                }
            }
        }
        return result.toString();
    }

    private static String[] stringToArrayOfString(String inputString) {
        if(StringUtils.isEmpty(inputString)) {
            return null;
        }

        return inputString.split(ARRAY_ITEM_SEPARATOR, -1);
    }



}
