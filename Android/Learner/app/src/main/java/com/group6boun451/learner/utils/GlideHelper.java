package com.group6boun451.learner.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.group6boun451.learner.R;
import com.group6boun451.learner.model.Topic;

public class GlideHelper {

    private GlideHelper() {}

    public static void loadImage(Context context, ImageView image, Topic topic) {
        loadImage(image,context.getString(R.string.base_url)+topic.getHeaderImage());
    }
    public static void loadImage(ImageView image, String path) {
        Glide.with(image.getContext().getApplicationContext())
                .load(path)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);
    }

}
