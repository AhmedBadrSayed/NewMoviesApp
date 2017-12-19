package com.projects.brightcreations.moviesappmvp.Screens.MovieDetails;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.projects.brightcreations.moviesappmvp.Models.MovieObject;
import com.projects.brightcreations.moviesappmvp.R;
import com.projects.brightcreations.moviesappmvp.SQLiteDataBase.MoviesDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
    @BindView(R.id.movie_description)
    TextView overview;
    @BindView(R.id.movie_vote)
    TextView vote;
    @BindView(R.id.movie_date)
    TextView date;
    private String MovieDetails, PosterPath, MovieTitle, Overview, VoteAverage, Date, ID;
    private MovieObject movieObject;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    public static MovieDetailsFragment newInstance() {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this,rootview);
        Bundle extra = getActivity().getIntent().getExtras();
        if (extra != null) {
            movieObject = extra.getParcelable("MovieDetails");
            viewAdapter(movieObject);
//            Log.e("CustomerWalletDetails", totalcustomerwallet.getRebate().get(0).getPointTotal());
        }

        return rootview;
    }

    public void viewAdapter(MovieObject mMovie) {
        ID = mMovie.getMovieID();
        MovieTitle = mMovie.getMovieTittle();
        Overview = mMovie.getMovieOverview();
        VoteAverage = mMovie.getMovieVoteAverage();
        Date = mMovie.getMovieDate();
        PosterPath = mMovie.getMoviePoster();
        overview.setText(Overview);
        vote.setText(VoteAverage+"/10");
        date.setText(Date);
    }
}
