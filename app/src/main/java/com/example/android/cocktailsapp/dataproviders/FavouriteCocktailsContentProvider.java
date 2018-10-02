package com.example.android.cocktailsapp.dataproviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract.CocktailRecord;

/**
 * Created by angelov on 6/26/2018.
 */

public class FavouriteCocktailsContentProvider extends ContentProvider {

    private static final int COCKTAIL = 100;
    private static final int COCKTAIL_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavouriteCocktailsDbHelper favouriteCocktailsDbHelper;


    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavouriteCocktailsDbContract.CONTENT_AUTHORITY, CocktailRecord.FAVOURITE_COCKTAILS_TABLE_NAME, COCKTAIL);
        uriMatcher.addURI(FavouriteCocktailsDbContract.CONTENT_AUTHORITY, CocktailRecord.FAVOURITE_COCKTAILS_TABLE_NAME
                + "/#", COCKTAIL_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        favouriteCocktailsDbHelper = new FavouriteCocktailsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase favouriteCocktailsDb = favouriteCocktailsDbHelper.getReadableDatabase();

        Cursor resultCursor;
        switch(sUriMatcher.match(uri)){
            case COCKTAIL:{
                resultCursor = favouriteCocktailsDb.query(
                        CocktailRecord.FAVOURITE_COCKTAILS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case COCKTAIL_WITH_ID:{
                resultCursor = favouriteCocktailsDb.query(
                        CocktailRecord.FAVOURITE_COCKTAILS_TABLE_NAME,
                        projection,
                        CocktailRecord.DB_ID + " = ?",
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
            case COCKTAIL:{
                return CocktailRecord.CONTENT_DIR_TYPE;
            }
            case COCKTAIL_WITH_ID:{
                return CocktailRecord.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase favouriteCocktailsDb = favouriteCocktailsDbHelper.getWritableDatabase();

        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case COCKTAIL: {
                long id = favouriteCocktailsDb.insert(CocktailRecord.FAVOURITE_COCKTAILS_TABLE_NAME, null, values);
                // insert unless it is already contained in the database
                if (id > 0) {
                    returnUri = CocktailRecord.buildUri(id);
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
        SQLiteDatabase favouriteCocktailsDb = favouriteCocktailsDbHelper.getWritableDatabase();
        int deletedRecords;
        switch(sUriMatcher.match(uri)){
            case COCKTAIL:
                deletedRecords = favouriteCocktailsDb.delete(
                        CocktailRecord.FAVOURITE_COCKTAILS_TABLE_NAME, selection, selectionArgs);
                // reset sequence
                favouriteCocktailsDb.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                        + CocktailRecord.FAVOURITE_COCKTAILS_TABLE_NAME + "'");
                break;
            case COCKTAIL_WITH_ID:
                deletedRecords = favouriteCocktailsDb.delete(CocktailRecord.FAVOURITE_COCKTAILS_TABLE_NAME,
                        CocktailRecord.ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return deletedRecords;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase favouriteCocktailsDb = favouriteCocktailsDbHelper.getWritableDatabase();

        return 0;
    }
}
