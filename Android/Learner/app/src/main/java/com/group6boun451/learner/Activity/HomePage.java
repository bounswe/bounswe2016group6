package com.group6boun451.learner.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import com.alexvasilkov.android.commons.utils.Views;
import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.alexvasilkov.foldablelayout.shading.GlanceFoldShading;
import com.doodle.android.chips.ChipsView;
import com.doodle.android.chips.model.Contact;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6boun451.learner.CommentListAdapter;
import com.group6boun451.learner.R;
import com.group6boun451.learner.model.Comment;
import com.group6boun451.learner.model.GenericResponse;
import com.group6boun451.learner.model.Tag;
import com.group6boun451.learner.model.Topic;
import com.group6boun451.learner.model.User;
import com.group6boun451.learner.utils.GlideHelper;
import com.group6boun451.learner.widget.Summernote;
import com.group6boun451.learner.widget.TouchyWebView;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePage extends AppCompatActivity{
    protected static final String TAG = HomePage.class.getSimpleName();
    private static final int EDITOR = 3;

    @BindView(R.id.touch_interceptor_view) View listTouchInterceptor;
    @BindView(R.id.summernote) Summernote summernote;
    @BindView(R.id.topic_TabHost) TabHost tabHost;
    @BindView(R.id.details_scrollView) ScrollView detailsScrollView;
    @BindView(R.id.unfoldable_view) UnfoldableView unfoldableView;
    @BindView(R.id.activity_topic_pager_view_pager) ViewPager viewpager;
    @BindView(R.id.activity_topic_pager_view_pager_popular) ViewPager viewpager2;
    @BindView(R.id.activity_topic_pager_view_pager_mostrecent) ViewPager viewpager3;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.fab2) FloatingActionButton fabQuiz;
    @BindView(R.id.edit_button) Button editButton;
    @BindView(R.id.topicPageCommentList) ListView comments;
    @BindView(R.id.send_comment_button) Button sendCommentButton;
    @BindView(R.id.topicPage_comment_text_area) EditText commentText;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.content_hamburger) View contentHamburger;
    @BindView(R.id.topic_content_TouchyWebView) TouchyWebView contentView;

    public static List<Topic> topics;
    private String username;
    private boolean isTeacher = true;
    private boolean isSnackBarActive = false;
    private boolean isTopicActive= false;
    private boolean isGuillotineOpened = false;
    private boolean isThereQuiz = false;
    private GuillotineAnimation guillotineAnimation;
    public static User user;
    public static Topic currentTopic = null;
    private String commentContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        initUser();
        username = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.user_name), " ");
//        fetch topics
        new Task<String>(this, new Callback() {
            @Override
            public void onResult(String resultString) {
                Topic[] result = null;
                ObjectMapper mapper = new ObjectMapper();
                try {
                    result = mapper.readValue(resultString, Topic[].class);
                }catch (Exception e) {e.printStackTrace();}
                topics = new ArrayList(Arrays.asList(result));
                //TODO handle this
                viewpager.setAdapter(new TopicPagerAdapter(HomePage.this));
                viewpager2.setAdapter(new TopicPagerAdapter(HomePage.this));
                viewpager3.setAdapter(new TopicPagerAdapter(HomePage.this));
            }
        }).execute(getString(R.string.base_url) + "topic/recent");

        summernote.setRequestCodeforFilepicker(EDITOR);

//        toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
//        drawer
        final View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        guillotineMenu.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewGroup)findViewById(android.R.id.content)).addView(guillotineMenu);
        guillotineMenu.findViewById(R.id.profile_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, ProfileActivity.class));
                guillotineAnimation.close();
            }
        });
        guillotineMenu.findViewById(R.id.activity_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, SearchActivity.class));
                guillotineAnimation.close();
            }
        });
        guillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(250)
                .setActionBarViewForAnimation(toolbar)
                .setGuillotineListener(new GuillotineListener() {
                    @Override
                    public void onGuillotineOpened() {
                        isGuillotineOpened=true;
                        fab.setVisibility(View.INVISIBLE);
                        fabQuiz.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onGuillotineClosed() {
                        isGuillotineOpened=false;
                        if(isTopicActive) fabQuiz.setVisibility(View.VISIBLE);
                        if(isTeacher||isTopicActive) fab.setVisibility(View.VISIBLE);

                    }
                })
                .setClosedOnStart(true)
                .build();

//       tabs on a topic
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("Tab One").setContent(R.id.topic_tab).setIndicator("Example"));
        tabHost.addTab(tabHost.newTabSpec("Tab Two").setContent(R.id.comment_tab).setIndicator("Discussion"));

        detailsScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(tabHost.getCurrentTab()==0&&detailsScrollView.getScrollY()==0) unfoldableView.setGesturesEnabled(true);
                else unfoldableView.setGesturesEnabled(false);
            }
        });

