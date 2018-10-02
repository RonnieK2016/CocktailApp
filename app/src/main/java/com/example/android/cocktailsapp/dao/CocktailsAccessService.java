package com.example.android.cocktailsapp.dao;

import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.domain.Review;
import com.example.android.cocktailsapp.listeners.HttpResponseListener;
import com.example.android.cocktailsapp.cocktailsdb.CocktailDbHttpResponse;

/**
 * Created by angelov on 5/1/2018.
 */

public interface CocktailsAccessService {

    void getAlcoholicCocktails(HttpResponseListener<CocktailDbHttpResponse<Cocktail>> listener, int currentPage);

    void getNonAlcoholicCocktails(HttpResponseListener<CocktailDbHttpResponse<Cocktail>> listener, int currentPage);

    void searchCocktailByIngredient(HttpResponseListener<CocktailDbHttpResponse<Cocktail>> listener, String ingredient, int currentPage);

    void loadCocktailDetails(HttpResponseListener<CocktailDbHttpResponse<Cocktail>> listener, int cocktailId, int currentPage);

}
