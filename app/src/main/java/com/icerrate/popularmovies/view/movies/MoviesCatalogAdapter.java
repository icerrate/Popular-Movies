package com.icerrate.popularmovies.view.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.view.common.GlideApp;
import com.icerrate.popularmovies.view.common.LoadMoreBaseAdapter;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public class MoviesCatalogAdapter extends LoadMoreBaseAdapter<Movie> {

    private OnItemClickListener onItemClickListener;

    private int columns;

    public MoviesCatalogAdapter(OnItemClickListener onItemClickListener, int columns) {
        this(new ArrayList<Movie>(), onItemClickListener, columns);
    }

    public MoviesCatalogAdapter(ArrayList<Movie> movies, OnItemClickListener onItemClickListener, int columns) {
        this.data = movies;
        this.onItemClickListener = onItemClickListener;
        this.columns = columns;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies_catalog, parent, false);
        layoutView.getLayoutParams().height = (int) (parent.getWidth() / columns *
                1.5f);
        return new MovieViewHolder(layoutView, onItemClickListener);
    }

    @Override
    public void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            Movie movie = data.get(position);
            Context context = holder.itemView.getContext();
            String url = movie.getPosterUrl("w500");

            GlideApp.with(context)
                    .load(url)
                    .placeholder(context.getResources().getDrawable(R.drawable.poster_placeholder))
                    .into(movieViewHolder.posterImageView);

            movieViewHolder.itemView.setTag(movie);
        }
    }

    public void addItems(ArrayList<Movie> items) {
        this.data.addAll(items);
        notifyDataSetChanged();
    }

    public void resetItems() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView posterImageView;

        private OnItemClickListener onItemClickListener;

        public MovieViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.poster);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view);
            }
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view);
    }
}