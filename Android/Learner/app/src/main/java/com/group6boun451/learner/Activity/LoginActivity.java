package com.group6boun451.learner.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6boun451.learner.R;
import com.group6boun451.learner.model.User;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    protected static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.editTextUserName)
    EditText userNameEditText;
    @BindView(R.id.editTextUserPassword)
    EditText passwordEditText;
    @BindView(R.id.login_button)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getString(getString(R.string.user_name), "").length()>0){
            new LoginTask().execute();
            return;
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userNameEditText.setText("test@test.com");
        passwordEditText.setText("test");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {new LoginTask().execute();}});

    }
    //button register click
    public void btnRegisterClicked(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    public class LoginTask extends AsyncTask<Void,Void,User> {
        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            if(userNameEditText!=null){
                username = userNameEditText.getText().toString();
                password = passwordEditText.getText().toString();
            } else{
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                username = preferences.getString(LoginActivity.this.getString(R.string.user_name), " ");
                password = preferences.getString(LoginActivity.this.getString(R.string.password), " ");
            }

        }

        @Override
        protected User doInBackground(Void... params) {
            final String url = getString(R.string.base_url) + "userprofile";
            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                // Make the network request
                ResponseEntity<User> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        new HttpEntity<Object>(requestHeaders), User.class);
                return response.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(User result) {
            if(result == null){
                Snackbar.make(findViewById(android.R.id.content),"Sorry, wrong credentials",Snackbar.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            //TODO check if those are empty
            editor.putString(getString(R.string.user_name), username);
            editor.putString(getString(R.string.password),password);
            editor.commit();
            Intent intent = new Intent(LoginActivity.this, HomePage.class);
            String userString = null;
            try {
                userString = new ObjectMapper().writeValueAsString(result);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            intent.putExtra("user",userString);
            startActivity(intent);
            finish();
        }
    }
}
