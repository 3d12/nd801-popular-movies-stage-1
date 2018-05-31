package com.example.ne.popularmoviesstage1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

/**
 * Created by ne on 5/26/18.
 */

// I guess it's better form to use PreferenceFragmentCompat instead of
//  PreferenceActivity or PreferenceFragment.
// Also, I'm not 100% sure it's more efficient to implement the
//  OnSharedPreferenceChangeListener in this class instead of MainActivity,
//  but I realize you can't really assume that an instance of MainActivity
//  would have been the calling class, so better to handle setting the
//  summary here, and have a separate process of loading the current
//  preferences in MainActivity that "links" these together.
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    // Required implementation, this is where we load the preferences that we
    //  set up using PreferenceScreen in the XML.
    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        // This method is used to set the XML file outlining our PreferenceScreen
        //  as the preferences reference for this instance of the settings
        //  fragment.
        setPreferencesFromResource(R.xml.preferences, rootKey);
        // Get a reference to the PreferenceScreen
        PreferenceScreen prefScreen = this.getPreferenceScreen();
        // Get a reference to the SharedPreferences that the PreferenceScreen
        //  is saving to
        SharedPreferences sharedPreferences = prefScreen.getSharedPreferences();
        // Get a total count of the number of preferences in the PreferenceScreen,
        //  to iterate through in the next step
        int prefCount = prefScreen.getPreferenceCount();

        // Iterative loop, indexing through the preferences
        for (int i=0; i<prefCount; i++) {
            // Assigning the generic Preference type by fetching it from
            //  the PreferenceScreen via getPreference
            Preference p = prefScreen.getPreference(i);
            // Validate that the preference is of the specific type we want
            if (p instanceof ListPreference) {
                // Call the instance method that will set the summary based
                //  on the current value of the current preference, fetched
                //  from the instance of SharedPreferences we got earlier
                this.setPreferenceSummary(p, sharedPreferences.getString(p.getKey(), ""));
            }
        }
    }

    // Rather than override the onCreateView method and attach a listener
    //  to each preference by iterating over them, we can instead attach
    //  a listener to the SharedPreferences itself by fetching it from
    //  the PreferenceManager
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Assign the listener via the PreferenceManager & SharedPreferences
        this.getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    // Overriding onDestroy so that we can unattach the listener that we
    //  attached in onCreate, to help prevent potential memory leaks
    @Override
    public void onDestroy() {
        // Unassign the listener via the PreferenceManager & SharedPreferences
        // Does it really matter whether or not this is done before the super
        //  method, since the garbage collection should technically wait until
        //  this method finishes before terminating the instance?
        // Also, shouldn't we release this in onPause instead, if we really
        //  wanted to avoid memory leaks? In this current implementation, this
        //  listener might never get unassigned if the user switches tasks away
        //  from this app while in the settings menu, then the app gets
        //  terminated by the Android process manager. Will that really matter?
        this.getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    // This is a required override to implement OnSharedPreferenceChangeListener
    //  in this class. We will use this to trigger a refresh of the changed
    //  preference based on the preference key passed to this callback.
    // To be fair, I used my notes from the exercises in lesson 06 of Developing
    //  Android Apps to implement this method.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // First we have to find the preference that was changed using the key
        //  that's passed to this method
        Preference changedPref = this.getPreferenceScreen().findPreference(key);
        // Then, using the preference manager to fetch the default sharedPreferences
        //  (rather than trusting that the one that's passed is the one that's
        //  updated) we fetch the new value of that preference by using getString
        //  and I use blank ("") here as the default value, so that the function
        //  to update the preference summary won't be called if the preference
        //  has no value for getString (e.g. CheckboxPreference?)
        String changedValue = this.getPreferenceManager().getSharedPreferences().getString(key, "");
        // Only want to update summary where value has a getString
        if (!changedValue.equals("")) {
            // Use helper method to set summary on changed preference
            this.setPreferenceSummary(changedPref, changedValue);
        }
    }

    // Custom method that will update the summary of a preference with the passed
    //  value as long as the passed preference is an instance of ListPreference.
    // To be fair, I used my notes from the exercises in lesson 06 of Developing
    //  Android Apps to implement this method.
    private void setPreferenceSummary(Preference preference, String newValue) {
        // Validate that the preference passed is a ListPreference, so we
        //  can use setSummary safely
        if (preference instanceof ListPreference) {
            // Coerce to specific type
            ListPreference listPreference = (ListPreference) preference;
            // Find the index of the value (since the indexes of the
            //  entries array and values array are identical)
            int prefIndex = listPreference.findIndexOfValue(newValue);
            // Ensure that the selected option is a valid index, since I
            //  believe findIndexOfValue will return -1 if the passed
            //  value isn't found in the array
            if (prefIndex >= 0) {
                // Set the summary to the corresponding index of the
                //  entries array, fetched with getEntries()
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            } else {
                // Set the summary to blank ("") if the selected value
                //  doesn't exist in the values array
                listPreference.setSummary("");
            }
        }
    }
}
