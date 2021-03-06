package com.icerrate.popularmovies.view.movies.detail;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.view.common.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Cerrate.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private List<Trailer> items;

    private OnItemClickListener onItemClickListener;

    public TrailersAdapter(OnItemClickListener onItemClickListener) {
        this(new ArrayList<Trailer>(), onItemClickListener);
    }

    public TrailersAdapter(List<Trailer> items, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.onItemClickListener = onItemClickListener;
    }

    @Override public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false);
        return new TrailerViewHolder(view, onItemClickListener);
    }

    @Override public void onBindViewHolder(TrailerViewHolder viewHolder, int position) {
        Trailer trailer = items.get(position);

        Context context = viewHolder.itemView.getContext();
        String url = trailer.getVideoThumbnail();
        GlideApp.with(context)
                .load(url)
                .placeholder(context.getResources().getDrawable(R.drawable.backdrop_placeholder))
                .into(viewHolder.thumbnailImageView);
        viewHolder.itemView.setTag(trailer);
        viewHolder.playImageButton.setTag(trailer);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public void addItems(List<Trailer> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public AppCompatImageView thumbnailImageView;

        public AppCompatImageButton playImageButton;

        private OnItemClickListener onItemClickListener;

        public TrailerViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnail);
            playImageButton = itemView.findViewById(R.id.play);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
            playImageButton.setOnClickListener(this);
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