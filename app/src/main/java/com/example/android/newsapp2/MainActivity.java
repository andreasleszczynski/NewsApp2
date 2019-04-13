package com.example.android.newsapp2;

/**
 * Copyright 2018 Andreas Leszczynski
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    // URL of the news data from the guardian api
    private final String REQUEST_URL =
            "http://content.guardianapis.com/search?";

    // Adapter for the list of news
    private NewsAdapter mNewsAdapter;

    // TextView, which is displayed when list empty
    private TextView mEmptyTextView;

    // Final value for the news loader ID
    private static final int NEWS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the reference to the ListView
        ListView newsListView = findViewById(R.id.list);

        // Find the reference to the TextView for the empty case and
        // set this TextView as EmptyView of the ListView
        mEmptyTextView = findViewById(R.id.empty_list);
        newsListView.setEmptyView(mEmptyTextView);

        // Create a new adapter and set the adapter on the ListView
        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mNewsAdapter);

        // Set an item click listener on the ListView, opens website of the selected news by
        // using an intent
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Find the news, which was clicked on
                News currentNews = mNewsAdapter.getItem(position);

                // Convert String URL to Uri object
                Uri newsUri = Uri.parse(currentNews.getURL());

                // Intent to view the news Uri
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // start the intent
                startActivity(websiteIntent);
            }
        });

        // check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // get information about the active networkd
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // in case of network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // get reference to LoaderManager and initialize it
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // find reference to progressbar and make it invisible
            View progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);

            // display error message in empty TextView
            mEmptyTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // getString gets a String value from SharedPreferences for search topic
        String search = sharedPreferences.getString(
                getString(R.string.settings_search_key),
                getString(R.string.settings_search_default));

        // getString gets a String value from SharedPreferences for pageSize
        String pageSize = sharedPreferences.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_pags_size_default));

        // getString gets a String value from SharedPreferences for pageSize
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // parse splits the URI string thats passed in the parameter
        Uri requestUri = Uri.parse(REQUEST_URL);

        // buildupon prepares builder so we can add query parameters
        Uri.Builder queryBuilder = requestUri.buildUpon();

        // append query parameter and value, i.e. "q=android"
        queryBuilder.appendQueryParameter("order-by", orderBy);
        queryBuilder.appendQueryParameter("show-fields", "webPublicationDate");
        queryBuilder.appendQueryParameter("show-tags", "contributor");
        queryBuilder.appendQueryParameter("api-key", "test");
        queryBuilder.appendQueryParameter("q", search);
        queryBuilder.appendQueryParameter("page-size", pageSize);

        // create new loader for the request URL
        return new NewsLoader(this, queryBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        // Hide progress bar when data loaded
        View progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // display message in case of no data was found
        mEmptyTextView.setText(R.string.no_news);

        mNewsAdapter.clear();

        // update the ListView in case of valid data
        if (data != null && !data.isEmpty()) {
            mNewsAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mNewsAdapter.clear();
    }

    @Override
    // initialize the contents of options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the options menu from based on xml file
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // passes the MenuItem that is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        // determine which item was selected
        int id = item.getItemId();
        // match id to action and perform appropriate action
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
