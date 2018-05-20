package com.example.ne.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Rect;
import android.icu.text.LocaleDisplayNames;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ne.popularmoviesstage1.NetworkUtils.MovieDbHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnClickListener,LoaderManager.LoaderCallbacks<String> {

    // Stuff that's required for the ASyncTaskLoader
    private static final int LOADER_TASK_ID = 12;
    private static final String LOADER_BUNDLE_KEY = "queryType";
    private String mApiStringData;
    private boolean mWaitingForData = true;

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

        // Create some dummy data for testing custom adapter
        List<MovieData> testData = createDummyData();

        // Provide new adapter with data
        mMoviesAdapter.updateDataList(testData);

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

        LoaderManager loaderManager = getLoaderManager();
        Bundle newBundle = new Bundle();
        newBundle.putString(MainActivity.LOADER_BUNDLE_KEY, MovieDbHelper.ENDPOINT_POPULAR);
        loaderManager.initLoader(MainActivity.LOADER_TASK_ID, newBundle, this);

    }

    // Whipped up a quick method to populate a List<MovieData>
    //  for testing that I got the adapter correct at this stage
    private List<MovieData> createDummyData() {
        ArrayList<MovieData> returnArray = new ArrayList<>();
        returnArray.add(new MovieData("Test1", "https://www.funtimepartyhire.com.au/wp-content/uploads/2017/11/hot-dog-06.jpg", null, null, null));
        returnArray.add(new MovieData("Test2", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test3", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test4", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test5", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test6", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test7", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test8", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test9", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test10", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test11", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test12", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test13", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test14", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test15", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test16", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test17", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test18", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test19", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test20", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test21", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test22", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test23", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test24", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test25", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test26", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test27", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        returnArray.add(new MovieData("Test28", "http://i.imgur.com/RgbcXVv.png", null, null, null));
        return returnArray;
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
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        if (id == MainActivity.LOADER_TASK_ID && args.containsKey(MainActivity.LOADER_BUNDLE_KEY)) {
            return new android.content.AsyncTaskLoader<String>(this) {
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
                public String loadInBackground() {
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
    public void onLoadFinished(Loader loader, String data) {
        // Store the data in a member variable
        mApiStringData = data;
        // Trigger relevant actions to occur upon finishing
        // this.testDataReceived();
    }

    // Test function to tell me that the loader finished loading,
    //  and confirm that the API returned data, telling me that
    //  I built and queried the Uri properly
    // Removed now because I'm doing a debug cleanup pass
    //private void testDataReceived() {
        //Toast.makeText(this, "API Data: " + mApiStringData, Toast.LENGTH_LONG).show();
    //}

    // Nothing special with this one, but it's a required implementation
    //  so we'll just leave it blank
    @Override
    public void onLoaderReset(Loader loader) {
    }

    // Found on stack overflow, this is a way to apply a decoration to the recyclerview
    //  and space out the columns. Adapted to suit my purpose.
    class CustomColumnAdapter extends RecyclerView.ItemDecoration {
        // Member variable to store value passed to the constructor
        private int space;

        // Constructor, accepts an integer representing dp(?)
        public CustomColumnAdapter(int space) {
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
