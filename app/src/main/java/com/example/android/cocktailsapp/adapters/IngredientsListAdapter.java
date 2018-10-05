package com.example.android.cocktailsapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.cocktailsapp.R;
import com.example.android.cocktailsapp.domain.Ingredient;
import com.example.android.cocktailsapp.holders.IngredientsViewHolder;
import com.example.android.cocktailsapp.listeners.CocktailAdapterCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelov on 10/4/2018.
 */

public class IngredientsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Ingredient> mIngredients;
    Context mContext;
    CocktailAdapterCallback<Ingredient> mCallback;

    public IngredientsListAdapter(ArrayList<Ingredient> ingredients) {
        this.mIngredients = ingredients;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.ingredient_list_item, null);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof IngredientsViewHolder) {
            final Ingredient selectedIngredient = mIngredients.get(position);

            final IngredientsViewHolder ingredientsViewHolder = (IngredientsViewHolder) holder;

            ingredientsViewHolder.ingredientName.setText(selectedIngredient.getIngredientName());
            ingredientsViewHolder.ingredientMeasurement.setText(selectedIngredient.getMeasurement());

            ingredientsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCallback != null) {
                        mCallback.onClick(selectedIngredient);
                    }
                }
            });
        }

    }

    public void addIngredients(List<Ingredient> ingredients) {
        mIngredients.addAll(ingredients);
    }

    public void clearIngredients() {
        mIngredients.clear();
    }

    @Override
    public int getItemCount() {
        return (mIngredients !=null ? mIngredients.size() : 0);
    }

    public void setCallbacks(CocktailAdapterCallback<Ingredient> callbacks) {
        this.mCallback = callbacks;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

}