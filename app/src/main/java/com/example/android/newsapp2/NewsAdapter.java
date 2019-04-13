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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * NewsAdapter to create a layout for each list item
 */
class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new NewsAdapter
     *
     * @param context
     * @param news
     */
    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }


    // Returns list item view of the given position in a list
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        // check if there is already an existing list item view otherwise inflate a new one
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        News currentNews = getItem(position);

        // Find the references of the given IDs
        TextView titleView = listItemView.findViewById(R.id.title_view);
        TextView sectionView = listItemView.findViewById(R.id.section_view);
        TextView dateView = listItemView.findViewById(R.id.date_view);
        TextView authorView = listItemView.findViewById(R.id.author_view);

        // if currentNews is not null, set the text of the current news object
        if (currentNews != null) {
            titleView.setText(currentNews.getTitle());
            sectionView.setText(currentNews.getSection());
            dateView.setText(formatDate(currentNews.getDate()));
            authorView.setText(formatAuthorName(currentNews.getAuthorFirstName(), currentNews.getAuthorLastName()));
        }
        return listItemView;
    }

    /**
     * formats the name of the author
     *
     * @param firstName
     * @param lastName
     * @return String
     */
    private String formatAuthorName(String firstName, String lastName) {
        final String PREFIX = "by ";

        String formattedFirstName = "";
        String formattedLastName = "";

        if (firstName != null && !firstName.isEmpty()) {
            formattedFirstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
        }
        if (lastName != null && !lastName.isEmpty()) {
            formattedLastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
        }

        if (formattedFirstName.isEmpty() && formattedLastName.isEmpty()) {
            return "";
        } else if (formattedFirstName.isEmpty()) {
            return PREFIX + formattedLastName;
        } else if (formattedLastName.isEmpty()) {
            return PREFIX + formattedFirstName;
        } else {
            return PREFIX + formattedFirstName + " " + formattedLastName;
        }
    }

    /**
     * formats the date
     *
     * @param dateTime
     * @return String
     */
    private String formatDate(String dateTime) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date formattedDate = dateFormat.parse(dateTime);
            SimpleDateFormat formatOutput = new SimpleDateFormat("dd. MMM yyyy");
            return formatOutput.format(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
