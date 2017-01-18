package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pestonio on 01/10/2016.
 */
public final class DataFetch {

    private static final String LOG_TAG = DataFetch.class.getSimpleName();

    public static List<NewsItemClass> fetchNewsData(String requestUrl) {
        URL url = creatUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<NewsItemClass> newItems = extractFeatureFromJson(jsonResponse);
        return newItems;
    }

    private static URL creatUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<NewsItemClass> extractFeatureFromJson(String newsJson) {
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }
        List<NewsItemClass> newsItems = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJson);
            JSONObject baseNewsObject = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = baseNewsObject.getJSONArray("results");
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNewsItem = newsArray.getJSONObject(i);
                String title = currentNewsItem.getString("webTitle");
                String section = currentNewsItem.getString("sectionName");
                String url = currentNewsItem.getString("webUrl");
                JSONArray tagsArray = currentNewsItem.getJSONArray("tags");
                String contributor = null;
                for (int o = 0; o < tagsArray.length(); o++) {
                    JSONObject currentTagsItem = tagsArray.getJSONObject(o);
                    contributor = currentTagsItem.getString("webTitle");
                }
                NewsItemClass newsItemClass = new NewsItemClass(title, section, contributor, url);
                newsItems.add(newsItemClass);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "DataFetch - problem parsing JSON", e);
        }
        return newsItems;
    }

}
