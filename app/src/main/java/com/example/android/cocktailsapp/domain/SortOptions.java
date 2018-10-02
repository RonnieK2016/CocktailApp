package com.example.android.cocktailsapp.domain;

/**
 * Created by angelov on 5/3/2018.
 */

public enum SortOptions {
    ALCOHOLIC("alcoholic"),
    NON_ALCOHOLIC("non_alcoholic"),
    FAVOURITE("favourite");

    private final String value;

    SortOptions(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return value;
    }

}
