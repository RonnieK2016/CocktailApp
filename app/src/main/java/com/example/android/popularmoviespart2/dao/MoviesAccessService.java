package com.example.android.popularmoviespart2.dao;

import com.example.android.popularmoviespart2.domain.Movie;
import com.example.android.popularmoviespart2.domain.Review;
import com.example.android.popularmoviespart2.domain.Video;
import com.example.android.popularmoviespart2.listeners.HttpResponseListener;
import com.example.android.popularmoviespart2.moviedb.MovieDbHttpResponse;

/**
 * Created by angelov on 5/1/2018.
 */

public interface MoviesAccessService {

    void getPopularMovies(HttpResponseListener<MovieDbHttpResponse<Movie>> listener, int currentPage);

    void getTopRatedMovies(HttpResponseListener<MovieDbHttpResponse<Movie>> listener, int currentPage);

    void getMovieReviews(HttpResponseListener<MovieDbHttpResponse<Review>> listener, int movieId, int currentPage);

    void getMovieRelatedVideos(HttpResponseListener<MovieDbHttpResponse<Video>> listener, int movieId, int currentPage);
}
