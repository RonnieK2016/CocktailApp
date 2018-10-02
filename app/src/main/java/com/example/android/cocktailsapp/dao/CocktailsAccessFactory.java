package com.example.android.cocktailsapp.dao;

import android.content.Context;

import com.example.android.cocktailsapp.dao.impl.HttpCocktailsService;

/**
 * Created by angelov on 5/1/2018.
 */

public class CocktailsAccessFactory {

    public static CocktailsAccessService getCocktailsService(Context context) {
        return new HttpCocktailsService(context);
    }
}
