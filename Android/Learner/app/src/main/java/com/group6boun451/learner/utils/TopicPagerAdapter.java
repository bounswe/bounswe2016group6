package com.group6boun451.learner.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexvasilkov.android.commons.utils.Views;
import com.doodle.android.chips.ChipsView;
import com.doodle.android.chips.model.Contact;
import com.group6boun451.learner.R;
import com.group6boun451.learner.activity.HomePage;
import com.group6boun451.learner.activity.SearchActivity;
import com.group6boun451.learner.activity.Task;
import com.group6boun451.learner.model.Tag;
import com.group6boun451.learner.model.Topic;
import com.group6boun451.learner.model.User;
import com.group6boun451.learner.widget.CanaroTextView;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import static com.group6boun451.learner.utils.GlideHelper.getReadableDateFromDate;
import static com.group6boun451.learner.utils.GlideHelper.showResult;

/**
 * List adapter for topics in homepage activity.
 *
 */
public class TopicPagerAdapter extends PagerAdapter {
    private Context context;
    private List<Topic> topics;

    public TopicPagerAdapter(Context context) {}

    public TopicPagerAdapter(Context context, List<Topic> topics) {
        this.context = context;
        this.topics = topics;
    }

    @Override
    public Object instantiateItem(final ViewGroup collection, int position) {
        final Topic topic = topics.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.topic_item_home, collection, false);
        ((TextView) v.findViewById(R.id.textTopicTitle)).setText(topic.getHeader());
        ((CanaroTextView) v.findViewById(R.id.textAuthor)).setText(topic.getOwner().getFirstName());
        ((CanaroTextView) v.findViewById(R.id.textDate)).setText(getReadableDateFromDate(context,topic.getRevealDate()));
        Views.find(v, R.id.textAuthor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra("tagName","Topics of "+ topic.getOwner().getFirstName());
                intent.putExtra("query",topic.getOwner().getId()+"");
                intent.putExtra("type","following");
                context.startActivity(intent);
            }
        });
        Views.find(v, R.id.imgTopicPageUserImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra("tagName","Topics of "+ topic.getOwner().getFirstName());
                intent.putExtra("query",topic.getOwner().getId()+"");
                intent.putExtra("type","following");
                context.startActivity(intent);
            }
        });

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
        GlideHelper.loadImage(context,img, topic);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomePage) v.getContext()).openDetails(v.findViewById(R.id.card_view2), topic);
                //check is there quiz
                HomePage.isThereQuiz = topic.getQuestions().size() != 0;

            }
        });

        final ImageView imgProfile = (ImageView) v.findViewById(R.id.imgTopicPageUserImage);
        imgProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_menu_profile));

        for(final ChipsView.Chip chip: mChipsView.getChips()) {
            chip.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("ssss", chip.getContact().toString());
                    Intent intent = new Intent(context, SearchActivity.class);
                    intent.putExtra("tagName",chip.getContact().getFirstName());
                    intent.putExtra("query",chip.getContact().getDisplayName());
                    context.startActivity(intent);
                }
            });
        }
        final LikeButton likeButton = (LikeButton)v.findViewById(R.id.like_button);
        likeButton.setLiked(isLiked(topic.getLikedBy()));
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                new Task<String>(context, new Task.Callback() {
                    @Override
                    public void onResult(String result) {showResult((Activity) context,result);}
                })
                        .execute(context.getString(R.string.base_url) + "topic/like/"+topic.getId());
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                new Task<String>(context, new Task.Callback() {
                    @Override
                    public void onResult(String result) {showResult((Activity) context,result);}
                })
                        .execute(context.getString(R.string.base_url) + "topic/unlike/"+topic.getId());
            }
        });

        final LikeButton followButton = (LikeButton)v.findViewById(R.id.follow_button);
        followButton.setLiked(isLiked(topic.getOwner().getFollowedBy()));
        followButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                new Task<String>(context, new Task.Callback() {
                    @Override
                    public void onResult(String result) {showResult((Activity) context,result);}
                })
                        .execute(context.getString(R.string.base_url) + "follow/"+topic.getOwner().getId());
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                new Task<String>(context, new Task.Callback() {
                    @Override
                    public void onResult(String result) {showResult((Activity) context,result);}
                })
                        .execute(context.getString(R.string.base_url) + "unfollow/"+topic.getOwner().getId());
            }
        });

        collection.addView(v);
        return v;
    }

    private boolean isLiked(List<User> likedBy) {
        if(likedBy==null)return false;
        for (User u:likedBy){if(u.getEmail().equalsIgnoreCase(HomePage.username))return true;} return false;
    }
    @Override public void destroyItem(ViewGroup collection, int position, Object view) {collection.removeView((View) view);}

    @Override public int getCount() {return topics.size();}

    @Override public boolean isViewFromObject(View view, Object object) {return view == object;}

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
