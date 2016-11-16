package com.group6boun451.learner.Activity;

import android.app.SearchManager;
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
import android.widget.Button;
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
import com.group6boun451.learner.CommentListAdapter;
import com.group6boun451.learner.ProfileActivity;
import com.group6boun451.learner.R;
import com.group6boun451.learner.model.GenericResponse;
import com.group6boun451.learner.model.Topic;
import com.group6boun451.learner.utils.GlideHelper;
import com.group6boun451.learner.widget.Summernote;
import com.group6boun451.learner.widget.TouchyWebView;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.content_hamburger) View contentHamburger;
    @BindView(R.id.topic_content_TouchyWebView) TouchyWebView contentView;

    private List<Topic> topics;
    private String username;
    private boolean isTeacher = true;
    private boolean isSnackBarActive = false;
    private boolean isTopicActive= false;
    private boolean isGuillotineOpened = false;
    private GuillotineAnimation guillotineAnimation;
    private int topicId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        username = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.user_name), " ");
        new FetchTopicsTask().execute();
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
                //return to initial state
                tabHost.setVisibility(View.INVISIBLE);
                tabHost.setCurrentTab(0);
                editButton.setVisibility(View.GONE);
                isTopicActive = false;
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                fabQuiz.setVisibility(View.INVISIBLE);
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
                    contentView.setVisibility(View.VISIBLE);
                    summernote.setVisibility(View.INVISIBLE);
                    String content = summernote.getText();
                    Topic t = topics.get(topicId);
                    if(!t.getContent().equals(content)){
                        topics.get(topicId).setContent(content);
                        new EditTopicTask().execute(topics.get(topicId).getHeader(),summernote.getText(),""+t.getId());
                      //TODO edit the content animation
                    }
                    contentView.loadData(content,"text/html",null);
                    editButton.setText(getString(R.string.edit));

                }

            }
        });

