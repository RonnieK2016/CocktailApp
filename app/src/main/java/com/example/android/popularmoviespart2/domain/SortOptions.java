package com.example.android.popularmoviespart2.domain;

/**
 * Created by angelov on 5/3/2018.
 */

public enum SortOptions {
    POPULAR("popular"),
    TOP_RATED("top_rated");

    private final String value;

    SortOptions(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return value;
    }

}
