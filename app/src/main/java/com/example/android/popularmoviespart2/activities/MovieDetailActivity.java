package com.example.android.popularmoviespart2.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.android.popularmoviespart2.Constants;
import com.example.android.popularmoviespart2.R;
import com.example.android.popularmoviespart2.adapters.VideosAdapter;
import com.example.android.popularmoviespart2.dao.MoviesAccessFactory;
import com.example.android.popularmoviespart2.dao.MoviesAccessService;
import com.example.android.popularmoviespart2.domain.Movie;
import com.example.android.popularmoviespart2.domain.SortOptions;
import com.example.android.popularmoviespart2.domain.Video;
import com.example.android.popularmoviespart2.listeners.HttpResponseListener;
import com.example.android.popularmoviespart2.listeners.MovieAdapterCallback;
import com.example.android.popularmoviespart2.moviedb.MovieDbHttpResponse;
import com.example.android.popularmoviespart2.utils.NetworkUtils;
import com.example.android.popularmoviespart2.utils.ViewUtils;
import com.squareup.picasso.Picasso;

import org.apache.commons.collections4.CollectionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements MovieAdapterCallback<Video>, HttpResponseListener<MovieDbHttpResponse<Video>> {

    @BindView(R.id.movie_title)
    TextView movieTitle;
    @BindView(R.id.movie_poster_details)
    ImageView moviePosterBig;
    @BindView(R.id.movie_release_date)
    TextView releaseDate;
    @BindView(R.id.movie_overview)
    TextView overview;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.movie_rating)
    TextView movieRating;
    @BindView(R.id.pb_video_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.main_movie_detail_layout)
    LinearLayout mMainMovieDetailLayout;
    private static final String TAG = MoviesListActivity.class.getSimpleName();
    private VideosAdapter mVideosAdapter;
    @BindView(R.id.rv_videos)
    RecyclerView mVideosRecyclerView;
    private MoviesAccessService moviesAccessService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        ButterKnife.bind(this);
        Movie movie = readMovieFromIntent();
        moviesAccessService = MoviesAccessFactory.getMoviesService(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mVideosRecyclerView.setLayoutManager(linearLayoutManager);

        populateDataToViews(movie);
    }

    private void populateDataToViews(Movie movie) {
        loadVideos(movie.getId());
        movieTitle.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        movieRating.setText(String.valueOf(movie.getVoteAverage()));
        ratingBar.setRating((float) movie.getVoteAverage());
        overview.setText(movie.getOverview());
        moviePosterBig.setContentDescription(movie.getTitle());
        Picasso.with(this)
                .load(Constants.MOVIE_DB_IMAGES_BASE_PATH + movie.getPosterPath())
                .into(moviePosterBig);
    }

    private Movie readMovieFromIntent() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(Constants.MOVIE_DETAIL_INTENT_TAG)) {
            Movie movie = intent.getExtras().getParcelable(Constants.MOVIE_DETAIL_INTENT_TAG);
            ActionBar toolbar = getSupportActionBar();
            if (toolbar != null) {
                toolbar.setTitle(movie.getTitle());
                toolbar.setDisplayHomeAsUpEnabled(true);
            }
            return  movie;
        }
        return null;
    }

    private void showLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void loadVideos(final int movieId) {
        if(NetworkUtils.isConnectionAvailable(this)) {
            showLoadingIndicator();
            moviesAccessService.getMovieRelatedVideos(this, movieId, 1);
        }
        else {
            ViewUtils.showNoInternetConnectionSnackBar(mMainMovieDetailLayout, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadVideos(movieId);
                }
            });
        }
    }

    @Override
    public void onClick(Video item) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_VIDEO_APPLICATION_PATH + item.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(Constants.YOUTUBE_VIDEO_WATCH_BASE_PATH + item.getKey()));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "Can't load movies: ", error);
    }

    @Override
    public void onResponse(MovieDbHttpResponse<Video> response) {
        hideLoadingIndicator();

        if(response == null || CollectionUtils.isEmpty(response.getResults())) {
            return;
        }

        mVideosAdapter  = new VideosAdapter(response.getResults());
        mVideosAdapter.setCallbacks(this);
        mVideosRecyclerView.setAdapter(mVideosAdapter);
    }
}
