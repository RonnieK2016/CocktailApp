package com.example.android.popularmoviespart2.dataproviders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmoviespart2.dataproviders.FavouriteMoviesDbContract.MovieRecord;

/**
 * Created by angelov on 6/26/2018.
 */

public class FavouriteMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favouriteMovies.db";

    private static final int VERSION = 1;

    FavouriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + MovieRecord.FAVOURITE_MOVIES_TABLE_NAME + " (" +
                MovieRecord.ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieRecord.DB_ID               + " INTEGER NOT NULL, " +
                MovieRecord.TITLE               + " TEXT NOT NULL, " +
                MovieRecord.POSTER_PATH         + " TEXT, " +
                MovieRecord.VOTE_AVERAGE        + " REAL NOT NULL, " +
                MovieRecord.OVERVIEW            + " TEXT NOT NULL, " +
                MovieRecord.RELEASE_DATE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieRecord.FAVOURITE_MOVIES_TABLE_NAME);
        onCreate(db);
    }
}
