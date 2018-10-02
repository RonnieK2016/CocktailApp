package com.example.android.cocktailsapp.listeners;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by angelov on 5/8/2018.
 */
public abstract class CocktailsRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    @Getter
    @Setter
    private int currentPage = 1;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isLoading = false;
    @Getter
    @Setter
    private int previousTotal = 0;
    private int numberOfVisibleItems = 5;

    public CocktailsRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public CocktailsRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        numberOfVisibleItems = numberOfVisibleItems * layoutManager.getSpanCount();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        int firstVisibleItemPosition = 1;

        if(layoutManager instanceof GridLayoutManager) {
            firstVisibleItemPosition = ((GridLayoutManager)layoutManager).findFirstVisibleItemPosition();
        }
        else if (layoutManager instanceof LinearLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
        }

        if (isLoading) {
            if (totalItemCount > previousTotal) {
                isLoading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!isLoading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItemPosition + numberOfVisibleItems)) {
            loadMoreItems(++currentPage);
            isLoading = true;
        }

    }

    public void resetState() {
        isLoading = false;
        previousTotal = 0;
        currentPage = 1;
    }

    protected abstract void loadMoreItems(int currentPage);
}

