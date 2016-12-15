package com.group6boun451.learner.utils;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.group6boun451.learner.R;
import com.group6boun451.learner.activity.HomePage;
import com.group6boun451.learner.activity.SearchActivity;
import com.group6boun451.learner.activity.Task;
import com.group6boun451.learner.model.Comment;
import com.group6boun451.learner.model.User;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import static com.group6boun451.learner.utils.GlideHelper.getReadableDateFromDate;
import static com.group6boun451.learner.utils.GlideHelper.showResult;

/**
 * Created by Esra Alınca on 15.12.2016.
 */

public class UserListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<User> comments;
    private Context context;
    public UserListAdapter(Context context, List<User> items) {
        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        comments = items;
        this.context = context;
    }


    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public User getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView , ViewGroup parent){

        // layout for list view
        View v = mInflater.inflate(R.layout.user_item, null);

        ImageView usrImg = (ImageView) v.findViewById(R.id.commentUserImg);
        TextView usrName = (TextView) v.findViewById(R.id.commentUsrName);
        TextView comText = (TextView) v.findViewById(R.id.commentText);
        LikeButton followButton = (LikeButton) v.findViewById(R.id.follow_button);
        final User user = comments.get(position);
        GlideHelper.loadImage(usrImg,user.getPicture());

        usrName.setText(user.getFirstName() +" " +user.getLastName() );
        comText.setText(user.getEmail());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra("tagName","Topics of "+ user.getFirstName());
                intent.putExtra("query",user.getId()+"");
                intent.putExtra("type","following");
                context.startActivity(intent);
            }
        });
        followButton.setLiked(true);
        followButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                new Task<String>(context, new Task.Callback() {
                    @Override
                    public void onResult(String result) {showResult((Activity) context,result);}
                })
                        .execute(context.getString(R.string.base_url) + "follow/"+user.getId());
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                new Task<String>(context, new Task.Callback() {
                    @Override
                    public void onResult(String result) {showResult((Activity) context,result);}
                })
                        .execute(context.getString(R.string.base_url) + "unfollow/"+user.getId());
            }
        });
        return v;

    }

    public void add(User user) {
        comments.add(user);
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }
}
