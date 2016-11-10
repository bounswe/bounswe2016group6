package com.group6boun451.learner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePage extends AppCompatActivity{
    @BindView(R.id.root) CoordinatorLayout root;
    @BindView(R.id.touch_interceptor_view) View listTouchInterceptor;
    @BindView(R.id.tabHost) TabHost tabHost;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        guillotineMenu.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.addView(guillotineMenu);
        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(250)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

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

        if (!isTeacher) fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTopicActive){
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
                        isSnackBarActive= true;
                    }

                }else {
                    Intent intent = new Intent(HomePage.this, AddTopicActivity.class);
                    startActivity(intent);
                }
            }
        });

        tpc = new TopicContainer(this);
        topics = tpc.getTopics();

        listTouchInterceptor.setClickable(false);
        viewpager.setAdapter(new TopicPagerAdapter(getSupportFragmentManager()));
        viewpager2.setAdapter(new TopicPagerAdapter(getSupportFragmentManager()));
        viewpager3.setAdapter(new TopicPagerAdapter(getSupportFragmentManager()));

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
              // if (detailsScrollView.getChildAt(0).getHeight()>root.getRootView().getHeight())
                    unfoldableView.setGesturesEnabled(false);

                detailsScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if(detailsScrollView.getScrollY()==0) unfoldableView.setGesturesEnabled(true);
                        else unfoldableView.setGesturesEnabled(false);
                    }
                });
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
                tabHost.setVisibility(View.INVISIBLE);
                isTopicActive = false;
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
            }
        });

    }

    @Override
    public void onBackPressed() {

//        if (root.isDrawerOpen(GravityCompat.START)) {
//            root.closeDrawer(GravityCompat.START);
//        } else
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

        GlideHelper.loadPaintingImage(image, topic);
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


    private class TopicPagerAdapter extends FragmentStatePagerAdapter {
        public TopicPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Topic topic = topics.get(position);
            return TopicHomeFragment.newInstance(topic.getId());
        }

        @Override
        public int getCount() {
            return topics.size();
        }
    };
}
