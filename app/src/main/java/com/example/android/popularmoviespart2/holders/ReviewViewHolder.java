package com.example.android.popularmoviespart2.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmoviespart2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by angelov on 6/23/2018.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.movie_review_author)
    public TextView reviewAuthor;
    @BindView(R.id.movie_review_description)
    public TextView reviewDescription;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
