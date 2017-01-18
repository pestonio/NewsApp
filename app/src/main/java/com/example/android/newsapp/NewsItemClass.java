package com.example.android.newsapp;

/**
 * Created by pestonio on 01/10/2016.
 */
public class NewsItemClass {

    /*Custom class containing information about the headline, the categorisation and the writer
    of the news items we want to dislay
     */

    private String mHeadline;

    private String mSection;

    private String mContributor;

    private String mUrl;

    public NewsItemClass(String headline, String section, String contributor, String url) {
        mHeadline = headline;
        mSection = section;
        mContributor = contributor;
        mUrl = url;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getSection() {
        return mSection;
    }

    public String getContributor() {
        return mContributor;
    }

    public String getUrl() {
        return mUrl;
    }
}
