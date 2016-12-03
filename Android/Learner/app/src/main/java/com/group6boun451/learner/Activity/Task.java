package com.group6boun451.learner.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.group6boun451.learner.R;

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

/**
 * Created by Ahmet Zorer on 12/5/2016.
 */
public class Task<T,K> extends AsyncTask<T,Void,K> {
    private String username;
    private String password;
    private Context context;

    public Task(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        username = preferences.getString(context.getString(R.string.user_name), " ");
        password = preferences.getString(context.getString(R.string.password), " ");
    }

    @Override
    protected K doInBackground(T... params) {
        // Populate the HTTP Basic Authentitcation header with the username and password
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Class<K> k = null;
        try {
            // Make the network request
            ResponseEntity<K> response = restTemplate.exchange(
                    (String)params[0],
                    HttpMethod.GET,
                    new HttpEntity<Object>(requestHeaders), k);
            return response.getBody();
        }  catch (Exception e) {
            Log.e("Task", e.getLocalizedMessage(), e);
        }
        return null;
    }
    @Override
    protected void onPostExecute(K result) {
//        showResult((GenericResponse) result);
    }
}