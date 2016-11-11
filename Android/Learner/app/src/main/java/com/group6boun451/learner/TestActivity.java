package com.group6boun451.learner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.group6boun451.learner.widget.CanaroTextView;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.nashapp.androidsummernote.Summernote;

public class TestActivity extends AppCompatActivity {
    Summernote summernote;
    @BindView(R.id.content_hamburger)
    View contentHamburger;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private GuillotineAnimation guillotineAnimation;
    private boolean isGuillotineOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
        ((CanaroTextView) toolbar.findViewById(R.id.title)).setText("ADD TOPIC");
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        guillotineMenu.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewGroup) findViewById(android.R.id.content)).addView(guillotineMenu);
        guillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(250)
                .setActionBarViewForAnimation(toolbar)
                .setGuillotineListener(new GuillotineListener() {
                    @Override
                    public void onGuillotineOpened() {
                        isGuillotineOpened = true;
                    }

                    @Override
                    public void onGuillotineClosed() {
                        isGuillotineOpened = false;

                    }
                })
                .setClosedOnStart(true)
                .build();
        summernote = (Summernote) findViewById(R.id.summernote);
        summernote.setRequestCodeforFilepicker(5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        summernote.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
        if (isGuillotineOpened) {
            guillotineAnimation.close();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test, menu);//Second es your new xml.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.get_text:
                Snackbar.make(findViewById(android.R.id.content), summernote.getText(), Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.set_text:
                summernote.setText("<h2>Hello World ' \"</h2>");
                break;
        }
        return true;
    }

}