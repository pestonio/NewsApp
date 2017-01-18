package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by pestonio on 01/10/2016.
 */
public class NewsItemLoader extends AsyncTaskLoader<List<NewsItemClass>> {
    private static final String LOG_TAG = NewsItemLoader.class.getName();

    private String mUrl;

    public NewsItemLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsItemClass> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<NewsItemClass> newItems = DataFetch.fetchNewsData(mUrl);
        return newItems;
    }
}
