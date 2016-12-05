package com.group6boun451.learner.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.group6boun451.learner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.btnSelectRole)
    Button btnSelectRole;
    private String whatIsRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        ButterKnife.bind(this);




    }

    public void btnSelectRoleClicked(View view){
        final String[] items = new String[] {"Student","Teacher"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        new AlertDialog.Builder(this)
                .setTitle("Select Role")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        whatIsRole = items[which];
                        btnSelectRole.setText(whatIsRole);


                        dialog.dismiss();
                    }
                }).create().show();
    }
}
