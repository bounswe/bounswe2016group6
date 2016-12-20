package com.group6boun451.learner.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.group6boun451.learner.R;
import com.group6boun451.learner.activity.Task;
import com.group6boun451.learner.model.GenericResponse;
import com.group6boun451.learner.model.Topic;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

/**
 * Helper class for loading images and getting readable dates.
 */
public class GlideHelper {

    private GlideHelper() {}

    /**
     * Loads the header image of the topic to given ImageView in given context.
     * @param context
     * @param image
     * @param topic
     */
    public static void loadImage(Context context, ImageView image, Topic topic) {
        loadImage(image,context.getString(R.string.base_url)+topic.getHeaderImage());
    }

    /**
     * Loads the image in given path to given ImageView.
     * @param image
     * @param path
     */
    public static void loadImage(ImageView image, String path) {
        Glide.with(image.getContext().getApplicationContext())
                .load(path)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);
    }

   /**
     * Loads the image in given path to given ImageView.
     * @param image
     * @param path
     */
    public static void loadImage(final Button image, String path) {

        Glide.with(image.getContext().getApplicationContext())
                .load(path).asBitmap()
                .into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    image.setBackground(drawable);
                    image.setText("");
                }
            }
        });


    }

    /**
     * Convert given date to a readable format. For exmample, yesterday at 7:00 pm.
     * @param c
     * @param revealDate
     * @return
     */
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

    /**
     * Shows snackbar with given text.
     * @param a
     * @param resultString
     * @return
     */
    public static boolean showResult(Activity a, String resultString) {
        GenericResponse result = Task.getResult(resultString,GenericResponse.class);
        if(result==null) {
            return false;
        }else if (result.getError() == null) {// display a notification to the user with the response information
            Snackbar.make(a.findViewById(android.R.id.content),  result.getMessage(), Snackbar.LENGTH_SHORT).show();
            return true;
        } else {
            Snackbar.make(a.findViewById(android.R.id.content),  result.getError(), Snackbar.LENGTH_SHORT).show();
            return false;
        }
    }
}
