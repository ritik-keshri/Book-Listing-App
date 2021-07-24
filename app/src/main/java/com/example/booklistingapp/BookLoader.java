package com.example.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String url;

    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (url == null)
            return null;
        return QueryUtils.fetchBookData(url);
    }
}
