package com.example.ne.popularmoviesstage1.NetworkUtils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.ne.popularmoviesstage1.BuildConfig;
import com.example.ne.popularmoviesstage1.MovieData;
import com.example.ne.popularmoviesstage1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    private static final String BASE_MOVIEDB_IMAGE_URL = "http://image.tmdb.org";
    private static final String ENDPOINT_IMAGE_API_1 = "t";
    private static final String ENDPOINT_IMAGE_API_2 = "p";
    private static final String ENDPOINT_IMAGE_API_SIZE = "w185";

    // Constants for navigating the JSON object mapping
    private static final String JSON_POSTER_PATH_KEY = "poster_path";
    private static final String JSON_TITLE_KEY = "title";
    private static final String JSON_SYNOPSIS_KEY = "overview";
    private static final String JSON_RELEASE_DATE_KEY = "release_date";
    private static final String JSON_RATING_KEY = "vote_average";

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

    // Static method to construct the base image URI for querying
    private static Uri assembleImageUri() {
        // Create a builder by parsing the base URL
        Uri.Builder builder = Uri.parse(BASE_MOVIEDB_IMAGE_URL).buildUpon();
        // Add the API components to the path
        builder.appendPath(ENDPOINT_IMAGE_API_1);
        builder.appendPath(ENDPOINT_IMAGE_API_2);
        builder.appendPath(ENDPOINT_IMAGE_API_SIZE);
        // Build and return the built Uri object
        return builder.build();
    }

    // Static method to construct a Uri pointing directly to
    //  a poster image
    private static Uri constructImageUri(String posterPath) {
        // Strip the leading forward-slash "/" out of the posterPath
        String fixedPosterPath = posterPath.replace("/", "");
        // Create a builder from the base Uri
        Uri.Builder builder = MovieDbHelper.assembleImageUri().buildUpon();
        // Add the fixed posterPath to the end, which includes the file extension
        builder.appendPath(fixedPosterPath);
        // Build and return the built Uri object
        return builder.build();
    }

    // Instance method to construct a URI for querying
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
    //  that JSON data as a List of MovieData objects
    public List<MovieData> fetchApiData(String queryType) {
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
        HttpsURLConnection httpsURLConnection = null;
        try {
            // Build a new URL object from the Uri string
            URL newUrl = new URL(this.assembleQueryUri(queryType).toString());
            // Open a connection to the URL
            httpsURLConnection = (HttpsURLConnection) newUrl.openConnection();
            // Fetch an InputStream from the connection to read from
            InputStream inputStream = httpsURLConnection.getInputStream();
            // Create an InputStreamReader, this (I think) handles the reading
            //  of lines and strings from the stream, rather than characters
            //  or bytes/byteArrays
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            // Create a BufferedReader, which maybe actually handles the reading
            //  of lines from the stream, I'm not really sure what the difference
            //  between a BufferedReader and the InputStreamReader is
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // Placeholder local String field to hold the current line when reading
            String line;
            // The inner part of this while evaluation is the assignment, which
            //  still kind of blows me away that Java allows (assignment within
            //  a boolean evaluation, I mean)
            while ((line = bufferedReader.readLine()) != null) {
                // Appending the line to the end of the StringBuilder
                builder.append(line);
            }
            // Must make sure to close this reader -- this isn't in the finally
            //  statement because if opening or reading from the BufferedReader
            //  throws an error, the BufferedReader should be caught by
            //  garbage collection, since it's only a local field
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Checking to make sure it's not null, since invoking this
            //  while it is will throw an error itself
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }

        // Instantiate an empty ArrayList that we can return
        ArrayList<MovieData> returnList = new ArrayList<MovieData>();
        // Honestly, I built the method that would handle this
        //  before I realized I would really only be using it
        //  once during each parse. Oh well.
        JSONArray resultsJSONArray = this.parseDataIntoJSON(builder.toString());
        // Kinda disappointed that there isn't a better way
        //   to iterate over a JSONArray -- at least, not
        //   one that I found in my admittedly brief time
        //   searching for one
        for (int i=0; i<resultsJSONArray.length(); i++) {
            // Have to check for JSONException from resultsJSONArray.get()
            try {
                // Retrieve object
                Object testObject = resultsJSONArray.get(i);
                // Test the contents
                if (testObject instanceof JSONObject) {
                    // If it's passable, pass it to parseMovieData and add that
                    //  to the results
                    returnList.add(this.parseMovieData((JSONObject) testObject));
                } else {
                    // Otherwise we'll just null-fill that spot since
                    //  we'll be stripping those out before this data
                    //  hits the RecyclerView anyway
                    returnList.add(null);
                }
            } catch (JSONException e) {
                // Again, going to null this out if an error occurs, since
                //  the nulls should only ever appear in some sort of debug
                //  display, if at all
                e.printStackTrace();
                returnList.add(null);
            }
        }
        // Return our populated list
        return returnList;
    }

    // Instance method to fetch the apikey from a
    //  BuildConfig value, which is a better practice
    //  than an ignored XML values resource file.
    //  Seems to work OK, not sure why this concept
    //  was so confusing to me before.
    private String getApiKey() {
        return BuildConfig.api_key;
    }

    // Instance method to parse the JSONObject structure out of
    //  the String that's passed
    private JSONArray parseDataIntoJSON(String data) {
        JSONArray resultsParse = null;
        try {
            JSONObject firstParse = new JSONObject(data);
            if (firstParse.has("results")) {
                Object testObject = firstParse.get("results");
                if (testObject instanceof JSONArray) {
                    resultsParse = (JSONArray) firstParse.get("results");
                } else {
                    return resultsParse;
                }
            } else {
                return resultsParse;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultsParse;
    }

    // Instance method to parse a MovieData object out of
    //  the JSONObject that's passed
    private MovieData parseMovieData(JSONObject inJSON) {
        // Create local fields for the components of a MovieData object
        String poster = null;
        String title = null;
        String synopsis = null;
        String releaseDate = null;
        String rating = null;

        // One-by-one, check the incoming JSON object for the
        //  presence of each key, and assign the value to the
        //  corresponding field
        try {
            if (inJSON.has(JSON_POSTER_PATH_KEY)) {
                poster = inJSON.getString(JSON_POSTER_PATH_KEY);
            }
            if (inJSON.has(JSON_TITLE_KEY)) {
                title = inJSON.getString(JSON_TITLE_KEY);
            }
            if (inJSON.has(JSON_SYNOPSIS_KEY)) {
                synopsis = inJSON.getString(JSON_SYNOPSIS_KEY);
            }
            if (inJSON.has(JSON_RELEASE_DATE_KEY)) {
                releaseDate = inJSON.getString(JSON_RELEASE_DATE_KEY);
            }
            if (inJSON.has(JSON_RATING_KEY)) {
                rating = inJSON.getString(JSON_RATING_KEY) + " / 10";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return new MovieData(title,
                MovieDbHelper.constructImageUri(poster).toString(),
                synopsis,
                rating,
                releaseDate);
    }

}
