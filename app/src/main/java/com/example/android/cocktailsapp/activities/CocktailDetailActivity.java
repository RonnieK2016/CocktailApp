package com.example.android.cocktailsapp.activities;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.android.cocktailsapp.Constants;
import com.example.android.cocktailsapp.R;
import com.example.android.cocktailsapp.adapters.VideosAdapter;
import com.example.android.cocktailsapp.dao.CocktailsAccessFactory;
import com.example.android.cocktailsapp.dao.CocktailsAccessService;
import com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract;
import com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract.CocktailRecord;
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.domain.Video;
import com.example.android.cocktailsapp.listeners.FavouriteChangedEvent;
import com.example.android.cocktailsapp.listeners.HttpResponseListener;
import com.example.android.cocktailsapp.listeners.MovieAdapterCallback;
import com.example.android.cocktailsapp.cocktailsdb.CocktailDbHttpResponse;
import com.example.android.cocktailsapp.utils.ConverterUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CocktailDetailActivity extends AppCompatActivity implements MovieAdapterCallback<Video>,
        HttpResponseListener<CocktailDbHttpResponse<Video>>{

    public static final String SAVED_MOVIE_TAG = "SAVED_MOVIE_TAG";
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
    private static final String TAG = CocktailListActivity.class.getSimpleName();
    private VideosAdapter mVideosAdapter;
    @BindView(R.id.rv_videos)
    RecyclerView mVideosRecyclerView;
    private CocktailsAccessService cocktailsAccessService;
    private Cocktail cocktail;
    @BindView(R.id.like_button)
    FloatingActionButton mLikeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        ButterKnife.bind(this);
        cocktail = readMovieFromIntent();
        cocktailsAccessService = CocktailsAccessFactory.getCocktailsService(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mVideosRecyclerView.setLayoutManager(linearLayoutManager);

        if(!cocktail.isFavourite()) {
            Cursor result = getContentResolver().query(CocktailRecord.CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(cocktail.getId())).build(),
                    FavouriteCocktailsDbContract.GET_COCKTAIL_BY_ID_COLUMNS,
                    null,
                    null, null);

            if(result.getCount() > 0 && result.moveToFirst()) {
                cocktail.setDatabaseId(result.getInt(result.getColumnIndex(CocktailRecord.ID)));
            }
        }

        if(cocktail.isFavourite()) {
            mLikeButton.setImageResource(R.drawable.ic_like_clicked);
        }

        populateDataToViews(cocktail);
    }

    private void populateDataToViews(Cocktail cocktail) {
        movieTitle.setText(cocktail.getCocktailName());
        /*
        releaseDate.setText(cocktail.getReleaseDate());
        movieRating.setText(String.valueOf(cocktail.getVoteAverage()));
        ratingBar.setRating((float) cocktail.getVoteAverage());
        overview.setText(cocktail.getOverview());
        moviePosterBig.setContentDescription(cocktail.getCocktailName());
        Picasso.with(this)
                .load(cocktail.getPosterPath())
                .into(moviePosterBig);
                */
    }

    private Cocktail readMovieFromIntent() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(Constants.COCKTAIL_DETAIL_INTENT_TAG)) {
            Cocktail cocktail = intent.getExtras().getParcelable(Constants.COCKTAIL_DETAIL_INTENT_TAG);
            ActionBar toolbar = getSupportActionBar();
            if (toolbar != null) {
                toolbar.setTitle(cocktail.getCocktailName());
                toolbar.setDisplayHomeAsUpEnabled(true);
            }
            return cocktail;
        }
        return null;
    }

    private void showLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public void openReviewsButton(View item) {
        Intent intent = new Intent(this, SearchCocktailActivity.class);
        intent.putExtra(Constants.COCKTAIL_DETAIL_INTENT_TAG, cocktail);
        startActivity(intent);
    }

    @Override
    public void onClick(Video item) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "Can't load movies: ", error);
    }

    @Override
    public void onResponse(CocktailDbHttpResponse<Video> response) {
        hideLoadingIndicator();

        if(response == null || CollectionUtils.isEmpty(response.getCocktails())) {
            return;
        }

        mVideosAdapter  = new VideosAdapter(response.getCocktails());
        mVideosAdapter.setCallbacks(this);
        mVideosRecyclerView.setAdapter(mVideosAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_MOVIE_TAG, cocktail);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Object savedMovie = savedInstanceState.getParcelable(SAVED_MOVIE_TAG);
        if(savedMovie != null) {
            cocktail = (Cocktail) savedMovie;
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @OnClick(R.id.like_button)
    public void onLikeButtonClicked(View v) {
        if(cocktail.isFavourite()) {
            getContentResolver().delete(CocktailRecord.CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(cocktail.getDatabaseId())).build(), null, null);
            mLikeButton.setImageResource(R.drawable.ic_like_grey);
            cocktail.setDatabaseId(0);
            Toast.makeText(CocktailDetailActivity.this, getResources().getString(R.string.removed_from_favourites), Toast.LENGTH_SHORT).show();
        }
        else {
            Uri returnUri = getContentResolver().insert(CocktailRecord.CONTENT_URI, ConverterUtils.movieToContentValues(cocktail));
            long insertedId = ContentUris.parseId(returnUri);
            cocktail.setDatabaseId((int)insertedId);
            mLikeButton.setImageResource(R.drawable.ic_like_clicked);
            Toast.makeText(CocktailDetailActivity.this, getResources().getString(R.string.added_to_favourites), Toast.LENGTH_SHORT).show();
        }

        EventBus.getDefault().post(new FavouriteChangedEvent(true));
    }
}
