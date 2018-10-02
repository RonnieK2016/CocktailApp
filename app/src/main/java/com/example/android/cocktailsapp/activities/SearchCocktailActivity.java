package com.example.android.cocktailsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.example.android.cocktailsapp.Constants;
import com.example.android.cocktailsapp.R;
import com.example.android.cocktailsapp.adapters.ReviewsAdapter;
import com.example.android.cocktailsapp.dao.CocktailsAccessFactory;
import com.example.android.cocktailsapp.dao.CocktailsAccessService;
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.domain.Review;
import com.example.android.cocktailsapp.listeners.HttpResponseListener;
import com.example.android.cocktailsapp.listeners.CocktailsRecyclerViewScrollListener;
import com.example.android.cocktailsapp.cocktailsdb.CocktailDbHttpResponse;
import com.example.android.cocktailsapp.utils.NetworkUtils;
import com.example.android.cocktailsapp.utils.ViewUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by angelov on 6/19/2018.
 */

public class SearchCocktailActivity extends AppCompatActivity implements HttpResponseListener<CocktailDbHttpResponse<Cocktail>> {

    @BindView(R.id.movie_details_review_layout)
    RelativeLayout mMainMovieDetailLayout;
    @BindView(R.id.pb_reviews_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.rv_reviews)
    RecyclerView mReviewsRv;
    private CocktailsAccessService cocktailsAccessService;
    private CocktailsRecyclerViewScrollListener onScrollListener;
    private int totalPages = 1;
    private int movieId;
    private ReviewsAdapter mReviewsAdapter;
    private static final String TAG = SearchCocktailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_reviews_layout);
        ButterKnife.bind(this);
        movieId = readMovieIdFromIntent();
        cocktailsAccessService = CocktailsAccessFactory.getCocktailsService(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mReviewsRv.setLayoutManager(linearLayoutManager);

        initAdapter();

        onScrollListener = new CocktailsRecyclerViewScrollListener(linearLayoutManager){

            @Override
            protected void loadMoreItems(int currentPage) {
                if(currentPage < totalPages) {
                    loadReviews(movieId, currentPage);
                }
            }
        };

        mReviewsRv.addOnScrollListener(onScrollListener);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadReviews(movieId, 1);
    }

    private Integer readMovieIdFromIntent() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(Constants.COCKTAIL_DETAIL_INTENT_TAG)) {
            Cocktail cocktail = intent.getExtras().getParcelable(Constants.COCKTAIL_DETAIL_INTENT_TAG);
            ActionBar toolbar = getSupportActionBar();
            if (toolbar != null) {
                toolbar.setTitle(cocktail.getCocktailName());
                toolbar.setDisplayHomeAsUpEnabled(true);
            }
            return cocktail.getId();
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
            cocktailsAccessService.searchCocktailByIngredient(this, "", currentPage);
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
        mReviewsAdapter = new ReviewsAdapter(new ArrayList<Cocktail>());
        mReviewsRv.setAdapter(mReviewsAdapter);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "Can't load reviews: ", error);
    }

    @Override
    public void onResponse(CocktailDbHttpResponse<Cocktail> response) {
        hideLoadingIndicator();

        if(response == null || CollectionUtils.isEmpty(response.getCocktails())) {
            return;
        }

        mReviewsAdapter.addReviews(response.getCocktails());
        mReviewsAdapter.notifyDataSetChanged();
    }
}
