package com.example.ne.popularmoviesstage1;

/**
 * Created by ne on 5/6/18.
 */

class MovieData {
    final String mTitle;
    final String mImageLink;
    final String mSynopsis;
    final String mUserRating;
    final String mReleaseDate;

    public MovieData(String title, String imageLink, String synopsis, String userRating, String releaseDate) {
        mTitle = title;
        mImageLink = imageLink;
        mSynopsis = synopsis;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
    }
}
