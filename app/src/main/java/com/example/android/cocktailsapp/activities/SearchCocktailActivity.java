package com.example.android.cocktailsapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.android.cocktailsapp.AnalyticsApplication;
import com.example.android.cocktailsapp.Constants;
import com.example.android.cocktailsapp.R;
import com.example.android.cocktailsapp.adapters.CocktailsAdapter;
import com.example.android.cocktailsapp.dao.CocktailsAccessFactory;
import com.example.android.cocktailsapp.dao.CocktailsAccessService;
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.listeners.HttpResponseListener;
import com.example.android.cocktailsapp.cocktailsdb.CocktailDbHttpResponse;
import com.example.android.cocktailsapp.utils.NetworkUtils;
import com.example.android.cocktailsapp.utils.ViewUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by angelov on 6/19/2018.
 */

public class SearchCocktailActivity extends AppCompatActivity implements HttpResponseListener<CocktailDbHttpResponse<Cocktail>> {

    private CocktailsAdapter mCocktailsAdapter;
    @BindView(R.id.rv_cocktails)
    RecyclerView mCocktailsListRv;
    @BindView(R.id.no_favourite_cocktails)
    TextView mNoFavouriteCocktailsTextView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.activity_cocktails_list)
    RelativeLayout mMainLayout;
    @BindView(R.id.main_app_toolbar)
    Toolbar mToolBar;
    @BindView(R.id.app_bar_title)
    TextView mToolBarTitle;
    private CocktailsAccessService cocktailsAccessService;
    private String ingredient;
    private static final String TAG = SearchCocktailActivity.class.getSimpleName();
    private static final int NUMBER_OF_COLUMNS = 2;
    private static final int NUMBER_OF_COLUMNS_LANDSCAPE = 3;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        setContentView(R.layout.activity_cocktails_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled (true);

        int columnsNumber = NUMBER_OF_COLUMNS;
        if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columnsNumber = NUMBER_OF_COLUMNS_LANDSCAPE;
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columnsNumber);
        mCocktailsListRv.setLayoutManager(gridLayoutManager);
        readIngredientFromIntent();
        initAdapter();
        cocktailsAccessService = CocktailsAccessFactory.getCocktailsService(this);
        mNoFavouriteCocktailsTextView.setText(getResources().getString(R.string.no_cocktails_found));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!StringUtils.isEmpty(ingredient)) {
            loadSearchResults(ingredient, 1);
        }
        else {
            showNoResultsFound(true);
        }
    }

    private void readIngredientFromIntent() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(Constants.COCKTAIL_INGREDIENT_INTENT_TAG)) {
            ingredient = intent.getExtras().getString(Constants.COCKTAIL_INGREDIENT_INTENT_TAG);
            mToolBarTitle.setText(getResources().getString(R.string.search_results) + ingredient);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTracker.setScreenName("Search Results: " + ingredient);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void showLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void loadSearchResults(final String ingredient, final int currentPage) {
        if(NetworkUtils.isConnectionAvailable(this)) {
            showLoadingIndicator();
            cocktailsAccessService.searchCocktailByIngredient(this, ingredient, currentPage);
        }
        else {
            ViewUtils.showNoInternetConnectionSnackBar(mMainLayout, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadSearchResults(ingredient, currentPage);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initAdapter() {
        mCocktailsAdapter = new CocktailsAdapter(new ArrayList<Cocktail>());
        mCocktailsListRv.setAdapter(mCocktailsAdapter);
    }

    private void showNoResultsFound(boolean show) {
        mNoFavouriteCocktailsTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        mCocktailsListRv.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "Can't load reviews: ", error);
    }

    @Override
    public void onResponse(CocktailDbHttpResponse<Cocktail> response) {
        hideLoadingIndicator();

        if(response == null || CollectionUtils.isEmpty(response.getCocktails())) {
            showNoResultsFound(true);
            return;
        }

        showNoResultsFound(false);

        mCocktailsAdapter.addCocktails(response.getCocktails());
        mCocktailsAdapter.notifyDataSetChanged();
    }
}
