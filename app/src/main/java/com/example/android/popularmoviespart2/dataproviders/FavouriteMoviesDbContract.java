package com.example.android.popularmoviespart2.dataproviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by angelov on 6/26/2018.
 */

public class FavouriteMoviesDbContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviespart2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String[] FAVOURITE_MOVIES_COLUMNS = new String[] {
            MovieRecord.ID,
            MovieRecord.DB_ID,
            MovieRecord.TITLE,
            MovieRecord.POSTER_PATH,
            MovieRecord.VOTE_AVERAGE,
            MovieRecord.OVERVIEW,
            MovieRecord.RELEASE_DATE
    };

    public static final class MovieRecord implements BaseColumns {

        // table name
        public static final String FAVOURITE_MOVIES_TABLE_NAME = "FAVOURITE_MOVIES";

        // columns
        public static final String ID="ID";
        public static final String DB_ID = "DB_ID";
        public static final String TITLE = "TITLE";
        public static final String POSTER_PATH = "POSTER_PATH";
        public static final String VOTE_AVERAGE = "VOTE_AVERAGE";
        public static final String OVERVIEW = "OVERVIEW";
        public static final String RELEASE_DATE = "RELEASE_DATE";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FAVOURITE_MOVIES_TABLE_NAME).build();

        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FAVOURITE_MOVIES_TABLE_NAME;

        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + FAVOURITE_MOVIES_TABLE_NAME;

        public static Uri buildUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
