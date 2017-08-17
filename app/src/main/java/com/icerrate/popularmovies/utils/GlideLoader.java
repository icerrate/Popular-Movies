package com.icerrate.popularmovies.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by icerrate
 */
public class GlideLoader {

    interface Callback {
        void onSuccess();
        void onFailure();
    }

    public void loadFromUrl(String url, ImageView imageView, Drawable placeholder, Callback callback) {
        Context context = imageView.getContext();
        load(Glide.with(context).load(url), placeholder, callback).into(imageView);
    }

    private <T> DrawableTypeRequest<T> load(
            DrawableTypeRequest<T> load,
            Drawable placeholder,
            final Callback callback) {
        load.centerCrop();
        load.placeholder(placeholder);
        if (callback != null) {
            load.listener(new RequestListener<T, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, T model, Target<GlideDrawable> target, boolean isFirstResource) {
                    callback.onFailure();
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, T model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    callback.onSuccess();
                    return false;
                }
            });
        }
        return load;
    }

}
