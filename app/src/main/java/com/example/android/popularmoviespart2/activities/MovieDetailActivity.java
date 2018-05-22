package com.example.android.popularmoviespart2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmoviespart2.Constants;
import com.example.android.popularmoviespart2.R;
import com.example.android.popularmoviespart2.domain.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        ButterKnife.bind(this);
        Movie movie = readMovieFromIntent();
        populateDataToViews(movie);
    }

    private void populateDataToViews(Movie movie) {
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

}
