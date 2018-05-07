package com.example.ne.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Activity-wide reference to the RecyclerView
    RecyclerView mMoviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Look up our RecyclerView by ID
        mMoviesRecyclerView = this.findViewById(R.id.rv_movies_grid);

        // Instantiate new custom adapter
        MoviesAdapter moviesAdapter = new MoviesAdapter(this);

        // Create some dummy data for testing custom adapter
        List<MovieData> testData = createDummyData();

        // Provide new adapter with data
        moviesAdapter.updateDataList(testData);

        // Set adapter on RecyclerView
        mMoviesRecyclerView.setAdapter(moviesAdapter);

        // Get a grid layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);

        // Set the layout on the RecyclerView
        mMoviesRecyclerView.setLayoutManager(layoutManager);
    }

    // Whipped up a quick method to populate a List<MovieData>
    //  for testing that I got the adapter correct at this stage
    private List<MovieData> createDummyData() {
        ArrayList<MovieData> returnArray = new ArrayList<>();
        returnArray.add(new MovieData("Test1", null, null, null, null));
        returnArray.add(new MovieData("Test2", null, null, null, null));
        returnArray.add(new MovieData("Test3", null, null, null, null));
        returnArray.add(new MovieData("Test4", null, null, null, null));
        returnArray.add(new MovieData("Test5", null, null, null, null));
        returnArray.add(new MovieData("Test6", null, null, null, null));
        returnArray.add(new MovieData("Test7", null, null, null, null));
        returnArray.add(new MovieData("Test8", null, null, null, null));
        returnArray.add(new MovieData("Test9", null, null, null, null));
        returnArray.add(new MovieData("Test10", null, null, null, null));
        returnArray.add(new MovieData("Test11", null, null, null, null));
        returnArray.add(new MovieData("Test12", null, null, null, null));
        returnArray.add(new MovieData("Test13", null, null, null, null));
        returnArray.add(new MovieData("Test14", null, null, null, null));
        returnArray.add(new MovieData("Test15", null, null, null, null));
        returnArray.add(new MovieData("Test16", null, null, null, null));
        returnArray.add(new MovieData("Test17", null, null, null, null));
        returnArray.add(new MovieData("Test18", null, null, null, null));
        returnArray.add(new MovieData("Test19", null, null, null, null));
        returnArray.add(new MovieData("Test20", null, null, null, null));
        returnArray.add(new MovieData("Test21", null, null, null, null));
        returnArray.add(new MovieData("Test22", null, null, null, null));
        returnArray.add(new MovieData("Test23", null, null, null, null));
        returnArray.add(new MovieData("Test24", null, null, null, null));
        returnArray.add(new MovieData("Test25", null, null, null, null));
        returnArray.add(new MovieData("Test26", null, null, null, null));
        returnArray.add(new MovieData("Test27", null, null, null, null));
        returnArray.add(new MovieData("Test28", null, null, null, null));
        return returnArray;
    }
}
