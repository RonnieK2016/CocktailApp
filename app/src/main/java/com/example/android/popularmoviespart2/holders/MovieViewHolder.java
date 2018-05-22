package com.example.android.popularmoviespart2.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.android.popularmoviespart2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by angelov on 5/3/2018.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.movie_poster)
    public ImageView moviePoster;

    public MovieViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
