package com.group6boun451.learner.Activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.group6boun451.learner.R;
import com.group6boun451.learner.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {





    private class QuestionPagerAdapter extends PagerAdapter{
        Context mContext;
        LayoutInflater mLayoutInflater;

        public QuestionPagerAdapter(Context context){
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override public void destroyItem(ViewGroup collection, int position, Object view) {collection.removeView((View) view);}

        @Override
        public int getCount() {
            return mQuestions.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Question mQuestion = mQuestions.get(position);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup v = (ViewGroup) inflater.inflate(R.layout.quiz_question_view_pager_layout, container, false);
            ((TextView) v.findViewById(R.id.txtQuizQuestion)).setText(mQuestion.getQuestion());
            ((TextView) v.findViewById(R.id.txtQuizAnswerA)).setText(mQuestion.getAnswerA());
            ((TextView) v.findViewById(R.id.txtQuizAnswerB)).setText(mQuestion.getAnswerB());
            ((TextView) v.findViewById(R.id.txtQuizAnswerC)).setText(mQuestion.getAnswerC());
            ((TextView) v.findViewById(R.id.txtQuizQuestion)).setTypeface(chalkFont);
            ((TextView) v.findViewById(R.id.txtQuizAnswerA)).setTypeface(chalkFont);
            ((TextView) v.findViewById(R.id.txtQuizAnswerB)).setTypeface(chalkFont);
            ((TextView) v.findViewById(R.id.txtQuizAnswerC)).setTypeface(chalkFont);

            container.addView(v);
            return v;
        }

    }

    private Button leftArrow,rightArrow;
    private List<Question> mQuestions;
    private Typeface chalkFont;
    private TextView inWhichQuestion;
    private int numOfQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);
        chalkFont = Typeface.createFromAsset(getAssets(),"fonts/chalk_font.ttf");
        leftArrow = (Button) findViewById(R.id.btnArrowPrev);
        rightArrow = (Button) findViewById(R.id.btnArrowNext);
        inWhichQuestion = (TextView) findViewById(R.id.txtAtWhich);
        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            mQuestions =(ArrayList<Question>)((List)bundle.getParcelableArrayList("questions"));
        }else{

        }

        Question q1 = new Question();
        q1.setAnswerA("35");
        q1.setAnswerB("23");
        q1.setAnswerC("30");
        q1.setQuestion("What is the best age to have children?");

        Question q2 = new Question();
        q2.setAnswerA("asdasd");
        q2.setAnswerB("213123");
        q2.setAnswerC("123125dsfsd");
        q2.setQuestion("asdasdasd");

        Question q3 = new Question();
        q3.setAnswerA("asdasd");
        q3.setAnswerB("213123");
        q3.setAnswerC("123125dsfsd");
        q3.setQuestion("asdasdasd");
        mQuestions = new ArrayList<Question>();
        mQuestions.add(q1);
        mQuestions.add(q2);
        mQuestions.add(q3);
        numOfQuestion = mQuestions.size();
        ViewPager questionsPager = (ViewPager) findViewById(R.id.quiz_viewpager);


        questionsPager.setAdapter(new QuestionPagerAdapter(this));
        leftArrow.setVisibility(View.INVISIBLE);
        inWhichQuestion.setText("1 / "+numOfQuestion);
        questionsPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    rightArrow.setVisibility(View.VISIBLE);
                    leftArrow.setVisibility(View.INVISIBLE);
                }else if(position == mQuestions.size()-1){
                    leftArrow.setVisibility(View.VISIBLE);
                    rightArrow.setVisibility(View.INVISIBLE);
                }else{
                    rightArrow.setVisibility(View.VISIBLE);
                    leftArrow.setVisibility(View.VISIBLE);
                }

                inWhichQuestion.setText( (position+1) + " / " + numOfQuestion);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });





    }

}
