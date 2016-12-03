package com.group6boun451.learner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.group6boun451.learner.model.Comment;
import com.group6boun451.learner.utils.GlideHelper;

import java.util.List;

/**
 * Created by muaz on 05.11.2016.
 * adapter for list view of comments
 * methods are implmented according to BaseAdapter
 */
public class CommentListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Comment> comments;
    public CommentListAdapter(Context context, List<Comment> items) {
        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        comments = items;
    }


    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView , ViewGroup parent){

        // layout for list view
        View v = mInflater.inflate(R.layout.comment_item, null);

        ImageView usrImg = (ImageView) v.findViewById(R.id.commentUserImg);
        TextView usrName = (TextView) v.findViewById(R.id.commentUsrName);
        TextView comDate = (TextView) v.findViewById(R.id.commentDate);
        TextView comText = (TextView) v.findViewById(R.id.commentText);
        Comment com = comments.get(position);
        GlideHelper.loadImage(usrImg,com.getOwner().getPicture());

        usrName.setText(com.getOwner().getFirstName());
        comDate.setText(com.getCreatedAt().toString());
        comText.setText(com.getContent());

        return v;


    }

    public void add(Comment comment) {
        comments.add(comment);
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }
}
