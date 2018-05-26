package com.example.ne.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ne on 5/6/18.
 */

public class MovieData implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mImageLink);
        dest.writeString(mSynopsis);
        dest.writeString(mUserRating);
        dest.writeString(mReleaseDate);
    }

    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel source) {
            return new MovieData(source.readString(),
                                source.readString(),
                                source.readString(),
                                source.readString(),
                                source.readString());
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[0];
        }
    };

}
