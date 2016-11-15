package com.group6boun451.learner.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.group6boun451.learner.R;
import com.group6boun451.learner.model.GenericResponse;
import com.group6boun451.learner.model.Topic;
import com.group6boun451.learner.utils.Summernote;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTopicActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    protected static final String TAG = HomePage.class.getSimpleName();
    @BindView(R.id.summernote) Summernote summernote;
    @BindView(R.id.content_hamburger) View contentHamburger;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.topic_name_layout) TextInputLayout topicNameLayout;
    @BindView(R.id.topic_name_textEdit) EditText topicNameEditText;
    @BindView(R.id.image_button)Button imageButton;
    @BindView(R.id.reveal_date_button)Button revealDateButton;
    @BindView(R.id.revealTimeButton)Button revealTimeButton;
    private DatePickerDialog pickUpDatePicker;
    private TimePickerDialog pickUpTimePicker;
    private int date[];
    private GuillotineAnimation guillotineAnimation;
    private boolean isGuillotineOpened = false;
    Topic newTopic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        ButterKnife.bind(this);
        newTopic = new Topic();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        guillotineMenu.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewGroup) findViewById(android.R.id.content)).addView(guillotineMenu);
        topicNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validate();
            }
        });

        guillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(250)
                .setActionBarViewForAnimation(toolbar)
                .setGuillotineListener(new GuillotineListener() {
                    @Override
                    public void onGuillotineOpened() {
                        isGuillotineOpened = true;
                    }

                    @Override
                    public void onGuillotineClosed() {
                        isGuillotineOpened = false;

                    }
                })
                .setClosedOnStart(true)
                .build();
        summernote.setRequestCodeforFilepicker(5);
        date = new int[6];
        setDatePicker();
        setTimePicker();
        revealDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickUpDatePicker.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        revealTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickUpTimePicker.show(getFragmentManager(), "Timepickerdialog");            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pickUpDatePicker != null) pickUpDatePicker.setOnDateSetListener(this);
    }

    private void setDatePicker() {
        Calendar now = Calendar.getInstance();
        int year =  now.get(Calendar.YEAR),
                month = now.get(Calendar.MONTH),
                day = now.get(Calendar.DAY_OF_MONTH);

        pickUpDatePicker = DatePickerDialog.newInstance(AddTopicActivity.this,year,month,day);
        pickUpDatePicker.setTitle(getString(R.string.date));
        pickUpDatePicker.setMinDate(Calendar.getInstance());
        pickUpDatePicker.setMaxDate(new GregorianCalendar(year+1,month,day));
    }

    private void setTimePicker() {
        pickUpTimePicker = TimePickerDialog.newInstance(AddTopicActivity.this, 9, 0, android.text.format.DateFormat.is24HourFormat(getApplicationContext()));
        Timepoint[] tp = new Timepoint[48];
        for(int i=0;i<24;i++){
            tp[2*i]=new Timepoint(i,0,0);
            tp[2*i+1]=new Timepoint(i,30,0);
        }
        pickUpTimePicker.setSelectableTimes(tp);
        pickUpTimePicker.setTitle(getString(R.string.time));
    }

    public boolean validate() {
        if (topicNameEditText.getText().toString().trim().isEmpty()) {
            topicNameLayout.setError(getString(R.string.enter_name));
            return false;
        } else {
            topicNameLayout.setErrorEnabled(false);
        }
        return !(revealTimeButton.getTag() == null || revealDateButton.getTag() == null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        summernote.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
        if (isGuillotineOpened) {
            guillotineAnimation.close();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test, menu);//Second es your new xml.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_text:
                if (validate()) {
                    //TODO edit here
                    newTopic.setHeader(topicNameEditText.getText().toString());
                    newTopic.setContent(summernote.getText());
                    newTopic.setRevealDate(new Date(date[0],date[1],date[2],date[3],date[4],date[5]));
                    new AddContentTask().execute();
//TODO                    newTopic.setHeaderImage();
                }
                break;
        }
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        ++monthOfYear;
        Date dateInstance = new Date(year - 1900, monthOfYear - 1, dayOfMonth);
        String date = DateFormat.getDateInstance(DateFormat.SHORT).format(dateInstance);

        String dayOfMonthString = dayOfMonth < 10 ? "0"+dayOfMonth : ""+dayOfMonth;
        String monthOfYearString = monthOfYear < 10 ? "0"+monthOfYear : ""+monthOfYear;
        this.date[0]=year;
        this.date[1]=monthOfYear;
        this.date[2]=dayOfMonth;
        revealDateButton.setTag(dayOfMonthString+"/"+monthOfYearString+"/"+year);
        revealDateButton.setText(date);
//        setUpButton(dropOffDateButton,calendarIcon);
        if (revealTimeButton.getTag() == null) {
            pickUpTimePicker.show(getFragmentManager(), "Timepickerdialog");
        }


    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        //day month year params are unimportant
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(2016-1900,12-1,12,hourOfDay,minute));
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        revealTimeButton.setText(time);
//        setUpButton(dropOffTimeButton,timeIcon);
        revealTimeButton.setTag(hourString+":"+minuteString);
        this.date[3]=hourOfDay;
        this.date[4]=minute;
        this.date[5]=second;
    }


    public class AddContentTask extends AsyncTask<Void, Void, GenericResponse> {
        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            username = preferences.getString(getString(R.string.user_name), " ");
            password = preferences.getString(getString(R.string.password), " ");
        }

        @Override
        protected GenericResponse doInBackground(Void... params) {
            final String url = getString(R.string.base_url) + "topic/create";

            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

//            requestHeaders.add("header",newTopic.getHeader());
//            requestHeaders.add("headerImage",newTopic.getHeaderImage());
//            requestHeaders.add("content",newTopic.getContent());
//            requestHeaders.add("date",newTopic.getRevealDate());
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("header",newTopic.getHeader()).queryParam("content",newTopic.getContent());
            Log.d("content",newTopic.getContent());
            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<GenericResponse> response = restTemplate.exchange(
                        builder.build().encode().toUri(),
                        HttpMethod.POST,
                        new HttpEntity<Object>(requestHeaders), GenericResponse.class);
                // Log.d("response",response.getBody());
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
            Snackbar.make(findViewById(android.R.id.content),result.toString(),Snackbar.LENGTH_SHORT).show();
            finish();
        }
    }

}