package com.example.ne.popularmoviesstage1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ne on 5/29/18.
 */

// We don't actually need to implement anything in this java file, I believe
//  this is really just a "nesting place" for the fragment we created
public class SettingsActivity extends AppCompatActivity {
    // Here's the override where we set the content view for this activity
    //  to the layout we created to house the fragment
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
