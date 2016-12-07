package com.group6boun451.learner.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.group6boun451.learner.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.btnSelectRole)
    Button btnSelectRole;
    @BindView(R.id.usrFirstName)
    EditText usrFirstName;
    @BindView(R.id.usrSurname) EditText usrSurname;
    @BindView(R.id.usrEmail) EditText usrEmail;
    @BindView(R.id.usrPass1) EditText password;
    @BindView(R.id.usrPass2) EditText password2;
    @BindView(R.id.btnCreateAccount) Button btnCreateAccount;
    private List<EditText> mItems;
    private String whatIsRole = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        ButterKnife.bind(this);
        mItems = new ArrayList<EditText>();
        fillList();


    }

    private void fillList(){
        mItems.add(usrFirstName);
        mItems.add(usrSurname);
        mItems.add(usrEmail);
        mItems.add(password);
        mItems.add(password2);
    }

    private boolean checkPasswords(){
       return password.getText().toString().equals(password2.getText().toString());
    }

    private boolean checkFields(){
        for(EditText tmp : mItems){
            if(tmp.getText().toString().length() == 0){
                return false;
            }
        }
        if(whatIsRole == null){
            return false;
        }
        return true;
    }

    public void btnCreateAccountClicked (View view){
        if(!checkFields()){
            Toast.makeText(this,"All fields must be filled",Toast.LENGTH_SHORT).show();
            return;
        }else if(!checkPasswords()){
            Toast.makeText(this,"Passwords are not same",Toast.LENGTH_SHORT).show();
            return;
        }else{

        }

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
