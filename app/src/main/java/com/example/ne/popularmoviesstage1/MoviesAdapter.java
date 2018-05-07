package com.example.ne.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    // Adding a constructor that instantiates with a context
    public MoviesAdapter(Context context) {
        this.mContext = context;
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

    // First implementation will be to successfully retrieve the title
    //  as a string from the MovieData object and use that to populate
    //  the title section of the inflated view... Once I have the
    //  adapter working, I can work on using Picasso to populate
    //  the image, per the guidance provided for this project
    @Override
    public void onBindViewHolder(MovieGridViewHolder holder, int position) {
        if (this.dataList != null) {
            holder.mMovieTitle.setText(this.dataList.get(position).mTitle);
            holder.mMoviePoster.setImageResource(R.drawable.ic_launcher_foreground);
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
        //  to the View components
        MovieGridViewHolder(View v) {
            super(v);
            this.mMovieTitle = v.findViewById(R.id.tv_movie_grid_title);
            this.mMoviePoster = v.findViewById(R.id.iv_movie_grid_poster);
        }
    }
}
