package com.group6boun451.learner.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.group6boun451.learner.model.Topic;

public class GlideHelper {

    private GlideHelper() {}

    public static void loadImage(ImageView image, Topic topic) {
        loadImage(image,"http://192.168.5.87:8080/"+topic.getHeaderImage());
    }
    public static void loadImage(ImageView image, String path) {
        Glide.with(image.getContext().getApplicationContext())
                .load(path)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);
    }

    public static void loadImage(ImageView image, String url) {
        Glide.with(image.getContext().getApplicationContext())
                .load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);
    }

}
