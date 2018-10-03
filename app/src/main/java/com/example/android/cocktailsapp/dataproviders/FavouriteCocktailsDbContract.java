package com.example.android.cocktailsapp.dataproviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by angelov on 6/26/2018.
 */

public class FavouriteCocktailsDbContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.cocktailsapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String[] FAVOURITE_COCKTAILS_COLUMNS = new String[] {
            CocktailRecord.ID,
            CocktailRecord.DB_ID,
            CocktailRecord.COCKTAIL_NAME,
            CocktailRecord.COCKTAIL_IMAGE_URL,
            CocktailRecord.CATEGORY,
            CocktailRecord.ALCOHOLIC,
            CocktailRecord.INSTRUCTIONS,
            CocktailRecord.INGREDIENTS,
            CocktailRecord.MEASUREMENTS
    };

    public static final class CocktailRecord implements BaseColumns {

        // table name
        public static final String FAVOURITE_COCKTAILS_TABLE_NAME = "FAVOURITE_COCKTAILS";

        // columns
        public static final String ID="ID";
        public static final String DB_ID = "DB_ID";
        public static final String COCKTAIL_NAME = "COCKTAIL_NAME";
        public static final String COCKTAIL_IMAGE_URL = "COCKTAIL_IMAGE_URL";
        public static final String CATEGORY = "CATEGORY";
        public static final String ALCOHOLIC = "ALCOHOLIC";
        public static final String INSTRUCTIONS = "INSTRUCTIONS";
        public static final String INGREDIENTS = "INGREDIENTS";
        public static final String MEASUREMENTS = "MEASUREMENTS";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FAVOURITE_COCKTAILS_TABLE_NAME).build();

        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FAVOURITE_COCKTAILS_TABLE_NAME;

        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + FAVOURITE_COCKTAILS_TABLE_NAME;

        public static Uri buildUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
