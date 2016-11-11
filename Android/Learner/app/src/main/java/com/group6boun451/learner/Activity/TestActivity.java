package com.group6boun451.learner.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;

import com.group6boun451.learner.R;
import com.group6boun451.learner.utils.Summernote;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {
    @BindView(R.id.summernote)
    Summernote summernote;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.content_hamburger)
    View contentHamburger;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.topic_name_layout)
    TextInputLayout topicNameLayout;
    @BindView(R.id.topic_name_textEdit)
    EditText topicNameEditText;

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
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        guillotineMenu.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewGroup) findViewById(android.R.id.content)).addView(guillotineMenu);
        topicNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateName();
            }
        });

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
        summernote.setRequestCodeforFilepicker(5);
    }

    public boolean validateName() {
        if (topicNameEditText.getText().toString().trim().isEmpty()) {
            topicNameLayout.setError(getString(R.string.enter_name));
            return false;
        } else {
            topicNameLayout.setErrorEnabled(false);
        }
        return true;
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
            case R.id.save_text:
                if (validateName()) {
                    webView.setVisibility(View.VISIBLE);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadData(summernote.getText(), "text/html", "UTF-8");
                    summernote.setVisibility(View.INVISIBLE);
                }
                break;
        }
        return true;
    }

}