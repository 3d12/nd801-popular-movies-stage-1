package com.example.ne.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ne on 5/6/18.
 */

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieGridViewHolder> {
    // Class-wide reference to the List object that will contain
    //  the data
    private List<MovieData> dataList = null;

    // Capture the calling context from instantiation
    private Context mContext = null;

    // Capture the OnClickListener from instantiation
    private RecyclerView.OnClickListener mOnClickListener = null;

    // Adding a constructor that instantiates with a context and clickListener
    public MoviesAdapter(Context context, RecyclerView.OnClickListener clickListener) {
        this.mContext = context;
        this.mOnClickListener = clickListener;
    }

    // Creating a custom ViewHolder here -- really no different than using
    //  the default RecyclerView.ViewHolder, except that by using a custom
    //  ViewHolder, I can capture package-private references to those views
    //  inside the layout without needing an external context to do so
    //  (that is to say, after they've already been inflated)
    @Override
    public MovieGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        return new MovieGridViewHolder(layoutInflater.inflate(R.layout.movie_list_item, parent, false)) {};
    }

    // Successfully implemented Picasso to load images, as well
    //  as retrieving the title from the MovieData object
    @Override
    public void onBindViewHolder(MovieGridViewHolder holder, int position) {
        if (this.dataList != null) {
            holder.mMovieTitle.setText(this.dataList.get(position).mTitle);

            // Implementing Picasso to load the image from the MovieData object,
            //  per project specs
            Picasso.with(mContext).load(this.dataList.get(position).mImageLink).into(holder.mMoviePoster);
        }
    }

    // Required method, able to implement using size() method
    //  of List object
    @Override
    public int getItemCount() {
        if (this.dataList != null) {
            return this.dataList.size();
        } else {
            return 0;
        }
    }

    // Method to update the dataList with new data when needed
    //  (at first, only one update will be needed, but
    //  could modify this to include refresh functionality
    //  for practice later)
    public void updateDataList(List<MovieData> newList) {
        if (newList != null) {
            this.dataList = newList;
            // Can't believe I missed this at first -- this allows
            //  the adapter to notify the RecyclerView that the
            //  underlying data set may have changed, and to
            //  re-read the appropriate data if needed
            this.notifyDataSetChanged();
        }
    }

    // Creating a custom ViewHolder here, so that I can capture
    //  references to the layout components without needing to
    //  retain the context to do so (that is to say, after they've
    //  already been inflated)
    class MovieGridViewHolder extends RecyclerView.ViewHolder {
        // Class fields to store references to the layout components
        TextView mMovieTitle;
        ImageView mMoviePoster;

        // Constructor which, just like the parent constructor,
        //  accepts a View object, but also stores our references
        //  to the View components and set the OnClickListener
        //  on the inflated view to the listener that was passed
        //  to the adapter's constructor
        MovieGridViewHolder(View v) {
            super(v);
            if (mOnClickListener != null) {
                v.setOnClickListener(mOnClickListener);
            }
            this.mMovieTitle = v.findViewById(R.id.tv_movie_grid_title);
            this.mMoviePoster = v.findViewById(R.id.iv_movie_grid_poster);
        }
    }
}
