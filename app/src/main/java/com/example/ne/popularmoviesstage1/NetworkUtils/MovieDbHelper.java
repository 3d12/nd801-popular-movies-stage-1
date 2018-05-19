package com.example.ne.popularmoviesstage1.NetworkUtils;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.example.ne.popularmoviesstage1.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by ne on 5/18/18.
 */

// We need a class that will handle the connections and
//  JSON parsing for the data that's returned from
//  the MovieDB API
public class MovieDbHelper {

    // Static method to fetch the apikey from a
    //  resource file that's hidden from git
    //  (res/values/apikey.xml)
    public static String getApiKey(Context context) {
        return context.getResources().getString(R.string.api_key);
    }

}
