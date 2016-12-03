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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.doodle.android.chips.ChipsView;
import com.doodle.android.chips.model.Contact;
import com.group6boun451.learner.R;
import com.group6boun451.learner.model.GenericResponse;
import com.group6boun451.learner.model.Tag;
import com.group6boun451.learner.model.Topic;
import com.group6boun451.learner.utils.GlideHelper;
import com.group6boun451.learner.widget.Summernote;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddTopicActivity extends AppCompatActivity {//implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    protected static final String TAG = HomePage.class.getSimpleName();
    private static final int PICK_IMAGE = 2;
    private static final int EDITOR = 3;
    Task<URI> f;

    private RecyclerView mContacts;
    private TagsAdapter mAdapter;
    private ChipsView mChipsView;

    @BindView(R.id.summernote)
    Summernote summernote;
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
        tabHost.addTab(tabHost.newTabSpec("Tab Two").setContent(R.id.tag_layout).setIndicator("Tags"));
        tabHost.addTab(tabHost.newTabSpec("Tab Three").setContent(R.id.editor_layout).setIndicator("Content"));


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

                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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



        mContacts = (RecyclerView) findViewById(R.id.rv_contacts);
        mContacts.setLayoutManager(new LinearLayoutManager(AddTopicActivity.this));
        mAdapter = new TagsAdapter();
        mContacts.setAdapter(mAdapter);

        mChipsView = (ChipsView) findViewById(R.id.cv_contacts);

        // change EditText config
        mChipsView.getEditText().setCursorVisible(true);

        mChipsView.setChipsValidator(new ChipsView.ChipValidator() {
            @Override
            public boolean isValid(Contact contact) {
                return !contact.getDisplayName().equals("asd@qwe.de");
            }
        });

        mChipsView.setChipsListener(new ChipsView.ChipsListener() {
            @Override
            public void onChipAdded(ChipsView.Chip chip) {
                for (ChipsView.Chip chipItem : mChipsView.getChips()) {
                    Log.d("ChipList", "chip: " + chipItem.toString());
                }
            }

            @Override
            public void onChipDeleted(ChipsView.Chip chip) {

            }

            @Override
            public void onTextChanged(CharSequence text) {
                if (f != null) {
                    f.cancel(true);
                }
                if ( text.equals("")) {
                    mAdapter.swapData(Collections.<Tag>emptyList());
//                    mSearchView.hideProgress();
                } else if(text.length()>2) {
//                    mSearchView.showProgress();
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getString(R.string.base_url) + "tag/suggest")
                            .queryParam("query",text.toString());

                    f = new Task<>(AddTopicActivity.this, new Callback() {
                        @Override
                        public void onResult(String resultString) {
                            Tag[] result =  Task.getResult(resultString, Tag[].class);
                            if (result!=null) {
                                mAdapter.swapData(Arrays.asList(result));
                            }
                        }
                    });
                    f.execute(builder.build().encode().toUri());
                }
            }
        });

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
                //TODO Display an error
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

    public void nextButton(View view) {tabHost.setCurrentTab(tabHost.getCurrentTab()+1);}

    public void cancelButton(View view) {
        finish();
    }

    public void backButton(View view) {tabHost.setCurrentTab(tabHost.getCurrentTab()-1);}

    public void finishButton(View view) {
        if (validate()) {
            newTopic.setHeader(topicNameEditText.getText().toString());
            newTopic.setContent(summernote.getText());
            newTopic.setRevealDate(new Date(date[0],date[1],date[2],date[3],date[4],date[5]));
            new AddTopicTask().execute();
        }
    }

    public class TagsAdapter extends RecyclerView.Adapter<CheckableContactViewHolder> {

        private List<Tag> mDataSet = new ArrayList<>();

        public TagsAdapter() {}
        public void swapData(List<Tag> mNewDataSet) {
            mDataSet = mNewDataSet;
            notifyDataSetChanged();
        }
        @Override
        public CheckableContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(AddTopicActivity.this).inflate(R.layout.item_checkable_contact, parent, false);
            return new CheckableContactViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CheckableContactViewHolder holder, int position) {
            holder.name.setText(mDataSet.get(position).getName());
            holder.description.setText(mDataSet.get(position).getContext());
            holder.tagId = mDataSet.get(position).getId();
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }

    public class CheckableContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView name,description;
        public final CheckBox selection;
        public Long tagId;

        public CheckableContactViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_contact_name);
            description = (TextView) itemView.findViewById(R.id.tag_content);
            selection = (CheckBox) itemView.findViewById(R.id.cb_contact_selection);
            selection.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selection.performClick();
                }
            });
        }

        @Override
        public void onClick(View v) {
            String tagName = name.getText().toString();
            Contact contact = new Contact(tagName, description.getText().toString(), tagId+"", tagName, null);

            if (selection.isChecked()) {
                mChipsView.addChip(tagName, "", contact);
            } else {
                mChipsView.removeChipBy(contact);
            }
        }
    }

    // ***************************************
    // Private methods
    // ***************************************
    private boolean showResult(GenericResponse result) {
        if (result.getError() == null) {// display a notification to the user with the response information
            finish();
            return true;
        } else {
            Snackbar.make(findViewById(android.R.id.content),  result.getError(), Snackbar.LENGTH_SHORT).show();
            return false;
        }
    }

    // ***************************************
    // Private classes
    // ***************************************
    private class AddTopicTask extends AsyncTask<Void, Void, GenericResponse> {

        private MultiValueMap<String, Object> formData;
        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
//            showLoadingProgressDialog();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            username = preferences.getString(getString(R.string.user_name), " ");
            password = preferences.getString(getString(R.string.password), " ");

            // populate the data to post
            formData = new LinkedMultiValueMap<String, Object>();
            formData.add("header",newTopic.getHeader());
            formData.add("content",newTopic.getContent());
            formData.add("image", new FileSystemResource(newTopic.getHeaderImage()));
        }

        @Override
        protected GenericResponse doInBackground(Void... params) {
            try {
                // The URL for making the POST request
                final String url = getString(R.string.base_url) + "topic/create";
                // Populate the HTTP Basic Authentitcation header with the username and password
                HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setAuthorization(authHeader);

                // Sending multipart/form-data
                requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

                // Populate the MultiValueMap being serialized and headers in an HttpEntity object to use for the request
                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, requestHeaders);

                // Create a new RestTemplate instance
                RestTemplate restTemplate = new RestTemplate(true);

                // Make the network request, posting the message, expecting a String in response from the server
                ResponseEntity<GenericResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, GenericResponse.class);

                // Return the response body to display to the user
                return response.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(GenericResponse result) {
            if(!showResult(result))return;
//            dismissProgressDialog();
            Tag[] tags = new Tag[mChipsView.getChips().size()];
            int i = 0;
            for(ChipsView.Chip c: mChipsView.getChips()){
                Tag t = new Tag();
                t.setName(c.getContact().getFirstName());
                t.setContext(c.getContact().getLastName());
                if(!c.getContact().getDisplayName().equalsIgnoreCase("null"))t.setId(Long.parseLong(c.getContact().getDisplayName()));
                tags[i++] = t;
            }

            if (tags.length== 0) return;
            new Task<>(AddTopicActivity.this, new Callback() {
                @Override
                public void onResult(String result) {showResult(Task.getResult(result,GenericResponse.class));}
            })
                    .execute(getString(R.string.base_url) + "tag/"+result.getMessage()+"/add",tags);
        }

    }
}