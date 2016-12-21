package com.group6boun451.learner.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.group6boun451.learner.CommentListAdapter;
import com.group6boun451.learner.R;
import com.group6boun451.learner.model.Comment;
import com.group6boun451.learner.model.Question;
import com.group6boun451.learner.model.QuizResult;
import com.group6boun451.learner.model.Tag;
import com.group6boun451.learner.model.Topic;
import com.group6boun451.learner.model.User;
import com.group6boun451.learner.utils.GlideHelper;
import com.group6boun451.learner.utils.TopicPagerAdapter;
import com.group6boun451.learner.widget.CanaroTextView;
import com.group6boun451.learner.widget.Summernote;
import com.group6boun451.learner.widget.TouchyWebView;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import net.danlew.android.joda.JodaTimeAndroid;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.group6boun451.learner.utils.GlideHelper.getReadableDateFromDate;

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

    public static String username;
    private boolean isTeacher = true;
    private boolean isSnackBarActive = false;
    private boolean isTopicActive= false;
    private boolean isGuillotineOpened = false;
    public static boolean isThereQuiz = false;
    private GuillotineAnimation guillotineAnimation;
    public static User user;
    public static Topic currentTopic = null;
    private String commentContent;

    /**
     * Initializes all variables and gets topics.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        JodaTimeAndroid.init(this);
        user = Task.getResult(
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .getString(getString(R.string.user), " "),User.class);
        username = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.user_name), " ");
//        fetch topics
       fetchTasks("recommended",viewpager);// TODO: 12/8/2016 recommended
       fetchTasks("popular",viewpager2);
       fetchTasks("recent",viewpager3);

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
                startActivity(new Intent(HomePage.this, com.group6boun451.learner.activity.ProfileActivity.class));
                guillotineAnimation.close();
            }
        });
        guillotineMenu.findViewById(R.id.activity_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, SearchActivity.class);
                intent.putExtra("tagName",getString(R.string.feed));
                intent.putExtra("query","0");
                intent.putExtra("type","recommendAll");
                startActivity(intent);
                guillotineAnimation.close();
            }
        });
        guillotineMenu.findViewById(R.id.activity_log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                //TODO check if those are empty
                editor.putString(getString(R.string.user_name), "");
                editor.putString(getString(R.string.password), "");
                editor.commit();
                guillotineAnimation.close();
                startActivity(new Intent(HomePage.this, com.group6boun451.learner.activity.LoginActivity.class));
                finish();
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
                        if(isTopicActive && isThereQuiz) fabQuiz.setVisibility(View.VISIBLE);
                        if(isTeacher||isTopicActive) fab.setVisibility(View.VISIBLE);

                    }
                })
                .setClosedOnStart(true)
                .build();

//       tabs on a topic
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("Tab One").setContent(R.id.topic_tab).setIndicator("Topic"));
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
                else {startActivity(new Intent(HomePage.this, com.group6boun451.learner.activity.AddTopicActivity.class));}
            }
        });

        fabQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomePage.this, com.group6boun451.learner.activity.QuizActivity.class);
                startActivityForResult(intent,31415);
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
                        new com.group6boun451.learner.activity.Task<URI>(HomePage.this, new com.group6boun451.learner.activity.Task.Callback() {
                            @Override
                            public void onResult(String result) {GlideHelper.showResult(HomePage.this,result);}
                        }).execute(builder.build().encode().toUri());
                    }
                    contentView.loadDataWithBaseURL("http://www.youtube.com/embed/", content,
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
                    new com.group6boun451.learner.activity.Task<URI>(HomePage.this, new com.group6boun451.learner.activity.Task.Callback() {
                        @Override
                        public void onResult(String result) {
                            if(GlideHelper.showResult(HomePage.this,result)) {
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

    /**
     * Fetches topics with given category.
     * @param category
     * @param viewpager
     */
    private void fetchTasks(String category, final ViewPager viewpager) {
        new com.group6boun451.learner.activity.Task<String>(this, new com.group6boun451.learner.activity.Task.Callback() {
            @Override
            public void onResult(String resultString) {
                Topic[] result = com.group6boun451.learner.activity.Task.getResult(resultString,Topic[].class);
                List<Topic> topics = new ArrayList(Arrays.asList(result));
                //TODO handle this
                viewpager.setAdapter(new TopicPagerAdapter(HomePage.this,topics));

            }
        }).execute(getString(R.string.base_url) + "topic/"+category);
    }

    /**
     * Toggles add comment tab.
     * @param view
     */
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

    /**
     * Quits from topic edit state.
     */
    private void editDone() {
        contentView.setVisibility(View.VISIBLE);
        summernote.setVisibility(View.INVISIBLE);
        editButton.setText(getString(R.string.edit));
    }

    /**
     * Returns results of quiz.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 31415 && resultCode == RESULT_OK){
            QuizResult quizResult = new QuizResult(
                    data.getIntExtra("correct",0),
                    data.getIntExtra("count",0));

            new Task<>(HomePage.this, new Task.Callback() {
                @Override
                public void onResult(String resultString) {
                    GlideHelper.showResult(HomePage.this,resultString);
                }
            }).execute(getString(R.string.base_url) + "quiz/" +currentTopic.getId() +"/result/save",quizResult);
            return;
        }

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

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                startActivity(new Intent(HomePage.this, com.group6boun451.learner.activity.SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * When user clicks any topic, the detailed view of the clicked topic is opened.
     * @param coverView
     * @param topic
     */
    public void openDetails(View coverView, final Topic topic) {
        //test Quiz
        List<Question> mQues = new ArrayList<Question>();
        Question q1 = new Question();
        q1.setAnswerA("q1aassdaasd");
        q1.setAnswerB("sadasa");
        q1.setAnswerC("adfasdasd");
        q1.setCorrect(1);
        q1.setQuestion("asdasdashdjnaksflmdasdöasdasd");
        mQues.add(q1);
        Question q2 = new Question();
        q2.setAnswerA("q2aassdaasd");
        q2.setAnswerB("sadasa");
        q2.setAnswerC("adfasdasd");
        q2.setCorrect(2);
        q2.setQuestion("asdasdashdjnaksflmdasdöasdasd");
        mQues.add(q2);
        Question q3 = new Question();
        q3.setAnswerA("q3aassdaasd");
        q3.setAnswerB("sadasa");
        q3.setAnswerC("adfasdasd");
        q3.setCorrect(1);
        q3.setQuestion("asdasdashdjnaksflmdasdöasdasd");
        mQues.add(q3);

        topic.setQuestions(mQues);
        //test finish
        currentTopic = topic;
        Log.d("topic",currentTopic.getId()+"");
        GlideHelper.loadImage(this,(ImageView) Views.find(tabHost, R.id.details_image), topic);
        ((CanaroTextView)Views.find(tabHost, R.id.details_title)).setText(topic.getHeader());
        ((CanaroTextView)Views.find(tabHost, R.id.txtTopicPageUserName)).setText(topic.getOwner().getFirstName());
        Views.find(tabHost, R.id.user_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, SearchActivity.class);
                intent.putExtra("tagName","Topics of "+ topic.getOwner().getFirstName());
                intent.putExtra("query",topic.getOwner().getId()+"");
                intent.putExtra("type","following");
                startActivity(intent);
            }
        });

        ((CanaroTextView)Views.find(tabHost, R.id.txtTopicPageDate)).setText(getReadableDateFromDate(HomePage.this,topic.getRevealDate()));

//tags
        ChipsView mChipsView = Views.find(tabHost, R.id.cv_contacts);
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
        for(final ChipsView.Chip chip: mChipsView.getChips()) {
            chip.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomePage.this, SearchActivity.class);
                    intent.putExtra("tagName",chip.getContact().getFirstName());
                    intent.putExtra("query",chip.getContact().getDisplayName());
                    startActivity(intent);
                }
            });
        }
        Button packButton = Views.find(tabHost,R.id.pack_button);
        packButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, SearchActivity.class);
                if(topic.getTopicPack()!=null) intent.putExtra("tagName",topic.getTopicPack().getName());
                intent.putExtra("query",topic.getId()+"");
                intent.putExtra("type","pack");
                startActivity(intent);
            }
        });

        Button recommendButton = Views.find(tabHost,R.id.recommend_button);
        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, SearchActivity.class);
                intent.putExtra("tagName",topic.getHeader());
                intent.putExtra("query",topic.getId()+"");
                intent.putExtra("type","recommend");
                startActivity(intent);
            }
        });


//content
        contentView.loadDataWithBaseURL("http://www.youtube.com/embed/", topic.getContent(),
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

}
