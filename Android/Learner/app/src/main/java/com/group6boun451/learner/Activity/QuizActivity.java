package com.group6boun451.learner.Activity;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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

import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {




    //adapter for pager
    private class QuestionPagerAdapter extends PagerAdapter{
        Context mContext;

        public QuestionPagerAdapter(Context context){
            mContext = context;

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
        public Object instantiateItem(ViewGroup container, final int position) {
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

            final Button choiceA = (Button) v.findViewById(R.id.btnQuizChoiceA);
            final Button choiceB = (Button) v.findViewById(R.id.btnQuizChoiceB);
            final Button choiceC = (Button) v.findViewById(R.id.btnQuizChoiceC);
            final Drawable drw = QuizActivity.this.getResources().getDrawable(R.drawable.quiz_marked);

            int tmp = answers[position];
            if(tmp != -1){
                if(tmp == 0){
                    choiceA.setBackground(drw);
                }else if(tmp == 1){
                    choiceB.setBackground(drw);
                }else{
                    choiceC.setBackground(drw);
                }
            }

            choiceA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choiceA.setBackground(drw);
                    choiceB.setBackgroundResource(0);
                    choiceC.setBackgroundResource(0);
                    answers[position] = 0;
                }
            });

            choiceB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choiceB.setBackground(drw);
                    choiceA.setBackgroundResource(0);
                    choiceC.setBackgroundResource(0);
                    answers[position] = 1;
                }
            });

            choiceC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choiceC.setBackground(drw);
                    choiceB.setBackgroundResource(0);
                    choiceA.setBackgroundResource(0);

                    answers[position] = 2;
                }
            });

            container.addView(v);
            return v;
        }

    }

    private Button leftArrow,rightArrow,finishQuiz;
    private List<Question> mQuestions;
    private Typeface chalkFont;
    private TextView inWhichQuestion;
    private int numOfQuestion;
    private int currentQuestion;
    private int[] answers;

    private void init(){
        chalkFont = Typeface.createFromAsset(getAssets(),"fonts/chalk_font.ttf");
        leftArrow = (Button) findViewById(R.id.btnArrowPrev);
        rightArrow = (Button) findViewById(R.id.btnArrowNext);
        inWhichQuestion = (TextView) findViewById(R.id.txtAtWhich);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);
        init();

        mQuestions = HomePage.topics.get(HomePage.topicId-1).getQuestions();



        numOfQuestion = mQuestions.size();
        final ViewPager questionsPager = (ViewPager) findViewById(R.id.quiz_viewpager);


        questionsPager.setAdapter(new QuestionPagerAdapter(this));
        leftArrow.setVisibility(View.INVISIBLE);
        inWhichQuestion.setText("1 / "+numOfQuestion);
        currentQuestion = 0;
        answers = new int[numOfQuestion];
        Arrays.fill(answers,-1);
        questionsPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    rightArrow.setVisibility(View.VISIBLE);
                    leftArrow.setVisibility(View.INVISIBLE);
                }else if(position == numOfQuestion-1){
                    leftArrow.setVisibility(View.VISIBLE);
                    rightArrow.setVisibility(View.INVISIBLE);
                }else{
                    rightArrow.setVisibility(View.VISIBLE);
                    leftArrow.setVisibility(View.VISIBLE);
                }
                currentQuestion = position;
                inWhichQuestion.setText( (position+1) + " / " + numOfQuestion);

            }
            @Override public void onPageScrollStateChanged(int state) {}
        });
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentQuestion>0){questionsPager.setCurrentItem(currentQuestion-1);}
            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentQuestion<numOfQuestion-1){questionsPager.setCurrentItem(currentQuestion+1);
                }else{//finish
                }
            }
        });

    }

}
