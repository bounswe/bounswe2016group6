package com.group6boun451.learner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by muaz on 07.10.2016.
 */
public class TopicHomeFragment extends android.support.v4.app.Fragment {

    private Topic topic;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getArguments().getInt("topic_id");
        TopicContainer tpc = new TopicContainer(getContext());
        topic = tpc.getTopic(id);


    }

    public static TopicHomeFragment newInstance(int id){
        Bundle args = new Bundle();
        args.putSerializable("topic_id",id);

        TopicHomeFragment fragment = new TopicHomeFragment();
        fragment.setArguments(args);
        return fragment;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topic_home,container,false);
        TextView title = (TextView) v.findViewById(R.id.textTopicTitle);
        TextView author = (TextView) v.findViewById(R.id.textAuthor);
        TextView date = (TextView) v.findViewById(R.id.textDate);
        ImageView img = (ImageView) v.findViewById(R.id.imageTopic);


        title.setText(topic.getTitle());
        author.setText(topic.getEditor());
        date.setText(topic.getDate());
        img.setImageDrawable(getResources().getDrawable(topic.getImage()));

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topic.getId()<1)
                    ((HomePage) v.getContext()).openDetails(v, topic);
                else{
                    Intent intent = new Intent(getActivity(),TopicPage.class);
                    intent.putExtra("topic_id",topic.getId());
                    startActivity(intent);
                }
            }
        });
        return v;
    }
}
