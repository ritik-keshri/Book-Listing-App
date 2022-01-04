package com.example.booklistingapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private final String urldata = "https://www.googleapis.com/books/v1/volumes?q=";
    private String url;
    private ImageView imageView;
    private SearchView searchView;
    private TextView textView;
    private ListView listView;
    private BookAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        //To check network connection is there or not.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        boolean isConnected = networkInfo != null && networkInfo.isConnected();
        if (!isConnected) {
        textView.setText(R.string.no_internet_connection);
            progressBar.setVisibility(View.GONE);
        } else
            getLoaderManager().initLoader(0, null, this);

        adapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(adapter);

        // perform set on query text listener event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null)
                    editQuery(query);
                else
                    imageView.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query != null)
                    editQuery(query);
                else
                    imageView.setVisibility(View.VISIBLE);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentEarthquake = (Book) adapter.getItem(position);
                String url = currentEarthquake.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private void init() {
        listView = findViewById(R.id.list);
        searchView = findViewById(R.id.search);
        imageView = findViewById(R.id.logo);
        textView = findViewById(R.id.text);
        progressBar = findViewById(R.id.prgoressbar);
    }

    private void editQuery(String query) {
        query = query.trim();
        query = query.replaceAll(" ", "+");
        url = urldata + query;
        imageView.setVisibility(View.GONE);
        getLoaderManager().restartLoader(0, null, MainActivity.this);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        return new BookLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        //if earthquake data is empty
        progressBar.setVisibility(View.GONE);
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }
}