//        //TODO test-> remove lines below in this method after test
//        ListView comments = (ListView) findViewById(R.id.topicPageCommentList);
//        CommentListAdapter cladap = new CommentListAdapter(this, (new CommentContainer(this)).getComments());
//        comments.setAdapter(cladap);
//
//        comments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView txt = (TextView) view.findViewById(R.id.commentText);
//                int numOfLines = txt.getMaxLines();
//                if (numOfLines == 3) {
//                    txt.setMaxLines(150);
//                } else {
//                    txt.setMaxLines(3);
//                }
//            }
//        });
//
//        contentView.loadData("<HEAD>\n" +
//                "<TITLE>Basic HTML Sample Page</TITLE>\n" +
//                "</HEAD>\n" +
//                "<BODY BGCOLOR=\"WHITE\">\n" +
//                "<CENTER>\n" +
//                "<H1>A Simple Sample Web Page</H1>\n" +
//                " \n" +
//                "  <IMG SRC=\"http://sheldonbrown.com/images/scb_eagle_contact.jpeg\">\n" +
//                " \n" +
//                "\n" +
//                " \n" +
//                "  <H4>By Sheldon Brown</H4>\n" +
//                "<H2>Demonstrating a few HTML features</H2>\n" +
//                "</CENTER>\n" +
//                "HTML is really a very simple language. It consists of ordinary text, with commands that are enclosed by \"<\" and \">\" characters, or bewteen an \"&\" and a \";\". <P>\n" +
//                " \n" +
//                "You don't really need to know much HTML to create a page, because you can copy bits of HTML from other pages that do what you want, then change the text!<P>\n" +
//                " \n" +
//                "This page shows on the left as it appears in your browser, and the corresponding HTML code appears on the right. The HTML commands are linked to explanations of what they do.\n" +
//                " \n" +
//                " \n" +
//                "<H3>Line Breaks</H3>\n" +
//                "HTML doesn't normally use line breaks for ordinary text. A white space of any size is treated as a single space. This is because the author of the page has no way of knowing the size of the reader's screen, or what size type they will have their browser set for.<P>\n" +
//                " \n" +
//                "If you want to put a line break at a particular place, you can use the \"<BR>\" command, or, for a paragraph break, the \"<P>\" command, which will insert a blank line. The heading command (\"<4></4>\") puts a blank line above and below the heading text.\n" +
//                " \n" +
//                "<H4>Starting and Stopping Commands</H4>\n" +
//                "Most HTML commands come in pairs: for example, \"<H4>\" marks the beginning of a size 4 heading, and \"</H4>\" marks the end of it. The closing command is always the same as the opening command, except for the addition of the \"/\".<P>\n" +
//                " \n" +
//                "Modifiers are sometimes included along with the basic command, inside the opening command's < >. The modifier does not need to be repeated in the closing command.\n" +
//                " \n" +
//                " \n" +
//                "<H1>This is a size \"1\" heading</H1>\n" +
//                "<H2>This is a size \"2\" heading</H2>\n" +
//                "<H3>This is a size \"3\" heading</H3>\n" +
//                "<H4>This is a size \"4\" heading</H4>\n" +
//                "<H5>This is a size \"5\" heading</H5>\n" +
//                "<H6>This is a size \"6\" heading</H6>\n" +
//                "<center>\n" +
//                "<H4>Copyright © 1997, by\n" +
//                "<A HREF=\"http://sheldonbrown.com/index.html\">Sheldon Brown</A>\n" +
//                "</H4>\n" +
//                "If you would like to make a link or bookmark to this page, the URL is:<BR> http://sheldonbrown.com/web_sample1.html</body> ","text/html",null);


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
        final TextView description = Views.find(tabHost, R.id.details_text);
        GlideHelper.loadImage((ImageView) Views.find(tabHost, R.id.details_image), topic);
        ((TextView)Views.find(tabHost, R.id.details_title)).setText(topic.getHeader());

        SpannableBuilder builder = new SpannableBuilder(this);
        builder.append(R.string.by).append(" ").append(topic.getOwner().getFirstName()).append("\t")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.date).append(" ")
                .clearStyle()
                .append(topic.getRevealDate().toString()).append("\n")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply();
        description.setText(builder.build());
        contentView.loadData(topic.getContent(),"text/html",null);

        ListView comments = (ListView) findViewById(R.id.topicPageCommentList);
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

    public class TopicPagerAdapter extends PagerAdapter {
        private Context mContext;
        TopicPagerAdapter(Context context) {mContext = context;}

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            topicId = position;
            final Topic topic = topics.get(position);
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


            final Button likeButton = (Button)v.findViewById(R.id.like_button);
            if(topic.getLikedBy().contains(topic)) likeButton.setTextColor(getResources().getColor(R.color.mdtp_accent_color));
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   //TODO likes
                    if(!topic.getLikedBy().contains(topic)) {
                        new LikeTopicTask().execute(""+topic.getId(),"like");
                        likeButton.setTextColor(getResources().getColor(R.color.mdtp_accent_color));
                    }else {
                       new LikeTopicTask().execute(""+topic.getId(),"");
                        likeButton.setTextColor(getResources().getColor(R.color.white));
                    }
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
//TODO merge asynctasks
    public class FetchTopicsTask extends AsyncTask<Void, Void, com.group6boun451.learner.model.Topic[]> {
        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            username = preferences.getString(getString(R.string.user_name), " ");
            password = preferences.getString(getString(R.string.password), " ");
        }

        @Override
        protected Topic[] doInBackground(Void... params) {
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
                ResponseEntity<Topic[]> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), Topic[].class);
                // Log.d("response",response.getBody());
                return response.getBody();
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new Topic[0];
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new Topic[0];
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new Topic[0];
            }
        }

        @Override
        protected void onPostExecute(Topic[] result) {
            topics = new ArrayList(Arrays.asList(result));
            //TODO handle this
            viewpager.setAdapter(new TopicPagerAdapter(HomePage.this));
            viewpager2.setAdapter(new TopicPagerAdapter(HomePage.this));
            viewpager3.setAdapter(new TopicPagerAdapter(HomePage.this));
        }
    }
    public class EditTopicTask extends AsyncTask<String, Void, GenericResponse> {
        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            username = preferences.getString(getString(R.string.user_name), " ");
            password = preferences.getString(getString(R.string.password), " ");
        }

        @Override
        protected GenericResponse doInBackground(String... params) {
            final String url = getString(R.string.base_url) + "topic/edit/"+params[2];

            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("header",params[0]).queryParam("content",params[1]);
            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<GenericResponse> response = restTemplate.exchange(
                        builder.build().encode().toUri(),
                        HttpMethod.POST,
                        new HttpEntity<Object>(requestHeaders), GenericResponse.class);
                 Log.d("response",response.getBody().toString());
                return response.getBody();
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new GenericResponse();
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new GenericResponse();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new GenericResponse();
            }
        }

        @Override
        protected void onPostExecute(GenericResponse result) {
//            if(result.getError()==null)
//            Snackbar.make(findViewById(android.R.id.content),result.getMessage().toString(),Snackbar.LENGTH_SHORT).show();
        }
    }
    public class LikeTopicTask extends AsyncTask<String, Void, GenericResponse> {
        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            username = preferences.getString(getString(R.string.user_name), " ");
            password = preferences.getString(getString(R.string.password), " ");
        }

        @Override
        protected GenericResponse doInBackground(String... params) {
            String un =(params[1].equalsIgnoreCase("like"))?"":"un";
            final String url = getString(R.string.base_url) + "topic/"+un+"like/"+params[0];

            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<GenericResponse> response = restTemplate.exchange(
                        builder.build().encode().toUri(),
                        HttpMethod.POST,
                        new HttpEntity<Object>(requestHeaders), GenericResponse.class);
                Log.d("response",response.getBody().toString());
                return response.getBody();
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new GenericResponse();
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new GenericResponse();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new GenericResponse();
            }
        }

        @Override
        protected void onPostExecute(GenericResponse result) {
//            if(result.getError()==null)
//                Snackbar.make(findViewById(android.R.id.content),result.getMessage().toString(),Snackbar.LENGTH_SHORT).show();
        }
    }


}