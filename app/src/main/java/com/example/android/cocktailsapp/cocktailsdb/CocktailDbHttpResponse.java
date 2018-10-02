package com.example.android.cocktailsapp.cocktailsdb;

import com.example.android.cocktailsapp.JsonConstants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by angelov on 5/6/2018.
 */

public class CocktailDbHttpResponse<T> {
    @Getter
    @Setter
    @SerializedName(JsonConstants.COCKTAILS_SERVICE_JSON_DRINKS_FIELD)
    private List<T> cocktails;
}
