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

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of news, using AsyncTaskLoader to perform network requests
 */
class NewsLoader extends AsyncTaskLoader<List<News>> {

    // URL
    private String mUrl;

    /**
     * Constructs a new NewsLoader
     *
     * @param context
     * @param url
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // for the background thread
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // make URL, network request, get JSONObjects and returns a list of news objects
        List<News> news = QueryUtilities.getNewsData(mUrl);
        return news;
    }
}
