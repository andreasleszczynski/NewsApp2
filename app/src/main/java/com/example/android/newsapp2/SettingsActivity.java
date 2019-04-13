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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference search = findPreference(getString(R.string.settings_search_key));
            bindPreferenceSummaryToValue(search);

            Preference pageSize = findPreference(getString(R.string.settings_page_size_key));
            bindPreferenceSummaryToValue(pageSize);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // updates the displayed preference summary after change
            String newStringValue = newValue.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int preferenceIndex = listPreference.findIndexOfValue(newStringValue);
                if (preferenceIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[preferenceIndex]);
                }
            } else if (preference.getKey().equalsIgnoreCase("page_size")) {
                if (checkValueForPageSize(newStringValue)) {
                    preference.setSummary(newStringValue);
                } else {
                    Toast.makeText(getActivity(), "Invalid Page Size", Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                preference.setSummary(newStringValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            // set current NewsPreferenceFragement instance to listen for changes
            preference.setOnPreferenceChangeListener(this);

            // read the current value of shared preference
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = sharedPreferences.getString(preference.getKey(), "");

            onPreferenceChange(preference, preferenceString);
        }

        // checks if the user selects the correct page sice
        private boolean checkValueForPageSize(String inputString) {
            final int MIN = 1;
            final int MAX = 200;
            int input = Integer.parseInt(inputString);

            return (input >= MIN && input <= MAX);
        }
    }
}
