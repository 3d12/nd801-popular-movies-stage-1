package com.example.ne.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ne on 5/15/18.
 */

public class DetailActivity extends AppCompatActivity {

    // Need to create a field where we can store the passed
    //  MovieData object we will instantiate from the Parcel
    private MovieData mData = null;

    // Create private fields that will contain references
    //  to the views in the corresponding activity
    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mSynopsis;
    private TextView mReleaseDate;
    private TextView mRating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Set up our references to the IDs in the layout file
        mMoviePoster = this.findViewById(R.id.iv_movie_detail_poster);
        mMovieTitle = this.findViewById(R.id.tv_movie_detail_title);
        mSynopsis = this.findViewById(R.id.tv_movie_detail_synopsis);
        mReleaseDate = this.findViewById(R.id.tv_movie_detail_release_date);
        mRating = this.findViewById(R.id.tv_movie_detail_rating);

        // Capture a local reference to the intent, to fetch
        //  passed extras
        Intent originatingIntent = this.getIntent();

        // Check for the extra we are storing the movie data in
        if (originatingIntent.hasExtra("MOVIE_DETAIL")) {

            // New plan. Instead of trying to create and pass
            //  (and subsequently decode/instantiate) a Parcel
            //  that's passed via extra, I'm going to try passing
            //  the object itself as an instance of a Parcelable
            //  object and hope that the Parcel is created and
            //  handed off solely in the background
            Bundle movieDataBundle = originatingIntent.getBundleExtra("MOVIE_DETAIL");
            mData = movieDataBundle.getParcelable("MovieData");

            // Assign the values from the newly instantiated
            //  Parcelable into the appropriate fields
            Picasso.with(this).load(mData.mImageLink).into(this.mMoviePoster);
            this.mMovieTitle.setText(mData.mTitle);
            this.mSynopsis.setText(mData.mSynopsis);
            this.mReleaseDate.setText(mData.mReleaseDate);
            this.mRating.setText(mData.mUserRating);

        }

    }
}
