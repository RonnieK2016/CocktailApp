package com.example.android.popularmoviespart2;

/**
 * Created by angelov on 5/3/2018.
 */

public interface Constants {
    String MOVIE_DB_IMAGES_BASE_PATH = "http://image.tmdb.org/t/p/w185";
    String MOVIE_DETAIL_INTENT_TAG = "MOVIE_INTENT_TAG";
    String MOVIE_DB_BASE_PATH = "http://api.themoviedb.org/3/movie/";
    String MOVIE_DB_POPULAR_PATH = MOVIE_DB_BASE_PATH + "popular";
    String MOVIE_DB_TOP_RATED_PATH = MOVIE_DB_BASE_PATH + "top_rated";
    String MOVIE_ACCESS_SERVICE_API_KEY = "api_key";
    String MOVIE_ACCESS_SERVICE_PAGE = "page";
}
