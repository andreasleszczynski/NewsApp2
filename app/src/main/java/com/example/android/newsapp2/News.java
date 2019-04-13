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

class News {

    // states of News object
    private String mTitle;
    private String mSection;
    private String mDate;
    private String mURL;
    private String mAuthorFirstName;
    private String mAuthorLastName;

    /**
     * Constructs a news object
     *
     * @param title
     * @param section
     * @param date
     * @param url
     * @param authorFirstName
     * @param authorLastName
     */
    public News(String title, String section, String date, String url, String authorFirstName,
                String authorLastName) {
        mTitle = title;
        mSection = section;
        mDate = date;
        mURL = url;
        mAuthorFirstName = authorFirstName;
        mAuthorLastName = authorLastName;
    }

    // Returns news title
    public String getTitle() {
        return mTitle;
    }

    // Returns news section
    public String getSection() {
        return mSection;
    }

    // Returns news date
    public String getDate() {
        return mDate;
    }

    // Returns news URL
    public String getURL() {
        return mURL;
    }

    // Returns first name of news author
    public String getAuthorFirstName() {
        return mAuthorFirstName;
    }

    // Returns last name of news author
    public String getAuthorLastName() {
        return mAuthorLastName;
    }
}
