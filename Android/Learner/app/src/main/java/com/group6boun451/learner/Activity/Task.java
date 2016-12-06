package com.group6boun451.learner.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6boun451.learner.R;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

/**
 * Created by Ahmet Zorer on 12/5/2016.
 *
 */
public class Task<T> extends AsyncTask<T,Void,String> {
    private String username;
    private String password;
    private Context context;
    Callback callback;
    public Task(Context context, Callback callback){
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        username = preferences.getString(context.getString(R.string.user_name), " ");
        password = preferences.getString(context.getString(R.string.password), " ");
    }

    @Override
    protected String doInBackground(T... params) {
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
            ResponseEntity<String> response = null;
            if(params.length>1) {
                Log.d("call", (String) params[0]);
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                response = restTemplate.exchange((String) params[0], HttpMethod.POST, new HttpEntity<>( params[1], requestHeaders), String.class);
            } else if(params[0] instanceof String) {
                Log.d("call", (String) params[0]);
                response = restTemplate.exchange((String) params[0], HttpMethod.GET, new HttpEntity<Object>(requestHeaders), String.class);
            } else {
                Log.d("call", String.valueOf(params[0]));
                response = restTemplate.exchange((URI) params[0], HttpMethod.POST, new HttpEntity<Object>(requestHeaders), String.class);
            }
            return response.getBody();
        }  catch (Exception e) {
            Log.e("Task", e.getLocalizedMessage(), e);
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        callback.onResult(result);
    }
    public static <T> T getResult(String resultString, Class<T> aClass) {
        T result = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(resultString, aClass);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public interface Callback{
        void onResult(String result);
    }
}

