package com.group6boun451.learner.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.util.Util;
import com.doodle.android.chips.ChipsView;
import com.doodle.android.chips.model.Contact;
import com.group6boun451.learner.R;
import com.group6boun451.learner.activity.HomePage;
import com.group6boun451.learner.activity.SearchActivity;
import com.group6boun451.learner.activity.Task;
import com.group6boun451.learner.model.Tag;
import com.group6boun451.learner.model.Topic;
import com.group6boun451.learner.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.group6boun451.learner.utils.GlideHelper.getReadableDateFromDate;
import static com.group6boun451.learner.utils.GlideHelper.showResult;

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

     class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img;
        private TextView textTopicTitle;
        private TextView textAuthor;
        private TextView textDate;
        private Button likeButton;
        private ChipsView mChipsView;
        private ImageView imgProfile;

        private ViewHolder(View view) {
            super(view);
            textTopicTitle = (TextView) view.findViewById(R.id.textTopicTitle);
            textAuthor = ((TextView) view.findViewById(R.id.textAuthor));
            textDate = ((TextView) view.findViewById(R.id.textDate));
            img = (ImageView) view.findViewById(R.id.imageTopic);
            likeButton = (Button)view.findViewById(R.id.like_button);
            mChipsView = (ChipsView) view.findViewById(R.id.cv_contacts);
            imgProfile = (ImageView) view.findViewById(R.id.imgTopicPageUserImage);


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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Topic topic = mDataSet.get(position);
        holder.textTopicTitle.setText(topic.getHeader());
        holder.textAuthor.setText(topic.getOwner().getFirstName());
        holder.textDate.setText(getReadableDateFromDate(context,topic.getRevealDate()));

        // change EditText config
        holder.mChipsView.getEditText().setVisibility(View.GONE);
        int k = holder.mChipsView.getChips().size();
        Contact c = new Contact(null,null,null,"",null);
        for(int i =0;i<k;i++){holder.mChipsView.removeChipBy(c);}
        for(Tag t : topic.getTags()){
            String tagName = t.getName();
            Contact contact = new Contact(tagName, t.getContext(), t.getId()+"", "", null);
            holder.mChipsView.addChip(tagName, null, contact,true);
        }

        for(final ChipsView.Chip chip: holder.mChipsView.getChips()) {
            chip.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SearchActivity.class);
                    intent.putExtra("tagName",chip.getContact().getFirstName());
                    intent.putExtra("query",chip.getContact().getDisplayName());
                    context.startActivity(intent);
                }
            });
        }
        GlideHelper.loadImage(context,holder.img, topic);

        holder.imgProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_menu_profile));

        if(isLiked(topic.getLikedBy())) holder.likeButton.setTextColor(context.getResources().getColor(R.color.selected_item_color));
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task<String> likeTask = new Task<>(context, new Task.Callback() {
                    @Override
                    public void onResult(String result) {showResult((Activity) context,result);}
                });
                if(holder.likeButton.getCurrentTextColor()!=context.getResources().getColor(R.color.selected_item_color)) {
                    likeTask.execute(context.getString(R.string.base_url) + "topic/like/"+topic.getId());
                    holder.likeButton.setTextColor(context.getResources().getColor(R.color.selected_item_color));
                }else {
                    likeTask.execute(context.getString(R.string.base_url) + "topic/unlike/"+topic.getId());
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
    private boolean isLiked(List<User> likedBy) {
        for (User u:likedBy){if(u.getEmail().equalsIgnoreCase(HomePage.username))return true;} return false;
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