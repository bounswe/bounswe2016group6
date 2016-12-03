package com.group6boun451.learner.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Ahmet Zorer on 11/16/2016.
 */

public class TouchyWebView extends WebView {

    public TouchyWebView(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        this.setWebChromeClient(new WebChromeClient());
        this.getSettings().setPluginState(WebSettings.PluginState.ON);
        this.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        this.setWebViewClient(new WebViewClient());
        this.getSettings().setJavaScriptEnabled(true);
    }

    public TouchyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TouchyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //Check is required to prevent crash
        if (MotionEventCompat.findPointerIndex(event, 0) == -1) {
            return super.onTouchEvent(event);
        }

        if (event.getPointerCount() >= 2) {
            requestDisallowInterceptTouchEvent(true);
        } else {
            requestDisallowInterceptTouchEvent(false);
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        requestDisallowInterceptTouchEvent(true);


    }

}