package com.example.android.popularmoviespart2.dao.impl;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.android.popularmoviespart2.BuildConfig;
import com.example.android.popularmoviespart2.Constants;
import com.example.android.popularmoviespart2.dao.MoviesAccessService;
import com.example.android.popularmoviespart2.domain.Movie;
import com.example.android.popularmoviespart2.domain.Review;
import com.example.android.popularmoviespart2.domain.Video;
import com.example.android.popularmoviespart2.listeners.HttpResponseListener;
import com.example.android.popularmoviespart2.moviedb.MovieDbHttpRequest;
import com.example.android.popularmoviespart2.moviedb.MovieDbHttpResponse;
import com.google.gson.reflect.TypeToken;

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

    private <T> MovieDbHttpRequest buildRequest(int method, String url, HttpResponseListener<T> listener, int currentPage,
                                                TypeToken<T> typeToken) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.MOVIE_ACCESS_SERVICE_PAGE, String.valueOf(currentPage));
        params.put(Constants.MOVIE_ACCESS_SERVICE_API_KEY, BuildConfig.MOVIE_DB_API_KEY);
        return new MovieDbHttpRequest<T>(method, url, params, listener, typeToken);
    }

    @Override
    public void getPopularMovies(HttpResponseListener<MovieDbHttpResponse<Movie>> listener, int currentPage) {
        TypeToken<MovieDbHttpResponse<Movie>> movieTypeToken = new TypeToken<MovieDbHttpResponse<Movie>>(){};
        addToRequestQueue(buildRequest(Request.Method.POST,
                Constants.MOVIE_DB_POPULAR_PATH, listener, currentPage, movieTypeToken));
    }

    @Override
    public void getTopRatedMovies(HttpResponseListener<MovieDbHttpResponse<Movie>> listener, int currentPage) {
        TypeToken<MovieDbHttpResponse<Movie>> movieTypeToken = new TypeToken<MovieDbHttpResponse<Movie>>(){};
        addToRequestQueue(buildRequest(Request.Method.POST,
                Constants.MOVIE_DB_TOP_RATED_PATH, listener, currentPage, movieTypeToken));
    }

    @Override
    public void getMovieReviews(HttpResponseListener<MovieDbHttpResponse<Review>> listener, int movieId, int currentPage) {
        TypeToken<MovieDbHttpResponse<Review>> reviewTypeToken = new TypeToken<MovieDbHttpResponse<Review>>(){};
        StringBuilder url = new StringBuilder(Constants.MOVIE_DB_BASE_PATH);
        url.append(movieId);
        url.append(Constants.MOVIE_DB_REVIEWS_PATH);
        addToRequestQueue(buildRequest(Request.Method.POST, url.toString(), listener, currentPage, reviewTypeToken));
    }

    @Override
    public void getMovieRelatedVideos(HttpResponseListener<MovieDbHttpResponse<Video>> listener, int movieId, int currentPage) {
        TypeToken<MovieDbHttpResponse<Video>> videoTypeToken = new TypeToken<MovieDbHttpResponse<Video>>(){};
        StringBuilder url = new StringBuilder(Constants.MOVIE_DB_BASE_PATH);
        url.append(movieId).append(Constants.MOVIE_DB_VIDEOS_PATH).append("?").
                append(Constants.MOVIE_ACCESS_SERVICE_API_KEY).append("=").append(BuildConfig.MOVIE_DB_API_KEY);
        addToRequestQueue(this.<MovieDbHttpResponse<Video>>buildRequest(Request.Method.GET, url.toString(),
                listener, currentPage, videoTypeToken));
    }
}
