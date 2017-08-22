package com.icerrate.popularmovies.view.common;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icerrate.popularmovies.R;

import java.util.List;

/**
 * Created by Ivan Cerrate.
 */

public abstract class LoadMoreBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int VIEW_TYPE_LOAD = 1;

    protected static final int VIEW_TYPE_DATA = 2;

    private boolean hasLoadingFooter = false;

    protected List<T> data;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD) {
            View progressView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_space, parent, false);
            return new ProgressViewHolder(progressView);
        } else {
            return onCreateDataViewHolder(parent, viewType);
        }
    }

    public abstract RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_DATA ) {
            onBindDataViewHolder(holder, position);
        }
    }

    public abstract void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemViewType(int position) {
        return data.get(position) != null ? VIEW_TYPE_DATA : VIEW_TYPE_LOAD;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(View v) {
            super(v);
        }
    }

    public T getItem(int index) {
        if (data != null && data.get(index) != null) {
            return data.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + data);
        }
    }

    public boolean isLoading() {
        return hasLoadingFooter;
    }

    public void setLoading(boolean loading) {
        if (loading) {
            hasLoadingFooter = true;
            if (!data.contains(null)) {
                data.add(null);
                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        notifyItemInserted(data.size() - 1);
                    }
                };
                handler.post(r);
            }
        } else {
            hasLoadingFooter = false;
            data.remove(null);
            Handler handler = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    notifyItemRemoved(data.size() - 1);
                }
            };
            handler.post(r);
        }
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                notifyDataSetChanged();
            }
        };
        handler.post(r);
    }
}