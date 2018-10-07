package com.example.android.cocktailsapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.android.cocktailsapp.R;
import com.example.android.cocktailsapp.cocktailsdb.CocktailDbHttpResponse;
import com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract.CocktailRecord;
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.domain.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelov on 6/26/2018.
 */

public final class ConverterUtils {

    public static ContentValues cocktailToContentValues(Cocktail cocktail) {
        ContentValues values = new ContentValues();

        values.put(CocktailRecord.DB_ID, cocktail.getId());
        values.put(CocktailRecord.COCKTAIL_NAME, cocktail.getCocktailName());
        values.put(CocktailRecord.CATEGORY, cocktail.getCategory());
        values.put(CocktailRecord.ALCOHOLIC, cocktail.getAlcoholic());
        values.put(CocktailRecord.COCKTAIL_IMAGE_URL, cocktail.getImageUrl());
        values.put(CocktailRecord.INSTRUCTIONS, cocktail.getInstructions());
        values.put(CocktailRecord.INGREDIENTS, listToJsonString(cocktail.getIngredients()));
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
        List<Ingredient> ingredients = jsonStringToList(cursor.getString(cursor.getColumnIndex(CocktailRecord.INGREDIENTS)));

        return new Cocktail(remoteDbId, localDbId, cocktailName, category, alcoholic, imageUrl, instructions, ingredients);
    }

    private static String listToJsonString(List<Ingredient> ingredients) {
        Gson gson = new Gson();
        return gson.toJson(ingredients);
    }

    private static List<Ingredient> jsonStringToList(String inputString) {
        if(StringUtils.isEmpty(inputString)) {
            return null;
        }
        Gson gson = new Gson();
        TypeToken<ArrayList<Ingredient>> typeToken = new TypeToken<ArrayList<Ingredient>>(){};
        return gson.fromJson(inputString, typeToken.getType());
    }

    public static String cocktailToString(@NonNull Cocktail cocktail, Context context) {
        StringBuilder result = new StringBuilder();
        result.append(context.getResources().getString(R.string.cocktail_share_instructions_string)).append("\r\n")
                .append(cocktail.getInstructions()).append("\r\n").append("\r\n");
        if(!CollectionUtils.isEmpty(cocktail.getIngredients())) {
            result.append(context.getResources().getString(R.string.cocktail_share_ingredients_string)).append("\r\n");
            for(Ingredient ingredient : cocktail.getIngredients()) {
                result.append(ingredient.getIngredientName()).append(" - ").append(ingredient.getMeasurement()).append("\r\n");
            }
        }
        return  result.toString();
    }
}
