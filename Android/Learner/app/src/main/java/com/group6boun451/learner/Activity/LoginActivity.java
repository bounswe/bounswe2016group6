package com.group6boun451.learner.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.group6boun451.learner.R;

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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userNameEditText.setText("test@test.com");
        passwordEditText.setText("test");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                //TODO check if those are empty
                editor.putString(getString(R.string.user_name), userNameEditText.getText().toString());
                editor.putString(getString(R.string.password), passwordEditText.getText().toString());
                editor.commit();
                startActivity(new Intent(LoginActivity.this, HomePage.class));
            }
        });

    }
}
