package com.example.android.cocktailsapp.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cocktailsapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by angelov on 5/3/2018.
 */
public class CocktailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cocktail_image)
    public ImageView cocktailImage;
    @BindView(R.id.cocktail_name)
    public TextView cocktailName;

    public CocktailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
