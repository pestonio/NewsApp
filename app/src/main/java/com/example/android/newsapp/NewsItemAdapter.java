package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pestonio on 01/10/2016.
 */
public class NewsItemAdapter extends ArrayAdapter<NewsItemClass> {

    public NewsItemAdapter(Context context, ArrayList<NewsItemClass> newsItem) {
        super(context, 0, newsItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            NewsItemClass currentNewsItem = getItem(position);
            TextView headlineView = (TextView) listItemView.findViewById(R.id.headline_text);
            headlineView.setText(currentNewsItem.getHeadline());

            TextView sectionView = (TextView) listItemView.findViewById(R.id.section_text);
            sectionView.setText("Category: " + currentNewsItem.getSection());

            TextView writerView = (TextView) listItemView.findViewById(R.id.contributor_text);
            writerView.setText("By: " + currentNewsItem.getContributor());
        } else {
            NewsItemClass currentNewsItem = getItem(position);
            TextView headlineView = (TextView) listItemView.findViewById(R.id.headline_text);
            headlineView.setText(currentNewsItem.getHeadline());

            TextView sectionView = (TextView) listItemView.findViewById(R.id.section_text);
            sectionView.setText("Category: " + currentNewsItem.getSection());

            TextView writerView = (TextView) listItemView.findViewById(R.id.contributor_text);
            writerView.setText("By: " + currentNewsItem.getContributor());
        }
        return listItemView;
    }
}
