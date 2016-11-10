package com.group6boun451.learner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
    private boolean isSnackBarActive = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

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



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final ScrollView sv = (ScrollView)findViewById(R.id.topicScrollView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSnackBarActive) {
                    LinearLayout lyt = (LinearLayout) findViewById(R.id.snackdeneme);

                    lyt.setVisibility(View.INVISIBLE);
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_floatmenu_comment));
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    isSnackBarActive = false;
                }else{

                    LinearLayout lyt = (LinearLayout) findViewById(R.id.snackdeneme);
                    lyt.setVisibility(View.VISIBLE);
                    EditText textArea = (EditText) findViewById(R.id.topicPage_comment_text_area);
                    textArea.requestFocus();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_floatmenu_comment_quit));
                    sv.fullScroll(ScrollView.FOCUS_DOWN);
                    isSnackBarActive= true;

                }



            }
        });

        mTopic = mTpc.getTopic(getIntent().getExtras().getInt("topic_id"));
        mTopic.setComments(new CommentContainer(this));
        createTopicComments();



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


        ListView comments = (ListView) findViewById(R.id.topicPageCommentList);
        CommentListAdapter cladap = new CommentListAdapter(this,mTopic.getComments());
        comments.setAdapter(cladap);

        comments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView) view.findViewById(R.id.commentText);
                int numOfLines = txt.getMaxLines();
                if(numOfLines == 3){
                    txt.setMaxLines(150);
                }else {
                    txt.setMaxLines(3);
                }
            }
        });

    }

}
