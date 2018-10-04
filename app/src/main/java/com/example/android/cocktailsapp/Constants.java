package com.example.android.cocktailsapp;

/**
 * Created by angelov on 5/3/2018.
 */

public interface Constants {
    String COCKTAIL_DETAIL_INTENT_TAG = "COCKTAIL_DETAIL_INTENT_TAG";
    String COCKTAIL_INGREDIENT_INTENT_TAG = "COCKTAIL_INGREDIENT_INTENT_TAG";
    String COCKTAIL_DB_BASE_PATH = "https://www.thecocktaildb.com/api/json/v1/" + BuildConfig.COCKTAILS_DB_API_KEY + "/";
    String COCKTAIL_DB_SEARCH_BY_ALCOHOL_PATH = "/filter.php?a=";
    String COCKTAIL_DB_SEARCH_BY_INGREDIENT_PATH = "/filter.php?i=";
    String COCKTAIL_DB_LOAD_COCKTAIL_PATH = "/lookup.php?i=";
    String COCKTAIL_DB_ALCOHOLIC_KEYWORD = "Alcoholic";
    String COCKTAIL_DB_NON_ALCOHOLIC_KEYWORD = "Non_Alcoholic";
}
