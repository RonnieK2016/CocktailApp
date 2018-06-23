package com.example.android.popularmoviespart2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmoviespart2.R;
import com.example.android.popularmoviespart2.domain.Review;
import com.example.android.popularmoviespart2.holders.ReviewViewHolder;

import java.util.List;

/**
 * Created by angelov on 6/23/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Review> mReviews;

    public ReviewsAdapter(List<Review> reviews) {
        this.mReviews = reviews;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.review_item_layout, null);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ReviewViewHolder) {
            final Review selectedReview = mReviews.get(position);

            final ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;

            reviewViewHolder.reviewAuthor.setText(selectedReview.getAuthor());
            reviewViewHolder.reviewAuthor.setContentDescription(selectedReview.getAuthor());
            reviewViewHolder.reviewDescription.setText(selectedReview.getContent());
            reviewViewHolder.reviewDescription.setContentDescription(selectedReview.getContent());
        }
    }

    public void addReviews(List<Review> reviews) {
        mReviews.addAll(reviews);
    }

    public void clearReviews() {
        mReviews.clear();
    }

    @Override
    public int getItemCount() {
        return (mReviews !=null? mReviews.size():0);
    }
}
