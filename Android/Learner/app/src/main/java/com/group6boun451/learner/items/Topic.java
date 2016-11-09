package com.group6boun451.learner.items;

import android.content.res.Resources;
import android.content.res.TypedArray;

import com.group6boun451.learner.R;

public class Topic {

    private final int imageId;
    private final String title;
    private final String year;
    private final String location;

    private Topic(int imageId, String title, String year, String location) {
        this.imageId = imageId;
        this.title = title;
        this.year = year;
        this.location = location;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getLocation() {
        return location;
    }

    public static Topic[] getAllTopics(Resources res) {
        String[] titles = res.getStringArray(R.array.paintings_titles);
        String[] years = res.getStringArray(R.array.paintings_years);
        String[] locations = res.getStringArray(R.array.paintings_locations);
        TypedArray images = res.obtainTypedArray(R.array.paintings_images);

        int size = titles.length;
        Topic[] topics = new Topic[size];

        for (int i = 0; i < size; i++) {
            final int imageId = images.getResourceId(i, -1);
            topics[i] = new Topic(imageId, titles[i], years[i], locations[i]);
        }

        images.recycle();

        return topics;
    }

}
