package com.example.android.popularmoviespart2.utils;

import android.content.ContentValues;

import com.example.android.popularmoviespart2.dataproviders.FavouriteMoviesDbContract;
import com.example.android.popularmoviespart2.dataproviders.FavouriteMoviesDbContract.MovieRecord;
import com.example.android.popularmoviespart2.domain.Movie;

/**
 * Created by angelov on 6/26/2018.
 */

public class ConverterUtils {

    public static ContentValues movieToContentValues(Movie movie) {
        ContentValues values = new ContentValues();

        values.put(MovieRecord.DB_ID, movie.getId());
        values.put(MovieRecord.TITLE, movie.getTitle());
        values.put(MovieRecord.POSTER_PATH, movie.getPosterPath());
        values.put(MovieRecord.VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MovieRecord.OVERVIEW, movie.getOverview());
        values.put(MovieRecord.MOVIE_RELEASE_DATE, movie.getReleaseDate());

        return values;
    }
}
