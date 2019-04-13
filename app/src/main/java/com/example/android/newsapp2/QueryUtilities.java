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
 * Helper methods
 */
final class QueryUtilities {

    private static final int SUCCESS_CODE = 200;
    private static final int CONNECT_TIMEOUT = 1000;
    private static final int READ_TIMEOUT = 1500;

    // private Constructor to prevent creating an QueryUtilites object
    private QueryUtilities() {
    }

    // get the dataset and returns list of objects
    public static List<News> getNewsData(String newsURL) {
        // create URL object
        URL url = createURL(newsURL);

        // HTTP request and receive JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPConnection(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // extract fields, create a list and return it
        List<News> news = extractJSONObjects(jsonResponse);
        return news;
    }

    // Create a URL from a String
    private static URL createURL(String url) {
        URL newsURL = null;

        // Handle the transformation of an String to URL and catch exception
        try {
            newsURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return newsURL;
    }

    // Connect to the HTTPServer and get a stream of fata
    private static String makeHTTPConnection(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            // open connection, set request type, set timeouts in milliseconds
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.connect();

            // if the connection was build successful, keep going on
            if (connection.getResponseCode() == SUCCESS_CODE) {
                inputStream = connection.getInputStream();
                jsonResponse = readStream(inputStream);
            } else {
                Log.e("Error response code: ", String.valueOf(connection.getResponseCode()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                // close the connection
                connection.disconnect();
            }
            if (inputStream != null) {
                // close teh inputStream
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Extract the JSONObjects from the input stream
    private static List<News> extractJSONObjects(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> news = new ArrayList<>();

        try {
            // create JSONObject for JSON response string
            JSONObject base = new JSONObject(newsJSON);
            // extract JSONObject with key "response"
            JSONObject level2 = base.getJSONObject("response");

            // extract JSONArray with key "results"
            JSONArray results = level2.getJSONArray("results");

            // create a news object for each news in news array
            for (int i = 0; i < results.length(); i++) {
                String firstName = "";
                String lastName = "";

                // get a single news object for position i
                JSONObject singleNews = results.getJSONObject(i);

                // extract the value for the given keys, if there is no value it returns null
                String webTitle = singleNews.optString("webTitle");
                String sectionName = singleNews.optString("sectionName");
                String webDate = singleNews.optString("webPublicationDate");
                String webURL = singleNews.optString("webUrl");

                // extract JSONArray with the key "tags"
                JSONArray tags = singleNews.optJSONArray("tags");

                // extract the value at the first position of the array
                JSONObject tag0 = tags.optJSONObject(0);

                // check if there was any value at the first position of the array
                // first and last name are mixed up by the api
                if (tag0 != null && tag0.length() > 1) {
                    firstName = tag0.optString("lastName");
                    lastName = tag0.optString("firstName");
                }

                // create a new news object and add it to the list of news
                news.add(new News(webTitle, sectionName, webDate, webURL, firstName, lastName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }

    // convert input stream to string including the JSON response
    private static String readStream(InputStream input) throws IOException {
        StringBuilder output = new StringBuilder();
        if (input != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
