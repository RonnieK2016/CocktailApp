package com.example.android.cocktailsapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.example.android.cocktailsapp.Constants;
import com.example.android.cocktailsapp.R;
import com.example.android.cocktailsapp.adapters.CocktailsAdapter;
import com.example.android.cocktailsapp.dao.CocktailsAccessFactory;
import com.example.android.cocktailsapp.dao.CocktailsAccessService;
import com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract;
import com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract.CocktailRecord;
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.domain.SortOptions;
import com.example.android.cocktailsapp.listeners.FavouriteChangedEvent;
import com.example.android.cocktailsapp.listeners.HttpResponseListener;
import com.example.android.cocktailsapp.listeners.MovieAdapterCallback;
import com.example.android.cocktailsapp.listeners.CocktailsRecyclerViewScrollListener;
import com.example.android.cocktailsapp.cocktailsdb.CocktailDbHttpResponse;
import com.example.android.cocktailsapp.utils.NetworkUtils;
import com.example.android.cocktailsapp.utils.ViewUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CocktailListActivity extends AppCompatActivity implements HttpResponseListener<CocktailDbHttpResponse<Cocktail>>,
        MovieAdapterCallback<Cocktail>, LoaderManager.LoaderCallbacks<Cursor>  {

    private CocktailsAdapter mCocktailsAdapter;
    @BindView(R.id.rv_movies)
    public RecyclerView mMoviesListRv;
    @BindView(R.id.no_favourite_movies)
    TextView mNoFavouriteMoviesTextView;
    @BindView(R.id.pb_loading_indicator)
    public ProgressBar mLoadingIndicator;
    @BindView(R.id.activity_movies_list)
    public RelativeLayout mMainLayout;
    private static final int NUMBER_OF_COLUMNS = 2;
    private static final String TAG = CocktailListActivity.class.getSimpleName();
    private CocktailsAccessService cocktailsAccessService;
    private SortOptions mSelectedSort = SortOptions.ALCOHOLIC;
    private int totalPages = 1;
    private boolean sortOptionChanged = false;
    private static final String SEARCH_TAG = "SEARCH_TAG";
    private static final String COCKTAILS_LIST_TAG = "COCKTAILS_LIST_TAG";
    private static final String TOTAL_PAGES_TAG = "TOTAL_PAGES_TAG";
    private static final String CURRENT_PAGE_TAG = "CURRENT_PAGE_TAG";
    private static final String COCKTAIL_SCROLL_LISTENER_TOTAL_TAG = "COCKTAIL_SCROLL_LISTENER_TOTAL_TAG";
    private CocktailsRecyclerViewScrollListener onScrollListener;
    public static final int FAVOURITE_COCKTAILS_LOADER_ID = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mMoviesListRv.setLayoutManager(gridLayoutManager);

        onScrollListener = new CocktailsRecyclerViewScrollListener(gridLayoutManager){

            @Override
            protected void loadMoreItems(int currentPage) {
                if(currentPage < totalPages) {
                    loadMovies(mSelectedSort, currentPage);
                }
            }
        };

        mMoviesListRv.addOnScrollListener(onScrollListener);
        initAdapter();

        if(savedInstanceState != null) {

        }

        cocktailsAccessService = CocktailsAccessFactory.getCocktailsService(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mSelectedSort = (SortOptions) savedInstanceState.getSerializable(SEARCH_TAG);
            if(mSelectedSort != SortOptions.FAVOURITE) {
                totalPages = (int) savedInstanceState.getSerializable(TOTAL_PAGES_TAG);
                onScrollListener.setCurrentPage((Integer) savedInstanceState.getSerializable(CURRENT_PAGE_TAG));
                onScrollListener.setPreviousTotal((Integer) savedInstanceState.getSerializable(COCKTAIL_SCROLL_LISTENER_TOTAL_TAG));
            }
            mCocktailsAdapter.addMovies(savedInstanceState.<Cocktail>getParcelableArrayList(COCKTAILS_LIST_TAG));
        }
        else {
            loadMovies(mSelectedSort, 1);
        }
    }

    private void initAdapter() {
        mCocktailsAdapter = new CocktailsAdapter(new ArrayList<Cocktail>());
        mCocktailsAdapter.setCallbacks(this);
        mMoviesListRv.setAdapter(mCocktailsAdapter);
    }

    private void loadMovies(final SortOptions sort, int currentPage) {
        if(SortOptions.FAVOURITE == sort){
            showLoadingIndicator();
            getSupportLoaderManager().initLoader(FAVOURITE_COCKTAILS_LOADER_ID, null, this);
        }
        else {
            getSupportLoaderManager().destroyLoader(FAVOURITE_COCKTAILS_LOADER_ID);
            if (NetworkUtils.isConnectionAvailable(this)) {
                showHideFavourites(true);
                showLoadingIndicator();
                if (sort == SortOptions.ALCOHOLIC) {
                    cocktailsAccessService.getAlcoholicCocktails(this, currentPage);
                } else {
                    cocktailsAccessService.getNonAlcoholicCocktails(this, currentPage);
                }
            } else {
                ViewUtils.showNoInternetConnectionSnackBar(mMainLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadMovies(sort, 1);
                    }
                });
            }
        }
    }

    private void showLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(Cocktail cocktail) {
        Intent intent = new Intent(this, CocktailDetailActivity.class);
        intent.putExtra(Constants.COCKTAIL_DETAIL_INTENT_TAG, cocktail);
        startActivity(intent);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "Can't load movies: ", error);
    }

    @Override
    public void onResponse(CocktailDbHttpResponse<Cocktail> response) {
        hideLoadingIndicator();

        if(response == null || CollectionUtils.isEmpty(response.getCocktails())) {
            return;
        }

        if (sortOptionChanged) {
            sortOptionChanged = false;
            mCocktailsAdapter.clearMovies();
        }
        mCocktailsAdapter.addMovies(response.getCocktails());
        mCocktailsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_options_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (mSelectedSort) {
            case NON_ALCOHOLIC:
                menu.findItem(R.id.sort_by_top_rated).setChecked(true);
                break;
            case ALCOHOLIC:
                menu.findItem(R.id.sort_by_popularity).setChecked(true);
                break;
            case FAVOURITE:
                menu.findItem(R.id.favourite_movies).setChecked(true);
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
                    sortMoviesBySelection(SortOptions.ALCOHOLIC);
                    break;
                case R.id.sort_by_top_rated:
                    sortMoviesBySelection(SortOptions.NON_ALCOHOLIC);
                    break;
                case R.id.favourite_movies:
                    sortMoviesBySelection(SortOptions.FAVOURITE);
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
        outState.putSerializable(SEARCH_TAG, mSelectedSort);
        outState.putParcelableArrayList(COCKTAILS_LIST_TAG, mCocktailsAdapter.getMovies());
        if(mSelectedSort != SortOptions.FAVOURITE) {
            outState.putSerializable(TOTAL_PAGES_TAG, totalPages);
            outState.putSerializable(CURRENT_PAGE_TAG, onScrollListener.getCurrentPage());
            outState.putSerializable(COCKTAIL_SCROLL_LISTENER_TOTAL_TAG, onScrollListener.getPreviousTotal());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {
        if (id == FAVOURITE_COCKTAILS_LOADER_ID) {
            return new CursorLoader(this,
                    CocktailRecord.CONTENT_URI,
                    FavouriteCocktailsDbContract.FAVOURITE_COCKTAILS_COLUMNS,
                    null,
                    null,
                    CocktailRecord.ID);
        }
        else {
            throw new RuntimeException("Not implemented for id " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        hideLoadingIndicator();
        sortOptionChanged = false;
        mCocktailsAdapter.clearMovies();

        showHideFavourites(data.getCount() > 0);

        if(data.getCount() > 0) {
            populateDataFromCursor(data);
        }
    }

    private void populateDataFromCursor(Cursor data) {
        List<Cocktail> resultList = new ArrayList<>();

        if(data.moveToFirst()) {
            do {
                int databaseId = data.getInt(data.getColumnIndex(CocktailRecord.ID));
                int movieId = data.getInt(data.getColumnIndex(CocktailRecord.DB_ID));
                String title = data.getString(data.getColumnIndex(CocktailRecord.TITLE));
                String posterPath = data.getString(data.getColumnIndex(CocktailRecord.POSTER_PATH));
                double voteAverage = data.getDouble(data.getColumnIndex(CocktailRecord.VOTE_AVERAGE));
                String overview = data.getString(data.getColumnIndex(CocktailRecord.OVERVIEW));
                String releaseData = data.getString(data.getColumnIndex(CocktailRecord.RELEASE_DATE));
                //resultList.add(new Cocktail(movieId, databaseId, title, posterPath, voteAverage, overview, releaseData));
            }
            while (data.moveToNext());
        }
        //mCocktailsAdapter.clearMovies();
        mCocktailsAdapter.addMovies(resultList);
        mCocktailsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCocktailsAdapter.clearMovies();
        mCocktailsAdapter.notifyDataSetChanged();
    }

    private void showHideFavourites(boolean show) {
        mMoviesListRv.setVisibility(show ? View.VISIBLE : View.GONE);
        mNoFavouriteMoviesTextView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFavouriteChangedEvent(FavouriteChangedEvent event) {
        if(SortOptions.FAVOURITE == mSelectedSort) {
            getSupportLoaderManager().restartLoader(CocktailListActivity.FAVOURITE_COCKTAILS_LOADER_ID, null, this);
        }
    }

}
