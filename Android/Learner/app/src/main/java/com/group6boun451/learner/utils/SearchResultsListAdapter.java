package com.group6boun451.learner.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.util.Util;
import com.group6boun451.learner.R;
import com.group6boun451.learner.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder> {

    private List<Topic> mDataSet = new ArrayList<>();
    Context context;

    public SearchResultsListAdapter(Context c){
        context = c;
    }
    public SearchResultsListAdapter(){
    }

    private int mLastAnimatedItemPosition = -1;

    public interface OnItemClickListener{
        void onClick(Topic location);
    }

    private OnItemClickListener mItemsOnClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mLocation;
        private final ImageView img;
        public TextView textTopicTitle;
        public TextView textAuthor;
        public TextView textDate;
        public Button likeButton;

        public ViewHolder(View view) {
            super(view);
            mLocation = (TextView) view.findViewById(R.id.textTopicTitle);
            textTopicTitle = (TextView) view.findViewById(R.id.textTopicTitle);
            textAuthor = ((TextView) view.findViewById(R.id.textAuthor));
            textDate = ((TextView) view.findViewById(R.id.textDate));
            img = (ImageView) view.findViewById(R.id.imageTopic);
            likeButton = (Button)view.findViewById(R.id.like_button);
        }
    }

    public void swapData(List<Topic> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener){
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public SearchResultsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchResultsListAdapter.ViewHolder holder, final int position) {
        final Topic topic = mDataSet.get(position);
        holder.textTopicTitle.setText(topic.getHeader());
        holder.textAuthor.setText(topic.getOwner().getFirstName());
        holder.textDate.setText(topic.getRevealDate().toString());


        GlideHelper.loadImage(context,holder.img, topic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  ((HomePage) v.getContext()).openDetails(v.findViewById(R.id.card_view), topic);
            }
        });


      //  if(isLiked(topic.getLikedBy())) likeButton.setTextColor(context.getResources().getColor(R.color.mdtp_accent_color));
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO likes
                if(!topic.getLikedBy().contains(topic)) {
                  //  new HomePage.LikeTopicTask().execute(""+topic.getId(),"like");
                    holder.likeButton.setTextColor(context.getResources().getColor(R.color.mdtp_accent_color));
                }else {
                 //   new HomePage.LikeTopicTask().execute(""+topic.getId(),"");
                    holder.likeButton.setTextColor(context.getResources().getColor(R.color.white));
                }
            }
        });
        if(mLastAnimatedItemPosition < position){
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = position;
        }

        if(mItemsOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemsOnClickListener.onClick(mDataSet.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private void animateItem(View view) {
        view.setTranslationY(Util.getScreenHeight((Activity) view.getContext()));
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }
}