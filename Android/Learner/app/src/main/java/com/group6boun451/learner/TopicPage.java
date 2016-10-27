package com.group6boun451.learner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class TopicPage extends AppCompatActivity {


    private TextView mTopicTitle;
    private TextView mTopicText;
    private ImageView mTopicImage;
    private TextView mTopicEditorName;
    private TextView mTopicDate;
    private Topic mTopic;
    private TopicContainer mTpc = new TopicContainer(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mTopic = mTpc.getTopic(getIntent().getExtras().getInt("topic_id"));
        createTopicComments();
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();


        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Topic");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Discussion");
        host.addTab(spec);


    }

    public static Intent newIntent(Context packageContext , int id){
        Intent intent = new Intent(packageContext,TopicPage.class);
        intent.putExtra("topic_id",id);
        return intent;
    }

    public void createTopicComments(){
        mTopicTitle = (TextView) findViewById(R.id.txtTopicPageTitle);
        mTopicImage = (ImageView) findViewById(R.id.imgTopicPageImage);
        mTopicText = (TextView) findViewById(R.id.txtTopicPageText);
        mTopicEditorName = (TextView) findViewById(R.id.txtTopicPageUserName);
        mTopicDate = (TextView) findViewById(R.id.txtTopicPageDate);

        mTopicTitle.setText(mTopic.getTitle());
        mTopicImage.setImageDrawable(getResources().getDrawable(mTopic.getImage()));
        mTopicText.setText(mTopic.getText());
        mTopicEditorName.setText(mTopic.getEditor());
        mTopicDate.setText(mTopic.getDate());

    }

}