//        fab
        if (!isTeacher) fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTopicActive){commentView(view);}
                else {startActivity(new Intent(HomePage.this, AddTopicActivity.class));}
            }
        });

        fabQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomePage.this,QuizActivity.class);
                startActivity(intent);
            }
        });

//      opening animation of a topic
        listTouchInterceptor.setClickable(false);
        Bitmap glance = BitmapFactory.decodeResource(getResources(), R.drawable.unfold_glance);
        unfoldableView.setFoldShading(new GlanceFoldShading(glance));
        unfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(true);
                tabHost.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(final UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(false);
                if (detailsScrollView.getChildAt(0).getHeight()>(findViewById(android.R.id.content).getHeight()-findViewById(R.id.toolbar).getHeight()))
                    unfoldableView.setGesturesEnabled(false);
                isTopicActive = true;
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_floatmenu_comment));
                if(isThereQuiz){
                    fabQuiz.setVisibility(View.VISIBLE);
                }
                //TODO make fab animation here
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(true);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(false);
                unfoldableView.setGesturesEnabled(true);
                //return to initial state
                tabHost.setVisibility(View.INVISIBLE);
                tabHost.setCurrentTab(0);
                editButton.setVisibility(View.GONE);
                isTopicActive = false;
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                if(isThereQuiz){
                    fabQuiz.setVisibility(View.INVISIBLE);
                }
                editDone();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editButton.getText().equals(getString(R.string.edit))){
                    summernote.setVisibility(View.VISIBLE);
                    contentView.setVisibility(View.INVISIBLE);
                    editButton.setText(getString(R.string.done));
                }else {
                    editDone();
                    String content = summernote.getText();
                    Topic t = currentTopic;
                    if(!t.getContent().equals(content)){
                        t.setContent(content);
                        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getString(R.string.base_url) + "topic/edit/"+t.getId()).queryParam("header",t.getHeader()).queryParam("content",content);
                        new Task<URI>(HomePage.this, new Callback() {
                            @Override
                            public void onResult(String result) {showResult(result);}
                        }).execute(builder.build().encode().toUri());
                    }
                    contentView.loadDataWithBaseURL("https://www.youtube.com/embed/", content,
                            "text/html; charset=utf-8", "UTF-8", null);
                }

            }
        });

        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentContent = commentText.getText().toString();
                if(commentContent.length() < 9){Snackbar.make(findViewById(android.R.id.content),"Comment is too short",Snackbar.LENGTH_SHORT).show();
                }else{
                    Topic t = currentTopic;
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getString(R.string.base_url) + "topic/comment/create")
                            .queryParam("topicId",t.getId()).queryParam("content",commentContent);
                    new Task<URI>(HomePage.this, new Callback() {
                        @Override
                        public void onResult(String result) {
                            if(showResult(result)) {
                                ((CommentListAdapter)comments.getAdapter()).add(new Comment(commentContent));
                            }
                        }
                    }).execute(builder.build().encode().toUri());
                    commentText.setText("");
                    commentView(view);
                }
            }
        });
    }

    private void initUser() {
        Intent intent = getIntent();
        ObjectMapper mapper = new ObjectMapper();
        try {
            user = mapper.readValue(intent.getStringExtra("user"), User.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commentView(View view) {
        LinearLayout lyt = (LinearLayout) findViewById(R.id.new_comment_snack);
        if(isSnackBarActive) {
            lyt.setVisibility(View.INVISIBLE);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_floatmenu_comment));
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            isSnackBarActive = false;
        }else{
            lyt.setVisibility(View.VISIBLE);
            EditText textArea = (EditText) findViewById(R.id.topicPage_comment_text_area);
            textArea.requestFocus();
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_floatmenu_comment_quit));
            isSnackBarActive= true;
        }
    }

    private void editDone() {
        contentView.setVisibility(View.VISIBLE);
        summernote.setVisibility(View.INVISIBLE);
        editButton.setText(getString(R.string.edit));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        summernote.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {

        if (isGuillotineOpened) {
            guillotineAnimation.close();
        } else
        if (unfoldableView != null && (unfoldableView.isUnfolded() || unfoldableView.isUnfolding())) {
            unfoldableView.foldBack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    public void openDetails(View coverView, Topic topic) {
        currentTopic = topic;
        Log.d("topic",currentTopic.getId()+"");
        GlideHelper.loadImage(this,(ImageView) Views.find(tabHost, R.id.details_image), topic);
        ((TextView)Views.find(tabHost, R.id.details_title)).setText(topic.getHeader());
        ((TextView)Views.find(tabHost, R.id.txtTopicPageUserName)).setText(topic.getOwner().getFirstName());
        ((TextView)Views.find(tabHost, R.id.txtTopicPageDate)).setText(topic.getRevealDate().toString());

//tags
        ChipsView mChipsView = Views.find(tabHost,R.id.cv_contacts);
        // change EditText config
        mChipsView.getEditText().setFocusableInTouchMode(false);
        int k = mChipsView.getChips().size();
        Contact c = new Contact(null,null,null,"",null);
        for(int i =0;i<k;i++){mChipsView.removeChipBy(c);}
        for(Tag t : topic.getTags()){
            String tagName = t.getName();
            Contact contact = new Contact(tagName, t.getContext(), t.getId()+"", "", null);
            mChipsView.addChip(tagName, null, contact,true);
        }
//content
        contentView.loadDataWithBaseURL("https://www.youtube.com/embed/", topic.getContent(),
                "text/html; charset=utf-8", "UTF-8", null);
//comments
        comments.setAdapter(new CommentListAdapter(this, topic.getComments()));//TODO it might troublesome
        comments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView) view.findViewById(R.id.commentText);
                int numOfLines = txt.getMaxLines();
                if (numOfLines == 3) {
                    txt.setMaxLines(150);
                } else {
                    txt.setMaxLines(3);
                }
            }
        });
        if(topic.getOwner().getEmail().equalsIgnoreCase(username)){
            editButton.setVisibility(View.VISIBLE);
            summernote.setText(topic.getContent());
        }
        unfoldableView.unfold(coverView, tabHost);
    }
    @Override
    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    private boolean showResult(String resultString) {
        GenericResponse result = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(resultString, GenericResponse.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(result==null) {
            return false;
        }else if (result.getError() == null) {// display a notification to the user with the response information
            Snackbar.make(findViewById(android.R.id.content),  result.getMessage(), Snackbar.LENGTH_SHORT).show();
            return true;
        } else {
            Snackbar.make(findViewById(android.R.id.content),  result.getError(), Snackbar.LENGTH_SHORT).show();
            return false;
        }
    }

    public class TopicPagerAdapter extends PagerAdapter {
        private Context mContext;
        TopicPagerAdapter(Context context) {mContext = context;}

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            final Topic topic = topics.get(position);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup v = (ViewGroup) inflater.inflate(R.layout.topic_item_home, collection, false);
            ((TextView) v.findViewById(R.id.textTopicTitle)).setText(topic.getHeader());
            ((TextView) v.findViewById(R.id.textAuthor)).setText(topic.getOwner().getFirstName());
            ((TextView) v.findViewById(R.id.textDate)).setText(topic.getRevealDate().toString());

            ChipsView mChipsView = (ChipsView) v.findViewById(R.id.cv_contacts);
            // change EditText config
            mChipsView.getEditText().setVisibility(View.GONE);
            int k = mChipsView.getChips().size();
            Contact c = new Contact(null,null,null,"",null);
            for(int i =0;i<k;i++){mChipsView.removeChipBy(c);}
            for(Tag t : topic.getTags()){
                String tagName = t.getName();
                Contact contact = new Contact(tagName, t.getContext(), t.getId()+"", tagName, null);
                mChipsView.addChip(tagName, null, contact,true);
            }


            final ImageView img = (ImageView) v.findViewById(R.id.imageTopic);
            GlideHelper.loadImage(HomePage.this,img, topic);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomePage) v.getContext()).openDetails(v.findViewById(R.id.card_view), topic);
                    //check is there quiz
                    isThereQuiz = topic.getQuestions().size() != 0;

                }
            });


            final Button likeButton = (Button)v.findViewById(R.id.like_button);
            if(isLiked(topic.getLikedBy())) likeButton.setTextColor(getResources().getColor(R.color.mdtp_accent_color));
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task<String> likeTask = new Task<>(HomePage.this, new Callback() {
                        @Override
                        public void onResult(String result) {showResult(result);}
                    });
                    if(likeButton.getCurrentTextColor()!=getResources().getColor(R.color.mdtp_accent_color)) {
                        likeTask.execute(getString(R.string.base_url) + "topic/like/"+topic.getId());
                        likeButton.setTextColor(getResources().getColor(R.color.mdtp_accent_color));
                    }else {
                        likeTask.execute(getString(R.string.base_url) + "topic/unlike/"+topic.getId());
                        likeButton.setTextColor(getResources().getColor(R.color.white));
                    }
                }
            });

            collection.addView(v);
            return v;
        }

        private boolean isLiked(List<User> likedBy) {
            for (User u:likedBy){if(u.getEmail().equalsIgnoreCase(username))return true;} return false;
        }

        @Override public void destroyItem(ViewGroup collection, int position, Object view) {collection.removeView((View) view);}

        @Override public int getCount() {return topics.size();}

        @Override public boolean isViewFromObject(View view, Object object) {return view == object;}

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }
}
