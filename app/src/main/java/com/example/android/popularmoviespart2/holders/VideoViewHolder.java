package com.example.android.popularmoviespart2.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviespart2.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by angelov on 6/18/2018.
 */

public class VideoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.movie_video_poster)
    public ImageView movieVideoPoster;
    @BindView(R.id.movie_video_title)
    public TextView movieVideoTitle;

    public VideoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
