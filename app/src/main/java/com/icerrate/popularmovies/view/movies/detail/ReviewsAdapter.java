package com.icerrate.popularmovies.view.movies.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Review;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private ArrayList<Review> items;

    private OnButtonClickListener onButtonClickListener;

    public ReviewsAdapter(OnButtonClickListener onButtonClickListener) {
        this(new ArrayList<Review>(), onButtonClickListener);
    }

    public ReviewsAdapter(ArrayList<Review> items, OnButtonClickListener onButtonClickListener) {
        this.items = items;
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view, onButtonClickListener);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder viewHolder, int position) {
        Review review = items.get(position);

        viewHolder.authorTextView.setText(review.getAuthor());
        viewHolder.contentTextView.setText(review.getContent());
        viewHolder.visitButton.setTag(review);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<Review> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView authorTextView;

        public TextView contentTextView;

        public Button visitButton;

        private OnButtonClickListener onButtonClickListener;

        public ReviewViewHolder(View itemView, OnButtonClickListener onButtonClickListener) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.author);
            contentTextView = itemView.findViewById(R.id.content);
            visitButton = itemView.findViewById(R.id.visit);
            this.onButtonClickListener = onButtonClickListener;
            visitButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onButtonClickListener != null) {
                onButtonClickListener.onPlayButtonClick(view);
            }
        }
    }

    public interface OnButtonClickListener {

        void onPlayButtonClick(View view);
    }
}