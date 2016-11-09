package com.group6boun451.learner.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.group6boun451.learner.R;

import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.alexvasilkov.android.commons.utils.Views;
import com.group6boun451.learner.UnfoldableDetailsActivity;
import com.group6boun451.learner.utils.GlideHelper;

import java.util.Arrays;

public class TopicsAdapter extends ItemsAdapter<Topic> implements View.OnClickListener {

    public TopicsAdapter(Context context) {
        super(context);
        setItemsList(Arrays.asList(Topic.getAllTopics(context.getResources())));
    }

    @Override
    protected View createView(Topic item, int pos, ViewGroup parent, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.image = Views.find(view, R.id.list_item_image);
        vh.image.setOnClickListener(this);
        vh.title = Views.find(view, R.id.list_item_title);
        view.setTag(vh);

        return view;
    }

    @Override
    protected void bindView(Topic item, int pos, View convertView) {
        ViewHolder vh = (ViewHolder) convertView.getTag();

        vh.image.setTag(R.id.list_item_image, item);
        GlideHelper.loadPaintingImage(vh.image, item);
        vh.title.setText(item.getTitle());
    }

    @Override
    public void onClick(View view) {
        Topic item = (Topic) view.getTag(R.id.list_item_image);
        ((UnfoldableDetailsActivity) view.getContext()).openDetails(view, item);

    }

    private static class ViewHolder {
        ImageView image;
        TextView title;
    }

}
