package com.group6boun451.learner.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.group6boun451.learner.R;
import com.group6boun451.learner.model.Topic;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

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

    public static String getReadableDateFromDate(Context c,Date revealDate) {
        //DATE Android DateUtils
        String s = DateUtils.getRelativeDateTimeString(c,revealDate.getTime(),
                DateUtils.DAY_IN_MILLIS,DateUtils.WEEK_IN_MILLIS, 0).toString();

        String[] datetime = s.split(",");
        String date = datetime[0];

        //TIME JodaTime DateUtils
        Calendar cc = Calendar.getInstance();
        cc.set(Calendar.HOUR_OF_DAY,revealDate.getHours());
        cc.set(Calendar.MINUTE, revealDate.getMinutes());
        DateTime dt = new DateTime(cc.getTimeInMillis());
        String time = net.danlew.android.joda.DateUtils.getRelativeTimeSpanString(c, dt, true).toString();
        return date+ ", " + time;
    }

}
