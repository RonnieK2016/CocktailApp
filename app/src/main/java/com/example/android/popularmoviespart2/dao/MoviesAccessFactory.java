package com.example.android.popularmoviespart2.dao;

import android.content.Context;

import com.example.android.popularmoviespart2.dao.impl.HttpMoviesService;

/**
 * Created by angelov on 5/1/2018.
 */

public class MoviesAccessFactory {

    public static MoviesAccessService getMoviesService(Context context) {
        return new HttpMoviesService(context);
    }
}
