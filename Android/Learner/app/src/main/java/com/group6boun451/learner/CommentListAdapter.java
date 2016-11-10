package com.group6boun451.learner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by muaz on 05.11.2016.
 */
// adapter for list view of comments
    // methods are implmented according to BaseAdapter
public class CommentListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Comment> comments;
    private Context _context;
    public CommentListAdapter(Context context, List<Comment> items) {
        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        comments = items;
        _context = context;
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

        usrImg.setImageDrawable(_context.getResources().getDrawable(com.getUsrImgId()));
        usrName.setText(com.getUserName());
        comDate.setText(com.getDate());
        comText.setText(com.getText());

        return v;


    }
}
