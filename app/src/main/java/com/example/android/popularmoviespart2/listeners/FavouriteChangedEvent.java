package com.example.android.popularmoviespart2.listeners;

/**
 * Created by angelov on 6/30/2018.
 */

public class FavouriteChangedEvent {

    public final boolean favouriteChanged;

    public FavouriteChangedEvent(boolean favouriteChanged) {
        this.favouriteChanged = favouriteChanged;
    }

    public boolean isFavouriteChanged(){
        return  favouriteChanged;
    }
}
