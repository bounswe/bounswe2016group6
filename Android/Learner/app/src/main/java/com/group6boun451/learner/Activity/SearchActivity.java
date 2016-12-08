package com.group6boun451.learner.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.arlib.floatingsearchview.FloatingSearchView;
import com.doodle.android.chips.ChipsView;
import com.doodle.android.chips.model.Contact;
import com.group6boun451.learner.CommentListAdapter;
import com.group6boun451.learner.R;
import com.group6boun451.learner.model.Comment;
import com.group6boun451.learner.model.Tag;
import com.group6boun451.learner.model.Topic;
import com.group6boun451.learner.utils.GlideHelper;
import com.group6boun451.learner.utils.SearchResultsListAdapter;
import com.group6boun451.learner.widget.CanaroTextView;
import com.group6boun451.learner.widget.Summernote;
import com.group6boun451.learner.widget.TouchyWebView;

import net.danlew.android.joda.JodaTimeAndroid;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.group6boun451.learner.utils.GlideHelper.getReadableDateFromDate;


/** Activity for location search page. Location search activity is used for retrieving the data
 * and returning it to the caller.
 */
public class SearchActivity extends AppCompatActivity {
    protected static final String TAG = SearchActivity.class.getSimpleName();
    private static final int EDITOR = 3;

    @BindView(R.id.touch_interceptor_view) View listTouchInterceptor;
    @BindView(R.id.summernote) Summernote summernote;
    @BindView(R.id.topic_TabHost) TabHost tabHost;
    @BindView(R.id.details_scrollView) ScrollView detailsScrollView;
    @BindView(R.id.unfoldable_view) UnfoldableView unfoldableView;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.fab2) FloatingActionButton fabQuiz;
    @BindView(R.id.edit_button) Button editButton;
    @BindView(R.id.topicPageCommentList) ListView comments;
    @BindView(R.id.send_comment_button) Button sendCommentButton;
    @BindView(R.id.topicPage_comment_text_area) EditText commentText;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.topic_content_TouchyWebView) TouchyWebView contentView;

    public static String username;
    private boolean isSnackBarActive = false;
    private boolean isTopicActive= false;
    public static boolean isThereQuiz = false;
    public static Topic currentTopic = null;
    private String commentContent;

    Task<URI> f;
    private FloatingSearchView mSearchView;

    private SearchResultsListAdapter mSearchResultsAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        JodaTimeAndroid.init(this);
        username = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.user_name), " ");
        String title = getString(R.string.search);
        ((TextView)findViewById(R.id.header)).setText(title);
        findViewById(R.id.search_suggestions_section).setLayoutParams(new LinearLayout.LayoutParams(0,0));
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        RecyclerView mSearchResultsList = (RecyclerView) findViewById(R.id.search_results_list);
        mSearchResultsAdapter = new SearchResultsListAdapter(this);
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {}
        setupFloatingSearch();

        summernote.setRequestCodeforFilepicker(EDITOR);

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTopicActive){commentView(view);}
            }
        });

        fabQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,QuizActivity.class));
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
                fab.setVisibility(View.VISIBLE);
                if(isThereQuiz){
                    fabQuiz.setVisibility(View.VISIBLE);
                }
                mSearchView.clearSearchFocus();
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
                fab.setVisibility(View.INVISIBLE);
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
                        new Task<URI>(SearchActivity.this, new Task.Callback() {
                            @Override
                            public void onResult(String result) {GlideHelper.showResult(SearchActivity.this,result);}
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
                if(commentContent.length() < 9){
                    Snackbar.make(findViewById(android.R.id.content),"Comment is too short",Snackbar.LENGTH_SHORT).show();
                }else{
                    Topic t = currentTopic;
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getString(R.string.base_url) + "topic/comment/create")
                            .queryParam("topicId",t.getId()).queryParam("content",commentContent);
                    new Task<URI>(SearchActivity.this, new Task.Callback() {
                        @Override
                        public void onResult(String result) {
                            if(GlideHelper.showResult(SearchActivity.this,result)) {
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
        if (unfoldableView != null && (unfoldableView.isUnfolded() || unfoldableView.isUnfolding())) {
            unfoldableView.foldBack();
        }else {
            super.onBackPressed();
        }
    }

    public void openDetails(View coverView, Topic topic) {
        currentTopic = topic;
        Log.d("topic",currentTopic.getId()+"");
        GlideHelper.loadImage(this,(ImageView) Views.find(tabHost, R.id.details_image), topic);
        ((CanaroTextView)Views.find(tabHost, R.id.details_title)).setText(topic.getHeader());
        ((CanaroTextView)Views.find(tabHost, R.id.txtTopicPageUserName)).setText(topic.getOwner().getFirstName());


        ((CanaroTextView)Views.find(tabHost, R.id.txtTopicPageDate)).setText(getReadableDateFromDate(SearchActivity.this,topic.getRevealDate()));

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

    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                if (f != null) {
                    f.cancel(true);
                }
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchResultsAdapter.swapData(Collections.<Topic>emptyList());
                    mSearchView.hideProgress();
                } else if(newQuery.length()>2) {
                    mSearchView.showProgress();
                    //        fetch results
                    f =  new Task<>(SearchActivity.this, new Task.Callback() {
                                @Override
                                public void onResult(String resultString) {
                                    Topic[] result = Task.getResult(resultString, Topic[].class);
                                    if (result != null) {
                                        mSearchResultsAdapter.swapData(Arrays.asList(result));
                                    }
                                    mSearchView.hideProgress();
                                }
                            });
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getString(R.string.base_url) + "search/keyword")
                            .queryParam("keyword",newQuery);

                    f.execute(builder.build().encode().toUri());
                }
            }
        });
        mSearchResultsAdapter.setItemsOnClickListener(new SearchResultsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(Topic location) {
                openDetails(findViewById(R.id.card_view2), location);
            }
        });
        mSearchView.setSearchFocused(true);
    }

}
