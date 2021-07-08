package com.example.booklistingapp;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private final String urldata = "https://www.googleapis.com/books/v1/volumes?q=";
    private String url;
    private ImageView imageView;
    private TextView textView;
    private SearchView searchView;
    private ListView listView;
    private BookAdapter adapter;
    public static final String LOG_TAG = Book.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        adapter = new BookAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);

        BookAsyncTask task = new BookAsyncTask();
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            url = urldata + query;
            Log.e(TAG, "onCreate: "+url );
            task.execute(url);
        }
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String newText) {
//                newText = newText.trim();
//                url = urldata;
//                if (newText != null) {
//                    url += newText;
//                    adapter.getFilter().filter(newText);
//                }
//                else {
//                    textView.setText("Invalid Data!!!");
//                }
//                task.execute(url);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                newText = newText.trim();
//                url = urldata;
//                if (newText != null) {
//                    url += newText;
//                    adapter.getFilter().filter(newText);
//                }
//                else {
//                    textView.setText("Invalid Data!!!");
//                }
//                Log.e(TAG, "onQueryTextChange: "+url);
//                return false;
//            }
//        });

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
        textView = findViewById(R.id.textview);
        listView = findViewById(R.id.list);
        searchView = findViewById(R.id.search);
        imageView = findViewById(R.id.logo);
    }

    protected class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {

        protected List<Book> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            Log.e(TAG, "doInBackground: " + urls[0]);
            return QueryUtils.fetchBookData(urls[0]);
        }

        @Override
        protected void onPostExecute(List<Book> data) {
            // Clear the adapter of previous earthquake data
            adapter.clear();

            // If there is a valid list of  then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                adapter.addAll(data);
            }
        }
    }
}