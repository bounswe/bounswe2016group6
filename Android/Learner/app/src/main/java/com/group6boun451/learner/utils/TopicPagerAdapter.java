package com.group6boun451.learner.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.android.chips.ChipsView;
import com.doodle.android.chips.model.Contact;
import com.group6boun451.learner.Activity.HomePage;
import com.group6boun451.learner.Activity.Task;
import com.group6boun451.learner.R;
import com.group6boun451.learner.model.GenericResponse;
import com.group6boun451.learner.model.Tag;
import com.group6boun451.learner.model.Topic;
import com.group6boun451.learner.model.User;
import com.group6boun451.learner.widget.CanaroTextView;

import java.util.List;

import static com.group6boun451.learner.utils.GlideHelper.getReadableDateFromDate;

/**
 * Created by Ahmet Zorer on 12/6/2016.
 */
public class TopicPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<Topic> topics;

    public TopicPagerAdapter(Context context) {}

    public TopicPagerAdapter(Context context, List<Topic> topics) {
        mContext = context;
        this.topics = topics;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        final Topic topic = topics.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.topic_item_home, collection, false);
        ((TextView) v.findViewById(R.id.textTopicTitle)).setText(topic.getHeader());
        ((CanaroTextView) v.findViewById(R.id.textAuthor)).setText(topic.getOwner().getFirstName());
        ((CanaroTextView) v.findViewById(R.id.textDate)).setText(getReadableDateFromDate(mContext,topic.getRevealDate()));

        ChipsView mChipsView = (ChipsView) v.findViewById(R.id.cv_contacts);
        // change EditText config
        mChipsView.getEditText().setVisibility(View.GONE);
        int k = mChipsView.getChips().size();
        Contact c = new Contact(null,null,null,"",null);
        for(int i =0;i<k;i++){mChipsView.removeChipBy(c);}
        for(Tag t : topic.getTags()){
            String tagName = t.getName();
            Contact contact = new Contact(tagName, t.getContext(), t.getId()+"", tagName, null);
            mChipsView.addChip(tagName, null, contact,true);
        }


        final ImageView img = (ImageView) v.findViewById(R.id.imageTopic);
        GlideHelper.loadImage(mContext,img, topic);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomePage) v.getContext()).openDetails(v.findViewById(R.id.card_view2), topic);
                //check is there quiz
                HomePage.isThereQuiz = topic.getQuestions().size() != 0;

            }
        });

        final ImageView imgProfile = (ImageView) v.findViewById(R.id.imgTopicPageUserImage);
        imgProfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_menu_profile));


        final Button likeButton = (Button)v.findViewById(R.id.like_button);
        if(isLiked(topic.getLikedBy())) likeButton.setTextColor(mContext.getResources().getColor(R.color.selected_item_color));
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task<String> likeTask = new Task<>(mContext, new Task.Callback() {
                    @Override
                    public void onResult(String result) {showResult(result);}
                });
                if(likeButton.getCurrentTextColor()!=mContext.getResources().getColor(R.color.selected_item_color)) {
                    likeTask.execute(mContext.getString(R.string.base_url) + "topic/like/"+topic.getId());
                    likeButton.setTextColor(mContext.getResources().getColor(R.color.selected_item_color));
                }else {
                    likeTask.execute(mContext.getString(R.string.base_url) + "topic/unlike/"+topic.getId());
                    likeButton.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            }
        });

        collection.addView(v);
        return v;
    }

    private boolean isLiked(List<User> likedBy) {
        for (User u:likedBy){if(u.getEmail().equalsIgnoreCase(HomePage.username))return true;} return false;
    }
    private boolean showResult(String resultString) {
        GenericResponse result = Task.getResult(resultString,GenericResponse.class);
        if(result==null) {
            return false;
        }else if (result.getError() == null) {// display a notification to the user with the response information
            Snackbar.make(((Activity)mContext).findViewById(android.R.id.content),  result.getMessage(), Snackbar.LENGTH_SHORT).show();
            return true;
        } else {
            Snackbar.make(((Activity)mContext).findViewById(android.R.id.content),  result.getError(), Snackbar.LENGTH_SHORT).show();
            return false;
        }
    }
    @Override public void destroyItem(ViewGroup collection, int position, Object view) {collection.removeView((View) view);}

    @Override public int getCount() {return topics.size();}

    @Override public boolean isViewFromObject(View view, Object object) {return view == object;}

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
