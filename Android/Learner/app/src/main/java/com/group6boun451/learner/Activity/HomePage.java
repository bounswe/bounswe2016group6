package com.group6boun451.learner.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.group6boun451.learner.CommentContainer;
import com.group6boun451.learner.CommentListAdapter;
import com.group6boun451.learner.ProfileActivity;
import com.group6boun451.learner.R;
import com.group6boun451.learner.utils.GlideHelper;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePage extends AppCompatActivity{
    protected static final String TAG = HomePage.class.getSimpleName();

    @BindView(R.id.touch_interceptor_view) View listTouchInterceptor;
    @BindView(R.id.topic_TabHost) TabHost tabHost;
    @BindView(R.id.details_scrollView) ScrollView detailsScrollView;
    @BindView(R.id.unfoldable_view) UnfoldableView unfoldableView;
    @BindView(R.id.activity_topic_pager_view_pager) ViewPager viewpager;
    @BindView(R.id.activity_topic_pager_view_pager_popular) ViewPager viewpager2;
    @BindView(R.id.activity_topic_pager_view_pager_mostrecent) ViewPager viewpager3;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.fab2) FloatingActionButton fabQuiz;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.content_hamburger) View contentHamburger;

    private List<com.group6boun451.learner.model.Topic> topics;
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
        new FetchSecuredResourceTask().execute();

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
                }else {startActivity(new Intent(HomePage.this, AddTopicActivity.class));}
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
                fabQuiz.setVisibility(View.VISIBLE);
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
                fabQuiz.setVisibility(View.INVISIBLE);
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

    public void openDetails(View coverView, com.group6boun451.learner.model.Topic topic) {
        final ImageView image = Views.find(tabHost, R.id.details_image);
        final TextView title = Views.find(tabHost, R.id.details_title);
        final TextView description = Views.find(tabHost, R.id.details_text);
        GlideHelper.loadImage(image, topic);
        title.setText(topic.getHeader());

        SpannableBuilder builder = new SpannableBuilder(this);
        builder.append(R.string.by).append(" ").append(topic.getOwner().getFirstName()).append("\t")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.date).append(" ")
                .clearStyle()
                .append(topic.getRevealDate().toString()).append("\n")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.content).append(": ")
                .clearStyle()
                .append(topic.getContent());
        description.setText(builder.build());

        ListView comments = (ListView) findViewById(R.id.topicPageCommentList);
        CommentListAdapter cladap = new CommentListAdapter(this, (new CommentContainer(this)).getComments());
        comments.setAdapter(cladap);

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

        unfoldableView.unfold(coverView, tabHost);
    }

    public class TopicPagerAdapter extends PagerAdapter {
        private Context mContext;
        TopicPagerAdapter(Context context) {mContext = context;}

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            final com.group6boun451.learner.model.Topic topic = topics.get(position);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup v = (ViewGroup) inflater.inflate(R.layout.topic_item_home, collection, false);
            ((TextView) v.findViewById(R.id.textTopicTitle)).setText(topic.getHeader());
            ((TextView) v.findViewById(R.id.textAuthor)).setText(topic.getOwner().getFirstName());
            ((TextView) v.findViewById(R.id.textDate)).setText(topic.getRevealDate().toString());

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

    public class FetchSecuredResourceTask extends AsyncTask<Void, Void, com.group6boun451.learner.model.Topic[]> {
        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            username = preferences.getString(getString(R.string.user_name), " ");
            password = preferences.getString(getString(R.string.password), " ");
        }

        @Override
        protected com.group6boun451.learner.model.Topic[] doInBackground(Void... params) {
            final String url = getString(R.string.base_url) + "topic/recommended";

            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            Log.d(TAG + " username", username + ", " + password);
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<com.group6boun451.learner.model.Topic[]> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), com.group6boun451.learner.model.Topic[].class);
                // Log.d("response",response.getBody());
                return response.getBody();
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new com.group6boun451.learner.model.Topic[0];
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new com.group6boun451.learner.model.Topic[0];
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new com.group6boun451.learner.model.Topic[0];
            }
        }

        @Override
        protected void onPostExecute(com.group6boun451.learner.model.Topic[] result) {
            topics = new ArrayList(Arrays.asList(result));
            viewpager.setAdapter(new TopicPagerAdapter(HomePage.this));
            viewpager2.setAdapter(new TopicPagerAdapter(HomePage.this));
            viewpager3.setAdapter(new TopicPagerAdapter(HomePage.this));
        }
    }



}
