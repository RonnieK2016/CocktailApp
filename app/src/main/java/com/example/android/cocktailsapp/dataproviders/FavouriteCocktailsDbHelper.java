package com.example.android.cocktailsapp.dataproviders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract.CocktailRecord;

/**
 * Created by angelov on 6/26/2018.
 */

public class FavouriteCocktailsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favouriteCocktails.db";

    private static final int VERSION = 1;

    FavouriteCocktailsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + CocktailRecord.FAVOURITE_COCKTAILS_TABLE_NAME + " (" +
                CocktailRecord.ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CocktailRecord.DB_ID               + " INTEGER NOT NULL, " +
                CocktailRecord.TITLE               + " TEXT NOT NULL, " +
                CocktailRecord.POSTER_PATH         + " TEXT, " +
                CocktailRecord.VOTE_AVERAGE        + " REAL NOT NULL, " +
                CocktailRecord.OVERVIEW            + " TEXT NOT NULL, " +
                CocktailRecord.RELEASE_DATE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CocktailRecord.FAVOURITE_COCKTAILS_TABLE_NAME);
        onCreate(db);
    }
}
