package com.example.android.cocktailsapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.cocktailsapp.R;
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.holders.CocktailViewHolder;
import com.example.android.cocktailsapp.listeners.MovieAdapterCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelov on 5/3/2018.
 */

public class CocktailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MovieAdapterCallback<Cocktail> mCallbacks;
    private Context mContext;
    private ArrayList<Cocktail> mCocktails;

    public CocktailsAdapter(ArrayList<Cocktail> cocktails) {
        this.mCocktails = cocktails;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.cocktail_grid_item_layout, null);
        return new CocktailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof CocktailViewHolder) {
            final Cocktail selectedCocktail = mCocktails.get(position);

            final CocktailViewHolder cocktailViewHolder = (CocktailViewHolder) holder;

            Picasso.with(mContext)
                    .load(selectedCocktail.getImageUrl())
                    .placeholder(R.drawable.ic_cocktail_placeholder)
                    .into(cocktailViewHolder.cocktailImage);

            cocktailViewHolder.cocktailName.setText(selectedCocktail.getCocktailName());

            cocktailViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCallbacks!=null) {
                        mCallbacks.onClick(selectedCocktail);
                    }
                }
            });
        }

    }

    public void addCocktails(List<Cocktail> cocktails) {
        mCocktails.addAll(cocktails);
    }

    public void clearMovies() {
        mCocktails.clear();
    }

    @Override
    public int getItemCount() {
        return (mCocktails !=null? mCocktails.size():0);
    }

    public void setCallbacks(MovieAdapterCallback<Cocktail> callbacks) {
        this.mCallbacks = callbacks;
    }

    public ArrayList<Cocktail> getMovies() {
        return mCocktails;
    }

}