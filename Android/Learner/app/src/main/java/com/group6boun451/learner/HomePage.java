package com.group6boun451.learner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import com.alexvasilkov.android.commons.texts.SpannableBuilder;
import com.alexvasilkov.android.commons.utils.Views;
import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.alexvasilkov.foldablelayout.shading.GlanceFoldShading;
import com.group6boun451.learner.utils.GlideHelper;
import com.group6boun451.learner.widget.CanaroTextView;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePage extends AppCompatActivity{

    @BindView(R.id.touch_interceptor_view) View listTouchInterceptor;
    @BindView(R.id.topic_TabHost) TabHost tabHost;
    @BindView(R.id.details_scrollView) ScrollView detailsScrollView;
    @BindView(R.id.unfoldable_view) UnfoldableView unfoldableView;
    @BindView(R.id.activity_topic_pager_view_pager) ViewPager viewpager;
    @BindView(R.id.activity_topic_pager_view_pager_popular) ViewPager viewpager2;
    @BindView(R.id.activity_topic_pager_view_pager_mostrecent) ViewPager viewpager3;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.content_hamburger) View contentHamburger;

    private List<Topic> topics;
    private TopicContainer tpc;
    private boolean isTeacher = true;
    private boolean isSnackBarActive = false;
    private boolean isTopicActive= false;
    private boolean isGuillotineOpened = false;
    private GuillotineAnimation guillotineAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
        ((CanaroTextView) toolbar.findViewById(R.id.title)).setText("LEARNER");
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        guillotineMenu.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewGroup)findViewById(android.R.id.content)).addView(guillotineMenu);
        guillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(250)
                .setActionBarViewForAnimation(toolbar)
                .setGuillotineListener(new GuillotineListener() {
                    @Override
                    public void onGuillotineOpened() {
                        isGuillotineOpened=true;
                        fab.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onGuillotineClosed() {
                        isGuillotineOpened=false;
                        if(isTeacher||isTopicActive) fab.setVisibility(View.VISIBLE);

                    }
                })
                .setClosedOnStart(true)
                .build();

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

        if (!isTeacher) fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTopicActive){
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

                }else {
//                    Intent intent = new Intent(HomePage.this, AddTopicActivity.class);
//                    startActivity(intent);
                    startActivity(new Intent(HomePage.this, TestActivity.class));

                }
            }
        });

        tpc = new TopicContainer(this);
        topics = tpc.getTopics();

        listTouchInterceptor.setClickable(false);
        viewpager.setAdapter(new TopicPagerAdapter(this));
        viewpager2.setAdapter(new TopicPagerAdapter(this));
        viewpager3.setAdapter(new TopicPagerAdapter(this));

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
                tabHost.setVisibility(View.INVISIBLE);
                tabHost.setCurrentTab(0);
                isTopicActive = false;
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
            }
        });

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDetails(View coverView, Topic topic) {
        final ImageView image = Views.find(tabHost, R.id.details_image);
        final TextView title = Views.find(tabHost, R.id.details_title);
        final TextView description = Views.find(tabHost, R.id.details_text);
        GlideHelper.loadImage(image, topic);
        title.setText(topic.getTitle());

        SpannableBuilder builder = new SpannableBuilder(this);
        builder.append(R.string.by).append(" ").append(topic.getEditor()).append("\t")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.date).append(" ")
                .clearStyle()
                .append(topic.getDate()).append("\n")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.content).append(": ")
                .clearStyle()
                .append(topic.getText());
        description.setText(builder.build());

        topic.setComments(new CommentContainer(this));
        ListView comments = (ListView) findViewById(R.id.topicPageCommentList);
        CommentListAdapter cladap = new CommentListAdapter(this,topic.getComments());
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


        unfoldableView.unfold(coverView, tabHost);
    }

    public class TopicPagerAdapter extends PagerAdapter {
        private Context mContext;
        TopicPagerAdapter(Context context) {mContext = context;}

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            final Topic topic = topics.get(position);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup v = (ViewGroup) inflater.inflate(R.layout.topic_item_home, collection, false);
            ((TextView)v.findViewById(R.id.textTopicTitle)).setText(topic.getTitle());
            ((TextView) v.findViewById(R.id.textAuthor)).setText(topic.getEditor());
            ((TextView) v.findViewById(R.id.textDate)).setText(topic.getDate());

            final ImageView img = (ImageView) v.findViewById(R.id.imageTopic);
            GlideHelper.loadImage(img, topic);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomePage) v.getContext()).openDetails(v.findViewById(R.id.card_view), topic);
                }
            });

            collection.addView(v);
            return v;
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
