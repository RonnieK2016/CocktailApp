package com.example.android.popularmoviespart2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmoviespart2.Constants;
import com.example.android.popularmoviespart2.R;
import com.example.android.popularmoviespart2.domain.Video;
import com.example.android.popularmoviespart2.holders.VideoViewHolder;
import com.example.android.popularmoviespart2.listeners.MovieAdapterCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by angelov on 5/3/2018.
 */

public class VideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MovieAdapterCallback<Video> mCallbacks;
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

            Picasso.with(mContext)
                    .load(Constants.YOUTUBE_VIDEO_IMAGE_BASE_PATH + selectedVideo.getKey() + Constants.YOUTUBE_VIDEO_IMAGE_URL_END_PATH)
                    .placeholder(R.drawable.ic_posterplaceholder)
                    .into(videoViewHolder.movieVideoPoster);
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

    public void setCallbacks(MovieAdapterCallback<Video> callbacks) {
        this.mCallbacks = callbacks;
    }

}