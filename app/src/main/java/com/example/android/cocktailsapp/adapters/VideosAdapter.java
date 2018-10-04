package com.example.android.cocktailsapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.cocktailsapp.R;
import com.example.android.cocktailsapp.domain.Video;
import com.example.android.cocktailsapp.holders.VideoViewHolder;
import com.example.android.cocktailsapp.listeners.CocktailAdapterCallback;

import java.util.List;

/**
 * Created by angelov on 5/3/2018.
 */

public class VideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private CocktailAdapterCallback<Video> mCallbacks;
    private Context mContext;
    private List<Video> mVideos;

    public VideosAdapter(List<Video> videos) {
        this.mVideos = videos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.movie_video_layout, null);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof VideoViewHolder) {
            final Video selectedVideo = mVideos.get(position);

            final VideoViewHolder videoViewHolder = (VideoViewHolder) holder;

            videoViewHolder.movieVideoTitle.setText(selectedVideo.getName());

            videoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mCallbacks!=null) {
                        mCallbacks.onClick(selectedVideo);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return (mVideos !=null? mVideos.size():0);
    }

    public void setCallbacks(CocktailAdapterCallback<Video> callbacks) {
        this.mCallbacks = callbacks;
    }

}