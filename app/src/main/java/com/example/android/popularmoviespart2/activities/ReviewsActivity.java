package com.example.android.popularmoviespart2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.example.android.popularmoviespart2.Constants;
import com.example.android.popularmoviespart2.R;
import com.example.android.popularmoviespart2.adapters.ReviewsAdapter;
import com.example.android.popularmoviespart2.dao.MoviesAccessFactory;
import com.example.android.popularmoviespart2.dao.MoviesAccessService;
import com.example.android.popularmoviespart2.domain.Movie;
import com.example.android.popularmoviespart2.domain.Review;
import com.example.android.popularmoviespart2.listeners.HttpResponseListener;
import com.example.android.popularmoviespart2.listeners.MoviesRecyclerViewScrollListener;
import com.example.android.popularmoviespart2.moviedb.MovieDbHttpResponse;
import com.example.android.popularmoviespart2.utils.NetworkUtils;
import com.example.android.popularmoviespart2.utils.ViewUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by angelov on 6/19/2018.
 */

public class ReviewsActivity extends AppCompatActivity implements HttpResponseListener<MovieDbHttpResponse<Review>> {

    @BindView(R.id.movie_details_review_layout)
    LinearLayout mMainMovieDetailLayout;
    @BindView(R.id.pb_reviews_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.rv_videos)
    RecyclerView mReviewsRv;
    private MoviesAccessService moviesAccessService;
    private MoviesRecyclerViewScrollListener onScrollListener;
    private int totalPages = 1;
    private int movieId;
    private ReviewsAdapter mReviewsAdapter;
    private static final String TAG = ReviewsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_reviews_layout);
        ButterKnife.bind(this);
        movieId = readMovieIdFromIntent();
        moviesAccessService = MoviesAccessFactory.getMoviesService(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mReviewsRv.setLayoutManager(linearLayoutManager);

        onScrollListener = new MoviesRecyclerViewScrollListener(linearLayoutManager){

            @Override
            protected void loadMoreItems(int currentPage) {
                if(currentPage < totalPages) {
                    loadReviews(movieId, currentPage);
                }
            }
        };

        mReviewsRv.addOnScrollListener(onScrollListener);
    }

    private Integer readMovieIdFromIntent() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(Constants.MOVIE_DETAIL_INTENT_TAG)) {
            Movie movie = intent.getExtras().getParcelable(Constants.MOVIE_DETAIL_INTENT_TAG);
            ActionBar toolbar = getSupportActionBar();
            if (toolbar != null) {
                toolbar.setTitle(movie.getTitle());
                toolbar.setDisplayHomeAsUpEnabled(true);
            }
            return movie.getId();
        }
        return null;
    }

    private void showLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void loadReviews(final int movieId, final int currentPage) {
        if(NetworkUtils.isConnectionAvailable(this)) {
            showLoadingIndicator();
            moviesAccessService.getMovieReviews(this, movieId, currentPage);
        }
        else {
            ViewUtils.showNoInternetConnectionSnackBar(mMainMovieDetailLayout, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadReviews(movieId, currentPage);
                }
            });
        }
    }

    private void initAdapter() {
        mReviewsAdapter = new ReviewsAdapter(new ArrayList<Review>());
        mReviewsRv.setAdapter(mReviewsAdapter);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "Can't load reviews: ", error);
    }

    @Override
    public void onResponse(MovieDbHttpResponse<Review> response) {
        hideLoadingIndicator();

        if(response == null || CollectionUtils.isEmpty(response.getResults())) {
            return;
        }

        totalPages = response.getTotalPages();

        mReviewsAdapter.addReviews(response.getResults());
        mReviewsAdapter.notifyDataSetChanged();
    }
}
