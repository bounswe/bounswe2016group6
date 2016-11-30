package com.group6boun451.learner.Activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.group6boun451.learner.R;
import com.group6boun451.learner.model.Topic;
import com.group6boun451.learner.utils.SearchResultsListAdapter;

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

import java.util.Arrays;
import java.util.Collections;


/** Activity for location search page. Location search activity is used for retrieving the data
 * and returning it to the caller.
 */
public class LocationSearchActivity extends AppCompatActivity {
    protected static final String TAG = HomePage.class.getSimpleName();
    FetchTopicsTask f;
    private FloatingSearchView mSearchView;

    private SearchResultsListAdapter mSearchResultsAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
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
                    f = new FetchTopicsTask();
                    f.execute(newQuery);
                }
            }
        });

        mSearchResultsAdapter.setItemsOnClickListener(new SearchResultsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(Topic location) {
//                Intent intent = new Intent(LocationSearchActivity.this, HomePageActivity.class);
//                intent.putExtra(Constants.LOCATION_TYPE, locationType);
//                intent.putExtra(Constants.LOCATION, location.getName());
//                intent.putExtra(Constants.LOCATION_ID, location.getId());
//                setResult(RESULT_OK, intent);
//                finish();
            }
        });
        mSearchView.setSearchFocused(true);
    }

    public class FetchTopicsTask extends AsyncTask<String, Void, Topic[]> {
        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            username = preferences.getString(getString(R.string.user_name), " ");
            password = preferences.getString(getString(R.string.password), " ");
        }

        @Override
        protected Topic[] doInBackground(String... params) {
            final String url = getString(R.string.base_url) + "search/keyword";

            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            Log.d(TAG + " username", username + ", " + password);
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("keyword",params[0]);

            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<Topic[]> response = restTemplate.exchange(builder.build().encode().toUri(),
                        HttpMethod.GET, new HttpEntity<Object>(requestHeaders), Topic[].class);
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
            if (result!=null) {
                mSearchResultsAdapter.swapData(Arrays.asList(result));
            }
            mSearchView.hideProgress();

//            topics = new ArrayList(Arrays.asList(result));
//            //TODO handle this
//            viewpager.setAdapter(new HomePage.TopicPagerAdapter(HomePage.this));
//            viewpager2.setAdapter(new HomePage.TopicPagerAdapter(HomePage.this));
//            viewpager3.setAdapter(new HomePage.TopicPagerAdapter(HomePage.this));
        }
    }


}
