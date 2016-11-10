package com.group6boun451.learner.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.group6boun451.learner.App;

/**
 * Created by Dmytro Denysenko on 5/6/15.
 */
public class CanaroTextView extends TextView {
    public CanaroTextView(Context context) {
        this(context, null);
    }

    public CanaroTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanaroTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(App.canaroExtraBold);
    }

}
