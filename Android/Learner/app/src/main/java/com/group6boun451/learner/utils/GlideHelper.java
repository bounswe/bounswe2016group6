package com.group6boun451.learner.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class GlideHelper {

    private GlideHelper() {}

    public static void loadImage(ImageView image, com.group6boun451.learner.Topic topic) {
        Glide.with(image.getContext().getApplicationContext())
                .load(topic.getImage())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);
    }

}
