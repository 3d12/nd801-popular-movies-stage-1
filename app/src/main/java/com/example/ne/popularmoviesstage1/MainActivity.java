package com.example.ne.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.icu.text.LocaleDisplayNames;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ne.popularmoviesstage1.NetworkUtils.MovieDbHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnClickListener,LoaderManager.LoaderCallbacks<List>,SharedPreferences.OnSharedPreferenceChangeListener {

    // Stuff that's required for the ASyncTaskLoader
    private static final int LOADER_TASK_ID = 12;
    private static final String LOADER_BUNDLE_KEY = "queryType";
    private List<MovieData> mCachedMovieDataList;

    // Activity-wide reference to the MovieDbHelper that will be fetching the data
    MovieDbHelper mMovieDbHelper;

    // Activity-wide reference to the RecyclerView
    RecyclerView mMoviesRecyclerView;

    // Activity-wide reference to the adapter for
    //  the RecyclerView
    MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Look up our RecyclerView by ID
        mMoviesRecyclerView = this.findViewById(R.id.rv_movies_grid);

        // Instantiate new custom adapter
        mMoviesAdapter = new MoviesAdapter(this, this);

        // Set adapter on RecyclerView
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);

        // Get a grid layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);

        // Fetch the value from the resources file to use as the initializing
        //  space value for the ItemDecoration
        int recyclerViewSpacing = getResources().getDimensionPixelSize(R.dimen.RecyclerViewSpacing);

        // Set the ItemDecoration on the RecyclerView
        mMoviesRecyclerView.addItemDecoration(new CustomColumnAdapter(recyclerViewSpacing));

        // Set the layout on the RecyclerView
        mMoviesRecyclerView.setLayoutManager(layoutManager);

        // Testing for appropriate implementation of MovieDbHelper to return API data
        mMovieDbHelper = new MovieDbHelper(this);

        // Get a reference to the LoaderManager
        LoaderManager loaderManager = this.getLoaderManager();
        // Create a new bundle to pass into the loader
        Bundle newBundle = new Bundle();
        // Retrieve a reference to the default Shared Preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Use that reference to retrieve the string of the value of the selected option, or use popular by default
        String endpointPreference = sharedPreferences.getString(getString(R.string.pref_sort_order_key), MovieDbHelper.ENDPOINT_POPULAR);
        // Put whatever value was returned from getString() into the bundle to be passed to the Loader
        newBundle.putString(MainActivity.LOADER_BUNDLE_KEY, endpointPreference);
        // Init the loader with the newly-created bundle
        loaderManager.initLoader(MainActivity.LOADER_TASK_ID, newBundle, this);
    }

    // We override onClick here because we want to handle the clicked item in MainActivity.
    //  We could just as easily make this its own class or part of the Adapter,
    //  but then we would lose the ability to so easily create the explicit
    //  intent we need, or would still need to delegate at least part of the setup
    //  back to MainActivity. So best to do it all here, I think.
    @Override
    public void onClick(View v) {
        // Find the index of the view that was clicked, which is passed here
        int clickedPosition = mMoviesRecyclerView.getChildAdapterPosition(v);

        // Get the MovieData from the List field of the stored reference to
        //  the instantiated adapter
        MovieData clickedMovieData = mMoviesAdapter.dataList.get(clickedPosition);

        // Create a new bundle to store the Parcelable object
        Bundle bundleToPass = new Bundle();
        bundleToPass.putParcelable("MovieData", clickedMovieData);

        // Create a new intent for the DetailActivity class and load it with
        //  the bundle we just created, which should (at this point) contain
        //  a Parcel consisting of the MovieData fields
        Intent intentToStart = new Intent(this, DetailActivity.class);
        intentToStart.putExtra("MOVIE_DETAIL", bundleToPass);

        // Kick it!
        startActivity(intentToStart);
    }

    // This is one of the Loader Callbacks, this is called when a loader is
    //  invoked, and the provided id is passed along with the Bundle args
    @Override
    public Loader<List> onCreateLoader(int id, final Bundle args) {
        if (id == MainActivity.LOADER_TASK_ID && args.containsKey(MainActivity.LOADER_BUNDLE_KEY)) {
            return new android.content.AsyncTaskLoader<List>(this) {
                // Create member field for the queryType
                private String mQueryType;

                // This is where we can put any "setup," for example checking
                //  to see if we've already cached data and stopping the
                //  network call, or instantiating member variables
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    // We've already checked that this key exists in args before this point,
                    //  so there's no need to check again
                    this.mQueryType = args.getString(MainActivity.LOADER_BUNDLE_KEY);
                    // This is required to start loadInBackground(), I think
                    this.forceLoad();
                }

                // Here's the "payload," this is what's done in the background
                @Override
                public List loadInBackground() {
                    // Here's the network call, this has to be done off the main thread
                    return mMovieDbHelper.fetchApiData(this.mQueryType);
                }
            };
        } else {
            // Rather than throw an error, we would rather return a null Loader,
            //  since I don't think that will break the Loader structure
            return null;
        }
    }

    // Required implementation from LoaderCallbacks, this is called
    //  by the loader after loadInBackground finishes
    @Override
    public void onLoadFinished(Loader loader, List data) {
        // Apparently, because there's no way to ensure at runtime
        //  that the List we receive here will contain all instances
        //  of a specific object type before assigning it, we
        //  need to iterate over it and take only the members that
        //  match to the type of object we're expecting so we can
        //  cast without worrying about potential runtime errors
        ArrayList<MovieData> approvedList = new ArrayList<MovieData>();
        for (Object testData : data) {
            if (testData instanceof MovieData) {
                approvedList.add((MovieData) testData);
            }
        }
        // Store the data in a member variable
        this.mCachedMovieDataList = approvedList;
        // Kick off other "triggered" actions from this event
        this.mMoviesAdapter.updateDataList(this.mCachedMovieDataList);
    }

    // Nothing special with this one, but it's a required implementation
    //  so we'll just leave it blank
    @Override
    public void onLoaderReset(Loader loader) {
    }

    // This is the "trigger" called by the shared preferences change, which
    //  causes the Loader to restart with the new parameter and loads that
    //  data into the RecyclerView
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Validate that the value changed is the one we expect
        if (key.equals(getString(R.string.pref_sort_order_key))) {
            // Use the passed reference to SharedPreferences and the provided key
            //  to determine the changed value to provide to the Loader
            String changedPrefValue = sharedPreferences.getString(key, MovieDbHelper.ENDPOINT_POPULAR);

            // Bundle the newly-changed value and pass it to the loader to restart it
            Bundle newBundle = new Bundle();
            newBundle.putString(MainActivity.LOADER_BUNDLE_KEY, changedPrefValue);
            this.getLoaderManager().restartLoader(MainActivity.LOADER_TASK_ID, newBundle, this);
        }
    }

    // This is the override to implement a custom menu into the activity from XML
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Retrieve a reference to the MenuInflater by context
        MenuInflater menuInflater = this.getMenuInflater();
        // Use the MenuInflater to inflate the menu from XML
        menuInflater.inflate(R.menu.menu_main, menu);
        // Return true to signal the menu has been successfully overridden
        return true;
    }

    // This is the override to implement actions to tie to the menu button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Retrieve the ID of the clicked MenuItem
        int itemId = item.getItemId();
        // Compare to the ID of the menu button in the menu XML
        if (itemId == R.id.menubtn_settings) {
            // Create explicit intent for SettingsActivity
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            // Check to make sure we can handle this activity
            // Note, this is pretty redundant when using an explicit intent for
            //  an activity that's part of my own app, but I wanted to make sure
            //  to get into the habit of doing this since it's probably a best
            //  practice
            if (settingsIntent.resolveActivity(this.getPackageManager()) != null) {
                // If so, start the activity
                startActivity(settingsIntent);
            }
            // Regardless, signal that the action has been handled
            return true;
        } else {
            // Otherwise, let the super class handle the selected item
            return super.onOptionsItemSelected(item);
        }
    }

    // Found on stack overflow, this is a way to apply a decoration to the recyclerview
    //  and space out the columns. Adapted to suit my purpose.
    class CustomColumnAdapter extends RecyclerView.ItemDecoration {
        // Member variable to store value passed to the constructor
        private int space;

        // Constructor, accepts an integer representing dp(?)
        CustomColumnAdapter(int space) {
            // Setting member variable
            this.space = space;
        }

        // Overridden from RecyclerView.ItemDecoration, apparently
        //  the outRect is the grid portion (perhaps the "parent"
        //  of the view?) of the RecyclerView and this gets called
        //  for each Rect that's created, to fetch these properties
        //  when creating it in the UI
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            // These are standard public fields of a Rect, which we can override
            //  with the value passed to the constructor
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            //outRect.top = space;
        }
    }
}
