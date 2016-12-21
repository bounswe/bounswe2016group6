package com.group6boun451.learner.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.group6boun451.learner.R;
import com.group6boun451.learner.utils.GlideHelper;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
    @BindViews({R.id.usrFirstName,R.id.usrSurname,R.id.usrEmail,R.id.usrPass1,R.id.usrPass2}) List<EditText> mItems;
    @BindView(R.id.btnCreateAccount) Button btnCreateAccount;

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
        return true;
    }

    /**
     * Creates acoount when create account button is clicked.
     * @param view
     */
    public void btnCreateAccountClicked (View view){
        if(!checkFields()){
            Snackbar.make(findViewById(android.R.id.content),"All fields must be filled",Snackbar.LENGTH_SHORT).show();
            return;
        }else if(!checkPasswords()){
            Snackbar.make(findViewById(android.R.id.content),"Passwords are not same",Snackbar.LENGTH_SHORT).show();
            return;
        }else{
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getString(R.string.base_url) + "user/registration")
                        .queryParam("firstName",mItems.get(0).getText().toString())
                        .queryParam("lastName",mItems.get(1).getText().toString())
                        .queryParam("password",mItems.get(3).getText().toString())
                        .queryParam("matchingPassword",mItems.get(4).getText().toString())
                        .queryParam("email", mItems.get(2).getText().toString());

            new Task<URI>(RegisterActivity.this, new Task.Callback() {
                @Override
                public void onResult(String resultString) {
                    if(!GlideHelper.showResult(RegisterActivity.this,resultString))return;
                    finish();
                }
            }).execute(builder.build().encode().toUri());
        }

    }

}
