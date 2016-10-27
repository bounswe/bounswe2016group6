package com.group6boun451.learner;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewpager;
    private ViewPager viewpager2;
    private ViewPager viewpager3;
    private List<Topic> topics;
    private TopicContainer tpc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tpc = new TopicContainer(this);
        topics = tpc.getTopics();

        viewpager = (ViewPager) findViewById(R.id.activity_topic_pager_view_pager);
        viewpager2 = (ViewPager) findViewById(R.id.activity_topic_pager_view_pager_popular);
        viewpager3 = (ViewPager) findViewById(R.id.activity_topic_pager_view_pager_mostrecent);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentManager fragmentManager2 = getSupportFragmentManager();
        FragmentManager fragmentManager3 = getSupportFragmentManager();

        viewpager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
               Topic topic = topics.get(position);
                return TopicHomeFragment.newInstance(topic.getId());
            }

            @Override
            public int getCount() {
               return topics.size();
            }
        });

        viewpager.setOffscreenPageLimit(2);

        viewpager.post(new Runnable() {
            @Override
            public void run() {
                viewpager.setCurrentItem(0);
            }
        });

        viewpager2.setAdapter((new FragmentStatePagerAdapter(fragmentManager2) {
            @Override
            public Fragment getItem(int position) {
                Topic topic = topics.get(position);
                return TopicHomeFragment.newInstance(topic.getId());
            }

            @Override
            public int getCount() {
                return topics.size();
            }
        }));

        viewpager2.setOffscreenPageLimit(2);

        viewpager2.post(new Runnable() {
            @Override
            public void run() {
                viewpager2.setCurrentItem(1);
            }
        });

        viewpager3.setAdapter((new FragmentStatePagerAdapter(fragmentManager3) {
            @Override
            public Fragment getItem(int position) {
                Topic topic = topics.get(position);
                return TopicHomeFragment.newInstance(topic.getId());
            }

            @Override
            public int getCount() {
                return topics.size();
            }
        }));

        viewpager3.setOffscreenPageLimit(2);

        viewpager3.post(new Runnable() {
            @Override
            public void run() {
                viewpager3.setCurrentItem(2);
            }
        });




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
