package com.group6boun451.learner.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.group6boun451.learner.R;
import com.programmingwizzard.bbcodejava.BBCode;
import com.programmingwizzard.bbcodejava.exceptions.BBCodeException;

import org.kefirsf.bb.BBProcessorFactory;
import org.kefirsf.bb.TextProcessor;

public class AddTopicActivity extends AppCompatActivity {
    TextView tw;
    EditText et;
    ImageButton boldButton, italicButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final BBCode code = new BBCode();

        tw = (TextView)findViewById(R.id.preview);
        et = (EditText)findViewById(R.id.editor);
        boldButton = (ImageButton)findViewById(R.id.action_bold);
        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    code.addBold(et.getText().toString());
                    et.setText("");
                    tw.setText(code.toString());
                }catch (BBCodeException e){
                    e.printStackTrace();
                }
            }
        });
        italicButton = (ImageButton)findViewById(R.id.action_Italic);
        italicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    code.addItalic(et.getText().toString());
                    et.setText("");
                    tw.setText(code.toString());
                }catch (BBCodeException e){
                    e.printStackTrace();
                }
            }
        });

        final TextProcessor processor = BBProcessorFactory.getInstance().create();
        assert "<b>text</b>".equals(processor.process("[b]text[/b]"));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });
    }

}
