package com.group6boun451.learner.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.group6boun451.learner.model.Topic;

public class GlideHelper {

    private GlideHelper() {}

    public static void loadImage(ImageView image, Topic topic) {
        Glide.with(image.getContext().getApplicationContext())
                .load(topic.getHeaderImage())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);
    }

}
