package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsItemClass>> {

    private static final String GUARDIAN_API = "http://content.guardianapis.com/search?q=london/entertainment&show-tags=contributor&order-by=newest&page-size=20&api-key=test";

    private static final int NEWS_ITEM_LOADER_ID = 1;

    private ListView newsListView;

    private ArrayList<NewsItemClass> newsItemClassArrayList;

    private NewsItemAdapter adapter;

    private Button refreshButton;

    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshButton = (Button) findViewById(R.id.refreshButton);
        errorText = (TextView) findViewById(R.id.errorText);

        newsListView = (ListView) findViewById(R.id.list);
        newsItemClassArrayList = new ArrayList<NewsItemClass>();
        adapter = new NewsItemAdapter(this, newsItemClassArrayList);
        adapter.notifyDataSetChanged();
        newsListView.setAdapter(adapter);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsItemClass currentNewsItem = adapter.getItem(position);
                Uri newsItemUri = Uri.parse(currentNewsItem.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsItemUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_ITEM_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            errorText.setText("No internet connection.");
        }
    }

    @Override
    public Loader<List<NewsItemClass>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(GUARDIAN_API);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new NewsItemLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItemClass>> loader, List<NewsItemClass> newsItemClasses) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        adapter.clear();
        if (newsItemClasses != null && !newsItemClasses.isEmpty()) {
            adapter.addAll(newsItemClasses);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItemClass>> loader) {
        adapter.clear();
    }
}
