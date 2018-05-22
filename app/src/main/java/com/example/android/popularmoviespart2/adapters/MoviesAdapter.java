package com.example.android.popularmoviespart2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmoviespart2.Constants;
import com.example.android.popularmoviespart2.R;
import com.example.android.popularmoviespart2.domain.Movie;
import com.example.android.popularmoviespart2.holders.MovieViewHolder;
import com.example.android.popularmoviespart2.listeners.MovieAdapterCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by angelov on 5/3/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MovieAdapterCallback mCallbacks;
    private Context mContext;
    private List<Movie> mMovies;

    public MoviesAdapter(List<Movie> movies) {
        this.mMovies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.movie_poster_layout, null);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof MovieViewHolder) {
            final Movie selectedMovie = mMovies.get(position);

            final MovieViewHolder movieViewHolder = (MovieViewHolder) holder;

            Picasso.with(mContext)
                    .load(Constants.MOVIE_DB_IMAGES_BASE_PATH + selectedMovie.getPosterPath())
                    .placeholder(R.drawable.ic_posterplaceholder)
                    .into(movieViewHolder.moviePoster);

            movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mCallbacks!=null) {
                        mCallbacks.onMovieClick(selectedMovie);
                    }
                }
            });
        }

    }

    public void addMovies(List<Movie> movies) {
        mMovies.addAll(movies);
    }

    public void clearMovies() {
        mMovies.clear();
    }

    @Override
    public int getItemCount() {
        return (mMovies !=null? mMovies.size():0);
    }

    public void setCallbacks(MovieAdapterCallback callbacks) {
        this.mCallbacks = callbacks;
    }

}