package com.example.android.popularmoviespart2.dao.impl;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.android.popularmoviespart2.BuildConfig;
import com.example.android.popularmoviespart2.Constants;
import com.example.android.popularmoviespart2.dao.MoviesAccessService;
import com.example.android.popularmoviespart2.listeners.HttpResponseListener;
import com.example.android.popularmoviespart2.moviedb.MovieDbHttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by angelov on 5/1/2018.
 */

public class HttpMoviesService implements MoviesAccessService {

    private final static String TAG = MoviesAccessService.class.getSimpleName();
    private Context mContext;
    private RequestQueue mRequestQueue;

    private HttpMoviesService(){ }

    public HttpMoviesService(Context context) {
        this.mContext = context;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    private MovieDbHttpRequest buildRequest(int method, String url, HttpResponseListener listener, int currentPage) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.MOVIE_ACCESS_SERVICE_PAGE, String.valueOf(currentPage));
        params.put(Constants.MOVIE_ACCESS_SERVICE_API_KEY, BuildConfig.MOVIE_DB_API_KEY);
        return new MovieDbHttpRequest(method, url, params, listener);
    }

    public void getPopularMovies(HttpResponseListener listener, int currentPage) {
        addToRequestQueue(buildRequest(Request.Method.POST, Constants.MOVIE_DB_POPULAR_PATH, listener, currentPage));
    }

    @Override
    public void getTopRatedMovies(HttpResponseListener listener, int currentPage) {
        addToRequestQueue(buildRequest(Request.Method.POST, Constants.MOVIE_DB_TOP_RATED_PATH, listener, currentPage));
    }
}
