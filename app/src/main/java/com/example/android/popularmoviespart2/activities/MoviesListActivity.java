package com.example.android.popularmoviespart2.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.android.popularmoviespart2.Constants;
import com.example.android.popularmoviespart2.R;
import com.example.android.popularmoviespart2.adapters.MoviesAdapter;
import com.example.android.popularmoviespart2.dao.MoviesAccessFactory;
import com.example.android.popularmoviespart2.dao.MoviesAccessService;
import com.example.android.popularmoviespart2.domain.Movie;
import com.example.android.popularmoviespart2.domain.SortOptions;
import com.example.android.popularmoviespart2.listeners.HttpResponseListener;
import com.example.android.popularmoviespart2.listeners.MovieAdapterCallback;
import com.example.android.popularmoviespart2.listeners.MoviesRecyclerViewScrollListener;
import com.example.android.popularmoviespart2.moviedb.MovieDbHttpResponse;
import com.example.android.popularmoviespart2.utils.NetworkUtils;
import com.example.android.popularmoviespart2.utils.ViewUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesListActivity extends AppCompatActivity implements HttpResponseListener<MovieDbHttpResponse<Movie>>,
        MovieAdapterCallback<Movie> {

    private MoviesAdapter mMoviesAdapter;
    @BindView(R.id.rv_movies)
    public RecyclerView mMoviesListRv;
    @BindView(R.id.pb_loading_indicator)
    public ProgressBar mLoadingIndicator;
    @BindView(R.id.activity_movies_list)
    public RelativeLayout mMainLayout;
    private static final int NUMBER_OF_COLUMNS = 2;
    private static final String TAG = MoviesListActivity.class.getSimpleName();
    private MoviesAccessService moviesAccessService;
    private SortOptions mSelectedSort = SortOptions.POPULAR;
    private int totalPages = 1;
    private boolean sortOptionChanged = false;
    private static final String SELECTED_SEARCH_TAG = "SELECTED_SEARCH_TAG";
    private MoviesRecyclerViewScrollListener onScrollListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mMoviesListRv.setLayoutManager(gridLayoutManager);

        onScrollListener = new MoviesRecyclerViewScrollListener(gridLayoutManager){

            @Override
            protected void loadMoreItems(int currentPage) {
                if(currentPage < totalPages) {
                    loadMovies(mSelectedSort, currentPage);
                }
            }
        };

        mMoviesListRv.addOnScrollListener(onScrollListener);
        initAdapter();
        moviesAccessService = MoviesAccessFactory.getMoviesService(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadMovies(mSelectedSort, 1);
    }

    private void initAdapter() {
        mMoviesAdapter = new MoviesAdapter(new ArrayList<Movie>());
        mMoviesAdapter.setCallbacks(this);
        mMoviesListRv.setAdapter(mMoviesAdapter);
    }

    private void loadMovies(final SortOptions sort, int currentPage) {
        if(NetworkUtils.isConnectionAvailable(this)) {
            showLoadingIndicator();
            if(sort == SortOptions.POPULAR) {
                moviesAccessService.getPopularMovies(this, currentPage);
            } else {
                moviesAccessService.getTopRatedMovies(this, currentPage);
            }
        }
        else {
            ViewUtils.showNoInternetConnectionSnackBar(mMainLayout, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadMovies(sort, 1);
                }
            });
        }
    }

    private void showLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Constants.MOVIE_DETAIL_INTENT_TAG, movie);
        startActivity(intent);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "Can't load movies: ", error);
    }

    @Override
    public void onResponse(MovieDbHttpResponse<Movie> response) {
        hideLoadingIndicator();

        if(response == null || CollectionUtils.isEmpty(response.getResults())) {
            return;
        }

        totalPages = response.getTotalPages();
        if (sortOptionChanged) {
            sortOptionChanged = false;
            mMoviesAdapter.clearMovies();
        }
        mMoviesAdapter.addMovies(response.getResults());
        mMoviesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_options_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (mSelectedSort) {
            case TOP_RATED:
                menu.findItem(R.id.sort_by_top_rated).setChecked(true);
                break;
            case POPULAR:
                menu.findItem(R.id.sort_by_popularity).setChecked(true);
        }
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if(!item.isChecked()) {
            totalPages = 1;

            item.setChecked(true);
            sortOptionChanged = true;
            onScrollListener.resetState();
            switch (item.getItemId()) {
                case R.id.sort_by_popularity:
                    sortMoviesBySelection(SortOptions.POPULAR);
                    break;
                case R.id.sort_by_top_rated:
                    sortMoviesBySelection(SortOptions.TOP_RATED);
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortMoviesBySelection(SortOptions selectedSort) {
        mSelectedSort = selectedSort;
        loadMovies(selectedSort, 1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SELECTED_SEARCH_TAG, mSelectedSort);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Object savedSortOption = savedInstanceState.getSerializable(SELECTED_SEARCH_TAG);
        if(savedSortOption != null) {
            mSelectedSort = (SortOptions) savedSortOption;
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
