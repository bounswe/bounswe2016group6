package com.group6boun451.learner.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.group6boun451.learner.R;
import com.group6boun451.learner.model.GenericResponse;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

/**
 * Created by Ahmet Zorer on 12/5/2016.
 */
class Task<T> extends AsyncTask<T,Void,GenericResponse> {
    private String username;
    private String password;
    private Context context;
    private GenericResponse result;

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
    protected GenericResponse doInBackground(T... params) {
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
            ResponseEntity<GenericResponse> response = null;
            if(params[0] instanceof String) {
                response = restTemplate.exchange((String) params[0], HttpMethod.GET, new HttpEntity<Object>(requestHeaders), GenericResponse.class);
            } else{
                response = restTemplate.exchange((URI) params[0], HttpMethod.POST, new HttpEntity<Object>(requestHeaders), GenericResponse.class);
            }
            return response.getBody();
        }  catch (Exception e) {
            Log.e("Task", e.getLocalizedMessage(), e);
        }
        return null;
    }
    @Override
    protected void onPostExecute(GenericResponse result) {
        this.result = result;
        showResult(result);

    }

    public GenericResponse getResult() {
        return result;
    }
    private boolean showResult(GenericResponse result) {
        if(result==null) return false;
        if (result.getError() == null) {// display a notification to the user with the response information
            Snackbar.make(((Activity)context).findViewById(android.R.id.content),  result.getMessage(), Snackbar.LENGTH_SHORT).show();
            return true;
        } else {
            Snackbar.make(((Activity)context).findViewById(android.R.id.content),  result.getError(), Snackbar.LENGTH_SHORT).show();
            return false;
        }
    }

}