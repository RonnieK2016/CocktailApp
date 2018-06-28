package com.example.android.popularmoviespart2.dataproviders;

import android.content.ContentProvider;
import android.content.ContentUris;
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
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavouriteMoviesDbContract.CONTENT_AUTHORITY, MovieRecord.FAVOURITE_MOVIES_TABLE_NAME, MOVIE);
        uriMatcher.addURI(FavouriteMoviesDbContract.CONTENT_AUTHORITY, MovieRecord.FAVOURITE_MOVIES_TABLE_NAME
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

        Cursor resultCursor;
        switch(sUriMatcher.match(uri)){
            case MOVIE:{
                resultCursor = favouriteMoviesDb.query(
                        MovieRecord.FAVOURITE_MOVIES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case MOVIE_WITH_ID:{
                resultCursor = favouriteMoviesDb.query(
                        MovieRecord.FAVOURITE_MOVIES_TABLE_NAME,
                        projection,
                        MovieRecord.DB_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:{
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }


        return resultCursor;
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

        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                long id = favouriteMoviesDb.insert(MovieRecord.FAVOURITE_MOVIES_TABLE_NAME, null, values);
                // insert unless it is already contained in the database
                if (id > 0) {
                    returnUri = MovieRecord.buildUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert record: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase favouriteMoviesDb = favouriteMoviesDbHelper.getWritableDatabase();
        int deletedRecords;
        switch(sUriMatcher.match(uri)){
            case MOVIE:
                deletedRecords = favouriteMoviesDb.delete(
                        MovieRecord.FAVOURITE_MOVIES_TABLE_NAME, selection, selectionArgs);
                // reset sequence
                favouriteMoviesDb.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                        + MovieRecord.FAVOURITE_MOVIES_TABLE_NAME + "'");
                break;
            case MOVIE_WITH_ID:
                deletedRecords = favouriteMoviesDb.delete(MovieRecord.FAVOURITE_MOVIES_TABLE_NAME,
                        MovieRecord.ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return deletedRecords;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase favouriteMoviesDb = favouriteMoviesDbHelper.getWritableDatabase();

        return 0;
    }
}
