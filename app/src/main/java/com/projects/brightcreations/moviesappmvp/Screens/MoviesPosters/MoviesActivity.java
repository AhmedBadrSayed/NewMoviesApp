package com.projects.brightcreations.moviesappmvp.Screens.MoviesPosters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.projects.brightcreations.moviesappmvp.R;


public class MoviesActivity extends AppCompatActivity {

    boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        if (findViewById(R.id.movie_details_container)!= null){
            mTwoPane = true;
        }else mTwoPane = false;

    }
}
