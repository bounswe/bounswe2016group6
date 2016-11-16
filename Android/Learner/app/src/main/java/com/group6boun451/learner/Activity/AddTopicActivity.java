package com.group6boun451.learner.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.group6boun451.learner.R;
import com.group6boun451.learner.model.GenericResponse;
import com.group6boun451.learner.model.Topic;
import com.group6boun451.learner.utils.GlideHelper;
import com.group6boun451.learner.utils.Summernote;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTopicActivity extends AppCompatActivity {//implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    protected static final String TAG = HomePage.class.getSimpleName();
    private static final int PICK_IMAGE = 2;
    private static final int EDITOR = 3;
    @BindView(R.id.summernote) Summernote summernote;
    @BindView(R.id.content_hamburger) View contentHamburger;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.topic_name_layout) TextInputLayout topicNameLayout;
    @BindView(R.id.topic_name_textEdit) EditText topicNameEditText;
    @BindView(R.id.topic_TabHost) TabHost tabHost;
    @BindView(R.id.content_image_button) ImageButton contentImageButton;


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

        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("Tab One").setContent(R.id.topic_layout).setIndicator("Topic"));
        tabHost.addTab(tabHost.newTabSpec("Tab Two").setContent(R.id.editor_layout).setIndicator("Content"));


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

// when you click this demo button
        contentImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
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
        summernote.setRequestCodeforFilepicker(EDITOR);
        date = new int[6];
//        setDatePicker();
//        setTimePicker();
      }


//    private void setDatePicker() {
//        Calendar now = Calendar.getInstance();
//        int year =  now.get(Calendar.YEAR),
//                month = now.get(Calendar.MONTH),
//                day = now.get(Calendar.DAY_OF_MONTH);
//
//        pickUpDatePicker = DatePickerDialog.newInstance(AddTopicActivity.this,year,month,day);
//        pickUpDatePicker.setTitle(getString(R.string.date));
//        pickUpDatePicker.setMinDate(Calendar.getInstance());
//        pickUpDatePicker.setMaxDate(new GregorianCalendar(year+1,month,day));
//    }
//
//    private void setTimePicker() {
//        pickUpTimePicker = TimePickerDialog.newInstance(AddTopicActivity.this, 9, 0, android.text.format.DateFormat.is24HourFormat(getApplicationContext()));
//        Timepoint[] tp = new Timepoint[48];
//        for(int i=0;i<24;i++){
//            tp[2*i]=new Timepoint(i,0,0);
//            tp[2*i+1]=new Timepoint(i,30,0);
//        }
//        pickUpTimePicker.setSelectableTimes(tp);
//        pickUpTimePicker.setTitle(getString(R.string.next_button));
//    }

    public boolean validate() {
        if (topicNameEditText.getText().toString().trim().isEmpty()) {
            topicNameLayout.setError(getString(R.string.enter_name));
            return false;
        } else {
            topicNameLayout.setErrorEnabled(false);
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            GlideHelper.loadImage(contentImageButton,data.getData().toString());
            newTopic.setHeaderImage(getPath(data.getData()));
        } else if(requestCode==EDITOR && resultCode == Activity.RESULT_OK){
            summernote.onActivityResult(requestCode, resultCode, data);
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    public void onBackPressed() {
        if (isGuillotineOpened) {
            guillotineAnimation.close();
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        ++monthOfYear;
//        Date dateInstance = new Date(year - 1900, monthOfYear - 1, dayOfMonth);
//        String date = DateFormat.getDateInstance(DateFormat.SHORT).format(dateInstance);
//
//        String dayOfMonthString = dayOfMonth < 10 ? "0"+dayOfMonth : ""+dayOfMonth;
//        String monthOfYearString = monthOfYear < 10 ? "0"+monthOfYear : ""+monthOfYear;
//        this.date[0]=year;
//        this.date[1]=monthOfYear;
//        this.date[2]=dayOfMonth;
//        revealDateButton.setTag(dayOfMonthString+"/"+monthOfYearString+"/"+year);
//        revealDateButton.setText(date);
////        setUpButton(dropOffDateButton,calendarIcon);
//        if (revealTimeButton.getTag() == null) {
//            pickUpTimePicker.show(getFragmentManager(), "Timepickerdialog");
//        }
//
//
//    }
//
//    @Override
//    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
//        //day month year params are unimportant
//        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(2016-1900,12-1,12,hourOfDay,minute));
//        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
//        String minuteString = minute < 10 ? "0"+minute : ""+minute;
//        revealTimeButton.setText(time);
////        setUpButton(dropOffTimeButton,timeIcon);
//        revealTimeButton.setTag(hourString+":"+minuteString);
//        this.date[3]=hourOfDay;
//        this.date[4]=minute;
//        this.date[5]=second;
//    }

    public void nextButton(View view) {
        tabHost.setCurrentTab(1);
    }

    public void cancelButton(View view) {
        finish();
    }

    public void backButton(View view) {
        tabHost.setCurrentTab(0);
    }

    public void finishButton(View view) {
        if (validate()) {
            //TODO edit here
            newTopic.setHeader(topicNameEditText.getText().toString());
            newTopic.setContent(summernote.getText());
            newTopic.setRevealDate(new Date(date[0],date[1],date[2],date[3],date[4],date[5]));
            new AddContentTask().execute();
//TODO                    newTopic.setHeaderImage();
        }
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
            requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

            FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
            formHttpMessageConverter.setCharset(Charset.forName("UTF8"));

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add( formHttpMessageConverter );
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());


            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
            map.add("header",newTopic.getHeader());
            map.add("content",newTopic.getContent());
            map.add("file", new FileSystemResource(newTopic.getHeaderImage()));
            Log.d("content",newTopic.getContent());

            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<GenericResponse> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        new HttpEntity<MultiValueMap<String, Object>>(map, requestHeaders), GenericResponse.class);

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