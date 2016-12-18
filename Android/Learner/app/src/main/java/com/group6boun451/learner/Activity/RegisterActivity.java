package com.group6boun451.learner.activity;

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

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.btnSelectRole) Button btnSelectRole;
    @BindViews({R.id.usrFirstName,R.id.usrSurname,R.id.usrEmail,R.id.usrPass1,R.id.usrPass2}) List<EditText> mItems;
    @BindView(R.id.btnCreateAccount) Button btnCreateAccount;

    private String whatIsRole = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        ButterKnife.bind(this);

    }

    /**
     * Onclick method for check password button.
     * @return
     */
    private boolean checkPasswords(){
       return mItems.get(3).getText().toString().equals(mItems.get(4).getText().toString());
    }

    /**
     * Returns if none of the fields are empty.
     * @return
     */
    private boolean checkFields(){
        for(EditText tmp : mItems){
            if(tmp.getText().toString().length() == 0){
                return false;
            }
        }
        return whatIsRole != null;
    }

    /**
     * Creates acoount when create account button is clicked.
     * @param view
     */
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

    /**
     * Choose role when select role button is clicked.
     * @param view
     */
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
