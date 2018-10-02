package com.example.android.cocktailsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.cocktailsapp.R;
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.domain.Review;
import com.example.android.cocktailsapp.holders.ReviewViewHolder;

import java.util.List;

/**
 * Created by angelov on 6/23/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Cocktail> mCocktails;

    public ReviewsAdapter(List<Cocktail> cocktails) {
        this.mCocktails = cocktails;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.review_item_layout, null);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ReviewViewHolder) {
            final Cocktail selectedReview = mCocktails.get(position);

            final ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;

        }
    }

    public void addReviews(List<Cocktail> reviews) {
        mCocktails.addAll(reviews);
    }

    public void clearReviews() {
        mCocktails.clear();
    }

    @Override
    public int getItemCount() {
        return (mCocktails !=null? mCocktails.size():0);
    }
}
