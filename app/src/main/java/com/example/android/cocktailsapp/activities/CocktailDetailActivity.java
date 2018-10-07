package com.example.android.cocktailsapp.activities;

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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.android.cocktailsapp.AnalyticsApplication;
import com.example.android.cocktailsapp.Constants;
import com.example.android.cocktailsapp.R;
import com.example.android.cocktailsapp.adapters.IngredientsListAdapter;
import com.example.android.cocktailsapp.dao.CocktailsAccessFactory;
import com.example.android.cocktailsapp.dao.CocktailsAccessService;
import com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract;
import com.example.android.cocktailsapp.dataproviders.FavouriteCocktailsDbContract.CocktailRecord;
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.domain.Ingredient;
import com.example.android.cocktailsapp.listeners.FavouriteChangedEvent;
import com.example.android.cocktailsapp.listeners.HttpResponseListener;
import com.example.android.cocktailsapp.listeners.CocktailAdapterCallback;
import com.example.android.cocktailsapp.cocktailsdb.CocktailDbHttpResponse;
import com.example.android.cocktailsapp.utils.ConverterUtils;
import com.example.android.cocktailsapp.utils.NetworkUtils;
import com.example.android.cocktailsapp.utils.ViewUtils;
import com.example.android.cocktailsapp.widget.CocktailWidgetService;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import org.apache.commons.collections4.CollectionUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CocktailDetailActivity extends AppCompatActivity implements CocktailAdapterCallback<Ingredient>,
        HttpResponseListener<CocktailDbHttpResponse<Cocktail>>{

    public static final String SAVED_COCKTAIL_TAG = "SAVED_COCKTAIL_TAG";
    @BindView(R.id.cocktail_name)
    TextView cocktailNameTextView;
    @BindView(R.id.cocktail_image_details)
    ImageView cocktailImageBig;
    @BindView(R.id.ingredients_list)
    RecyclerView ingredientsRv;
    @BindView(R.id.instructions)
    TextView instructions;
    @BindView(R.id.pb_loading_indicator_details)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.main_cocktail_detail_layout)
    LinearLayout mMainCocktailDetailLayout;
    @BindView(R.id.main_content_layout)
    LinearLayout mMainContentLayout;
    @BindView(R.id.main_app_toolbar)
    Toolbar mToolBar;
    @BindView(R.id.app_bar_title)
    TextView mToolBarTitle;
    private static final String TAG = CocktailListActivity.class.getSimpleName();
    private CocktailsAccessService cocktailsAccessService;
    private Cocktail cocktail;
    @BindView(R.id.like_button)
    FloatingActionButton mLikeButton;
    private IngredientsListAdapter mIngredientsListAdapter;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cocktail_details);
        ButterKnife.bind(this);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        cocktail = readCocktailFromIntent();
        cocktailsAccessService = CocktailsAccessFactory.getCocktailsService(this);

        mIngredientsListAdapter = new IngredientsListAdapter(new ArrayList<Ingredient>());
        mIngredientsListAdapter.setCallbacks(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ingredientsRv.setLayoutManager(linearLayoutManager);
        ingredientsRv.setAdapter(mIngredientsListAdapter);

        setSupportActionBar(mToolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled (true);

        showLoadingIndicator();

        if(!cocktail.isFavourite()) {
            Cursor result = getContentResolver().query(CocktailRecord.CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(cocktail.getId())).build(),
                    FavouriteCocktailsDbContract.FAVOURITE_COCKTAILS_COLUMNS,
                    null,
                    null, null);

            //if found in local database then just use data from it
            if(result.getCount() > 0 && result.moveToFirst()) {
                cocktail = ConverterUtils.readCursorAsCocktail(result);
                populateDataToViews(cocktail);
            }
            //if not found in data, then load data from internet database
            else {
                loadCocktail(cocktail.getId());
            }
        }
        else {
            populateDataToViews(cocktail);
        }

        if(cocktail.isFavourite()) {
            mLikeButton.setImageResource(R.drawable.ic_like_clicked);
        }
    }

    private void populateDataToViews(Cocktail cocktail) {
        hideLoadingIndicator();

        cocktailNameTextView.setText(cocktail.getCocktailName());
        Picasso.with(this)
                .load(cocktail.getImageUrl())
                .placeholder(R.drawable.ic_cocktail_placeholder)
                .into(cocktailImageBig);

        cocktailImageBig.setContentDescription(cocktail.getCocktailName());
        instructions.setText(cocktail.getInstructions());

        mIngredientsListAdapter.addIngredients(cocktail.getIngredients());
        mIngredientsListAdapter.notifyDataSetChanged();

    }

    private Cocktail readCocktailFromIntent() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(Constants.COCKTAIL_DETAIL_INTENT_TAG)) {
            Cocktail cocktail = intent.getExtras().getParcelable(Constants.COCKTAIL_DETAIL_INTENT_TAG);
            mToolBarTitle.setText(cocktail.getCocktailName());
            return cocktail;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTracker.setScreenName(cocktail.getCocktailName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void showLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mMainContentLayout.setVisibility(View.GONE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMainContentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Ingredient item) {
        Intent intent = new Intent(this, SearchCocktailActivity.class);
        intent.putExtra(Constants.COCKTAIL_INGREDIENT_INTENT_TAG, item.getIngredientName());
        startActivity(intent);
    }

    private void loadCocktail(final int cocktailId) {
        if (NetworkUtils.isConnectionAvailable(this)) {
            cocktailsAccessService.loadCocktailDetails(this, cocktailId,0);
        } else {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            ViewUtils.showNoInternetConnectionSnackBar(mMainCocktailDetailLayout, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadCocktail(cocktailId);
                }
            });
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "Can't load cocktail: ", error);
    }

    @Override
    public void onResponse(CocktailDbHttpResponse<Cocktail> response) {

        if(response == null || CollectionUtils.isEmpty(response.getCocktails())) {
            return;
        }

        cocktail = response.getCocktails().get(0);

        populateDataToViews(cocktail);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_COCKTAIL_TAG, cocktail);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Object savedCocktail = savedInstanceState.getParcelable(SAVED_COCKTAIL_TAG);
        if(savedCocktail != null) {
            cocktail = (Cocktail) savedCocktail;
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
            Uri returnUri = getContentResolver().insert(CocktailRecord.CONTENT_URI, ConverterUtils.cocktailToContentValues(cocktail));
            long insertedId = ContentUris.parseId(returnUri);
            cocktail.setDatabaseId((int)insertedId);
            mLikeButton.setImageResource(R.drawable.ic_like_clicked);
            Toast.makeText(CocktailDetailActivity.this, getResources().getString(R.string.added_to_favourites), Toast.LENGTH_SHORT).show();
        }

        CocktailWidgetService.startActionUpdateCocktailWidgets(this);

        EventBus.getDefault().post(new FavouriteChangedEvent(true));
    }

    @OnClick(R.id.share_fab)
    public void shareButtonClicked(View v) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, cocktail.getCocktailName());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, ConverterUtils.cocktailToString(cocktail, this));
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_cocktail_via)));
    }
}
