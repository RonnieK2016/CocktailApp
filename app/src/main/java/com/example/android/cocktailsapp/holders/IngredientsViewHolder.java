package com.example.android.cocktailsapp.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.cocktailsapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by angelov on 6/23/2018.
 */

public class IngredientsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ingredient_name)
    public TextView ingredientName;
    @BindView(R.id.ingredient_measurement)
    public TextView ingredientMeasurement;

    public IngredientsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
