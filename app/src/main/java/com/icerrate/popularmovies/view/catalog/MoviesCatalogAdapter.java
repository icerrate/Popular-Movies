package com.icerrate.popularmovies.view.catalog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ivan Cerrate
 */
public class MoviesCatalogAdapter extends RecyclerView.Adapter<MoviesCatalogAdapter.ViewHolder> {

    private ArrayList<Movie> items;
    private OnItemClickListener onItemClickListener;

    public MoviesCatalogAdapter(OnItemClickListener onItemClickListener) {
        this(new ArrayList<Movie>(), onItemClickListener);
    }

    public MoviesCatalogAdapter(ArrayList<Movie> items, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movies_catalog, parent, false);
        layoutView.getLayoutParams().height = (int) (parent.getWidth() / 2 *
                1.5f);
        return new ViewHolder(layoutView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Movie movie = items.get(position);
        String url = movie.getPosterUrl("w342");

        Glide.with(context).load(url).placeholder(context.getResources().getDrawable(R.drawable.movie_placeholder)).into(holder.posterImageView);

        holder.posterImageView.setTag(holder.posterImageView.getId(), movie);
    }

    public void addItems(ArrayList<Movie> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void resetItems() {
        this.items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.poster) public ImageView posterImageView;
        private OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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