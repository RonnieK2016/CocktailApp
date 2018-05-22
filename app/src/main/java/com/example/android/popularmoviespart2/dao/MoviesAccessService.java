package com.example.android.popularmoviespart2.dao;

import com.example.android.popularmoviespart2.listeners.HttpResponseListener;

/**
 * Created by angelov on 5/1/2018.
 */

public interface MoviesAccessService {

    void getPopularMovies(HttpResponseListener listener, int currentPage);

    void getTopRatedMovies(HttpResponseListener listener, int currentPage);

}
