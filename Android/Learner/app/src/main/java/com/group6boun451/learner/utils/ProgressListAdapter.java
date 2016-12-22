package com.group6boun451.learner.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.group6boun451.learner.R;
import com.group6boun451.learner.activity.SearchActivity;
import com.group6boun451.learner.model.QuizProgressDTO;
import com.like.LikeButton;

import java.util.List;

/**
 * Created by Ahmet Zorer on 12/20/2016.
 */
public class ProgressListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<QuizProgressDTO> progressDTOs;
    private Context context;
    public ProgressListAdapter(Context context, List<QuizProgressDTO> items) {
        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        progressDTOs = items;
        this.context = context;
    }


    @Override
    public int getCount() {
        return progressDTOs.size();
    }

    @Override
    public QuizProgressDTO getItem(int position) {
        return progressDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView , ViewGroup parent){

        // layout for list view
        View v = mInflater.inflate(R.layout.comment_item, null);

        TextView usrName = (TextView) v.findViewById(R.id.commentUsrName);
        final TextView comDate = (TextView) v.findViewById(R.id.commentDate);
        final TextView comText = (TextView) v.findViewById(R.id.commentText);
        LikeButton followButton = (LikeButton) v.findViewById(R.id.follow_button);
        final QuizProgressDTO quizProgress = progressDTOs.get(position);

        usrName.setText(quizProgress.getTopicPack().getName());
        comDate.setText(quizProgress.getPackCompleted() + " / " + quizProgress.getPackTotal());
        comText.setText("");
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchActivity.class);
                if(quizProgress.getTopicPack()!=null) intent.putExtra("tagName",quizProgress.getTopicPack().getName());
                intent.putExtra("query",quizProgress.getTopicPack().getId()+"");
                intent.putExtra("type","pack2");
                context.startActivity(intent);
            }
        });
               return v;

    }

    public void add(QuizProgressDTO user) {
        progressDTOs.add(user);
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }
}

