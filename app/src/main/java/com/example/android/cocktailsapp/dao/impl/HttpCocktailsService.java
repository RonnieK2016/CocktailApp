package com.example.android.cocktailsapp.dao.impl;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.android.cocktailsapp.Constants;
import com.example.android.cocktailsapp.dao.CocktailsAccessService;
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.listeners.HttpResponseListener;
import com.example.android.cocktailsapp.cocktailsdb.CocktailDbHttpRequest;
import com.example.android.cocktailsapp.cocktailsdb.CocktailDbHttpResponse;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * Created by angelov on 5/1/2018.
 */

public class HttpCocktailsService implements CocktailsAccessService {

    private final static String TAG = CocktailsAccessService.class.getSimpleName();
    private Context mContext;
    private RequestQueue mRequestQueue;

    private HttpCocktailsService(){ }

    public HttpCocktailsService(Context context) {
        this.mContext = context;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    private CocktailDbHttpRequest buildRequest(int method, String searchPath, String searchBy,
                                               HttpResponseListener<CocktailDbHttpResponse<Cocktail>> listener, int currentPage) {
        StringBuilder url = new StringBuilder(Constants.COCKTAIL_DB_BASE_PATH);
        url.append(searchPath).append(searchBy);
        return new CocktailDbHttpRequest(method, url.toString(), new HashMap<String, String>(), listener);
    }

    @Override
    public void getAlcoholicCocktails(HttpResponseListener<CocktailDbHttpResponse<Cocktail>> listener, int currentPage) {
        addToRequestQueue(buildRequest(Request.Method.GET, Constants.COCKTAIL_DB_SEARCH_BY_ALCOHOL_PATH,
                Constants.COCKTAIL_DB_ALCOHOLIC_KEYWORD, listener, currentPage));
    }

    @Override
    public void getNonAlcoholicCocktails(HttpResponseListener<CocktailDbHttpResponse<Cocktail>> listener, int currentPage) {
        addToRequestQueue(buildRequest(Request.Method.GET, Constants.COCKTAIL_DB_SEARCH_BY_ALCOHOL_PATH,
                Constants.COCKTAIL_DB_NON_ALCOHOLIC_KEYWORD, listener, currentPage));
    }

    @Override
    public void searchCocktailByIngredient(HttpResponseListener<CocktailDbHttpResponse<Cocktail>> listener,
                                           String ingregient, int currentPage) {
        addToRequestQueue(buildRequest(Request.Method.GET, Constants.COCKTAIL_DB_SEARCH_BY_INGREDIENT_PATH,
                ingregient, listener, currentPage));
    }

    @Override
    public void loadCocktailDetails(HttpResponseListener<CocktailDbHttpResponse<Cocktail>> listener,
                                    int cocktailId, int currentPage) {
        addToRequestQueue(buildRequest(Request.Method.GET, Constants.COCKTAIL_DB_LOAD_COCKTAIL_PATH,
                String.valueOf(cocktailId), listener, currentPage));
    }
}
