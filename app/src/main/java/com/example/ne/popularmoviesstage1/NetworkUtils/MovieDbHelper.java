package com.example.ne.popularmoviesstage1.NetworkUtils;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.ne.popularmoviesstage1.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ne on 5/18/18.
 */

// We need a class that will handle the connections and
//  JSON parsing for the data that's returned from
//  the MovieDB API
public class MovieDbHelper {
    // Member field to store the API key for future requests
    private String mApiKey;

    // Member field to store the context passed to the constructor
    private Context mContext;

    // Constants for determining which endpoint to go after
    private static final String BASE_MOVIEDB_URL = "https://api.themoviedb.org";
    private static final String ENDPOINT_API_VERSION = "3";
    private static final String ENDPOINT_MOVIE = "movie";
    private static final String ENDPOINT_APIKEY_KEY = "api_key";
    public static final String ENDPOINT_POPULAR = "popular";
    public static final String ENDPOINT_TOP_RATED = "top_rated";

    // Class constructor, takes a context which is
    //  used to fetch the ApiKey from resources
    public MovieDbHelper(@NonNull Context context) {
        // Store the passed context
        mContext = context;
        // Store the ApiKey
        mApiKey = this.getApiKey();
    }

    // Static method to construct the base URI for querying
    private static Uri assembleBaseUri() {
        // Create a builder by parsing the base URL
        Uri.Builder builder = Uri.parse(BASE_MOVIEDB_URL).buildUpon();
        // Add the API version as the first path
        builder.appendPath(ENDPOINT_API_VERSION);
        // Second path is the word "movie", saved here as
        //  a constant for consistency
        builder.appendPath(ENDPOINT_MOVIE);
        // Build and return the built Uri object
        return builder.build();
    }

    // Static method to construct a URI for querying
    //  based on a string passed and append the api_key
    //  k/v pair to the URI
    private Uri assembleQueryUri(String queryType) {
        // Get a builder from the assembled base Uri
        Uri.Builder builder = MovieDbHelper.assembleBaseUri().buildUpon();
        // Different actions based on which string is passed
        //  (this is why I defined the parts of the URL
        //  as static final strings, so they can be passed
        //  to this method)
        switch (queryType) {
            case ENDPOINT_POPULAR:
                builder.appendPath(ENDPOINT_POPULAR);
                break;
            case ENDPOINT_TOP_RATED:
                builder.appendPath(ENDPOINT_TOP_RATED);
                break;
            default:
                builder.appendPath(queryType);
        }
        // We need to append the ApiKey k/v pair to the
        //  end of the current Uri
        builder.appendQueryParameter(ENDPOINT_APIKEY_KEY, mApiKey);
        // Build and return the built Uri object
        return builder.build();
    }

    // Query for a specific set of data, and return
    //  that JSON data as a String for now, to test
    public String fetchApiData(String queryType) {
        // There is no option when null is passed, so rather
        //  than return bad data, we will instead return
        //  exactly what was passed
        if (queryType == null) {
            return null;
        }
        // Open a connection and read the data; this part contains
        //  research from StackOverflow on using InputStreamReader
        //  properly with the URLConnection returned from the URL,
        //  since I'm still very new to Java Streams and Readers
        StringBuilder builder = new StringBuilder("");
        try {
            Log.w("MovieDbHelper", "Query Uri: " + this.assembleQueryUri(queryType).toString());
            URL newUrl = new URL(this.assembleQueryUri(queryType).toString());
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) newUrl.openConnection();
            Log.w("MovieDbHelper", "Connection opened!");
            InputStream inputStream = httpsURLConnection.getInputStream();
            Log.w("MovieDbHelper", "InputStream created!");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            Log.w("MovieDbHelper", "InputStreamReader created!");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Log.w("MovieDbHelper", "BufferedReader created!");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.w("MovieDbHelper", "Reading line from bufferedReader");
                builder.append(line);
            }
            bufferedReader.close();
            httpsURLConnection.disconnect();
            Log.w("MovieDbHelper", "Done Reading!");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    // Static method to fetch the apikey from a
    //  resource file that's hidden from git
    //  (res/values/apikey.xml)
    private String getApiKey() {
        return mContext.getResources().getString(R.string.api_key);
    }

}
