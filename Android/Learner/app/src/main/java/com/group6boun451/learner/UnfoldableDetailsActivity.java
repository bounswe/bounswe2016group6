package com.group6boun451.learner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alexvasilkov.android.commons.texts.SpannableBuilder;
import com.alexvasilkov.android.commons.utils.Views;
import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.alexvasilkov.foldablelayout.shading.GlanceFoldShading;
import com.group6boun451.learner.items.Topic;
import com.group6boun451.learner.items.TopicsAdapter;
import com.group6boun451.learner.utils.GlideHelper;

import butterknife.BindView;

public class UnfoldableDetailsActivity extends AppCompatActivity {

    @BindView(R.id.touch_interceptor_view) View listTouchInterceptor;
    @BindView(R.id.details_scrollView) ScrollView detailsScrollView;
    @BindView(R.id.unfoldable_view) UnfoldableView unfoldableView;
    @BindView(R.id.list_view) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfoldable_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView.setAdapter(new TopicsAdapter(this));
        listTouchInterceptor.setClickable(false);

        Bitmap glance = BitmapFactory.decodeResource(getResources(), R.drawable.unfold_glance);
        unfoldableView.setFoldShading(new GlanceFoldShading(glance));
        unfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(true);
                detailsScrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(final UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(false);
                if (detailsScrollView.getChildAt(0).getHeight()>listView.getHeight())
                    unfoldableView.setGesturesEnabled(false);
                detailsScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if(detailsScrollView.getScrollY()==0) unfoldableView.setGesturesEnabled(true);
                        else unfoldableView.setGesturesEnabled(false);
                    }
                });
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(true);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(false);
                detailsScrollView.setVisibility(View.INVISIBLE);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull @Override
    public ActionBar getSupportActionBar() {
        // Making getSupportActionBar() method to be @NonNull
        ActionBar actionBar = super.getSupportActionBar();
        if (actionBar == null) {
            throw new NullPointerException("Action bar was not initialized");
        }
        return actionBar;
    }
    @Override
    public void onBackPressed() {
        if (unfoldableView != null && (unfoldableView.isUnfolded() || unfoldableView.isUnfolding())) {
            unfoldableView.foldBack();
        } else {
            super.onBackPressed();
        }
    }

    public void openDetails(View coverView, Topic topic) {
        final ImageView image = Views.find(detailsScrollView, R.id.details_image);
        final TextView title = Views.find(detailsScrollView, R.id.details_title);
        final TextView description = Views.find(detailsScrollView, R.id.details_text);

        GlideHelper.loadPaintingImage(image, topic);
        title.setText(topic.getTitle());

        SpannableBuilder builder = new SpannableBuilder(this);
        builder
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.year).append(": ")
                .clearStyle()
                .append(topic.getYear()).append("\n")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.location).append(": ")
                .clearStyle()
                .append(topic.getLocation());
        description.setText(builder.build());

        unfoldableView.unfold(coverView, detailsScrollView);
    }

}
