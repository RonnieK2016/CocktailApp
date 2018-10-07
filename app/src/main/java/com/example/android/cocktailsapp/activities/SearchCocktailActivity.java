package com.example.android.cocktailsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.listeners.HttpResponseListener;
import com.example.android.cocktailsapp.cocktailsdb.CocktailDbHttpResponse;
import com.example.android.cocktailsapp.utils.NetworkUtils;
import com.example.android.cocktailsapp.utils.ViewUtils;

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
    public RecyclerView mCocktailsListRv;
    @BindView(R.id.no_favourite_cocktails)
    TextView mNoFavouriteCocktailsTextView;
    @BindView(R.id.pb_loading_indicator)
    public ProgressBar mLoadingIndicator;
    @BindView(R.id.activity_cocktails_list)
    public RelativeLayout mMainLayout;
    private CocktailsAccessService cocktailsAccessService;
    private String ingredient;
    private static final String TAG = SearchCocktailActivity.class.getSimpleName();
    private static final int NUMBER_OF_COLUMNS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cocktails_list);
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
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

            ActionBar toolbar = getSupportActionBar();
            if (toolbar != null) {
                toolbar.setTitle("Search Results: " + ingredient);
                toolbar.setDisplayHomeAsUpEnabled(true);
            }

        }
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
