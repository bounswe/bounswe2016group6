package com.group6boun451.learner.activity;

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
 * Generic class for all the background tasks.
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

    /**
     * Gets username and password for authentication.
     */
    @Override
    protected void onPreExecute() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        username = preferences.getString(context.getString(R.string.user_name), " ");
        password = preferences.getString(context.getString(R.string.password), " ");
    }

    /**
     * Makes call for server tasks and returns the result of the call.
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(T... params) {
        // Populate the HTTP Basic Authentitcation header with the username and password
        HttpHeaders requestHeaders = new HttpHeaders();
        if(params.length<3) {
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            requestHeaders.setAuthorization(authHeader);
        }
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            // Make the network request
            ResponseEntity<String> response = null;
            if(params.length==2) {
                Log.d("calls", (String) params[0]);
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

    /**
     * Callback method is called. The callback method changes in accordance with the place that the task is used.
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {
        callback.onResult(result);
    }

    /**
     * Maps the given string to an object that the type is given.
     * @param resultString
     * @param aClass
     * @param <T>
     * @return
     */
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

    /**
     *Callback interface for task class.
     */
    public interface Callback{
        void onResult(String result);
    }
}

