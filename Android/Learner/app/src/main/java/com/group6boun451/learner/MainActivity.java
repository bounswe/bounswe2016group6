package com.group6boun451.learner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = MainActivity.class.getSimpleName();

    @BindView( R.id.editTextUserName) EditText userNameEditText;
    @BindView( R.id.editTextUserPassword) EditText passwordEditText;
    @BindView( R.id.login_button) Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userNameEditText.setText("test@test.com");
        passwordEditText.setText("test");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(MainActivity.this,UnfoldableDetailsActivity.class));
                startActivity(new Intent(MainActivity.this,HomePage.class));
              //  new FetchSecuredResourceTask().execute();
            }
        });

    }

    private class FetchSecuredResourceTask extends AsyncTask<Void, Void, Greeting> {
        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            this.username = userNameEditText.getText().toString();
            this.password = passwordEditText.getText().toString();
        }

        @Override
        protected Greeting doInBackground(Void... params) {
            final String url = getString(R.string.base_url) + "topic/yeter/";

            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            Log.d(TAG+" username",username +", "+password);
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<Greeting> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Object>(requestHeaders), Greeting.class);
               // Log.d("response",response.getBody());
                return response.getBody();
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new Greeting(0,"a");
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new Greeting(0,"b");
            } catch (Exception e){
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new Greeting(0,"c");
            }
        }

        @Override
        protected void onPostExecute(Greeting result) {
            Log.d(TAG+"result",result.getContent());
        }
    }
}
