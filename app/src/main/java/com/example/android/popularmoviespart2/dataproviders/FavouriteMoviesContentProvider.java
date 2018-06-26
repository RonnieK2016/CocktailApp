package com.example.android.popularmoviespart2.dataproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.popularmoviespart2.dataproviders.FavouriteMoviesDbContract.MovieRecord;

/**
 * Created by angelov on 6/26/2018.
 */

public class FavouriteMoviesContentProvider extends ContentProvider {

    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavouriteMoviesDbHelper favouriteMoviesDbHelper;


    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(FavouriteMoviesDbContract.CONTENT_AUTHORITY, FavouriteMoviesDbContract.PATH_FAVOURITE_MOVIES, MOVIE);
        uriMatcher.addURI(FavouriteMoviesDbContract.CONTENT_AUTHORITY, FavouriteMoviesDbContract.PATH_FAVOURITE_MOVIES
                + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        favouriteMoviesDbHelper = new FavouriteMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase favouriteMoviesDb = favouriteMoviesDbHelper.getReadableDatabase();

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)){
            case MOVIE:{
                return MovieRecord.CONTENT_DIR_TYPE;
            }
            case MOVIE_WITH_ID:{
                return MovieRecord.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase favouriteMoviesDb = favouriteMoviesDbHelper.getWritableDatabase();

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase favouriteMoviesDb = favouriteMoviesDbHelper.getWritableDatabase();

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase favouriteMoviesDb = favouriteMoviesDbHelper.getWritableDatabase();

        return 0;
    }
}
