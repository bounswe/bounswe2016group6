package com.group6boun451.learner.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.group6boun451.learner.R;
import com.group6boun451.learner.model.User;
import com.group6boun451.learner.utils.GlideHelper;
import com.group6boun451.learner.utils.UserListAdapter;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.content_hamburger) View contentHamburger;
    @BindView(R.id.profile_imageView) ImageView profileImageView;
    @BindView(R.id.name_textView) TextView nameTextView;
    @BindView(R.id.mail_textView) TextView mailTextView;
    @BindView(R.id.profile_TabHost) TabHost tabHost;
    @BindView(R.id.topicList) ListView topicList;
    @BindView(R.id.teachersList) ListView teachersList;


    private boolean isGuillotineOpened = false;
    private GuillotineAnimation guillotineAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        guillotineMenu.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewGroup)findViewById(android.R.id.content)).addView(guillotineMenu);
        guillotineMenu.findViewById(R.id.feed_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    finish();
            }
        });
        guillotineMenu.findViewById(R.id.activity_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SearchActivity.class);
                intent.putExtra("tagName",getString(R.string.feed));
                intent.putExtra("query","0");
                intent.putExtra("type","recommendAll");
                startActivity(intent);
                guillotineAnimation.close();
                finish();
            }
        });
        guillotineMenu.findViewById(R.id.activity_log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString(getString(R.string.user_name), "");
                editor.putString(getString(R.string.password), "");
                editor.commit();
                guillotineAnimation.close();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
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
                    }

                    @Override
                    public void onGuillotineClosed() {
                        isGuillotineOpened=false;

                    }
                })
                .setClosedOnStart(true)
                .build();

        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("Tab One").setContent(R.id.interested_topics_tab).setIndicator("Progress"));
        tabHost.addTab(tabHost.newTabSpec("Tab Two").setContent(R.id.followed_teachers_tab).setIndicator("Followed"));


//"https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAJlAAAAJGIxNDQ3YzhiLTYyZjUtNDU2NS04ZTg3LWYxZjFlNjg3NmE5MQ.jpg"
        if(HomePage.user==null) HomePage.user = Task.getResult(
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .getString(getString(R.string.user), " "),User.class);

        if(HomePage.user!=null){
            GlideHelper.loadImage(profileImageView,HomePage.user.getPicture());
            mailTextView.setText(HomePage.user.getEmail());
            nameTextView.setText(HomePage.user.getFirstName()+" "+HomePage.user.getLastName());
        }

        final ListView teacherList = (ListView) findViewById(R.id.teachersList);
        new Task<String>(this, new Task.Callback() {
            @Override
            public void onResult(String resultString) {
                User[] result = Task.getResult(resultString,User[].class);
                List<User> users = new ArrayList(Arrays.asList(result));
                //TODO handle this
                UserListAdapter uladap = new UserListAdapter(ProfileActivity.this,users);
                teacherList.setAdapter(uladap);
            }
        }).execute(getString(R.string.base_url) + "user/following");



    }

}
