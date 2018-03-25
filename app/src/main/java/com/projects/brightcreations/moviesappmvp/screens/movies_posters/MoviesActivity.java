package com.projects.brightcreations.moviesappmvp.screens.movies_posters;

import android.support.v7.app.AppCompatActivity;

import com.projects.brightcreations.moviesappmvp.R;
import com.projects.brightcreations.moviesappmvp.interfaces.ActivityController;


public class MoviesActivity extends AppCompatActivity implements ActivityController {

    @Override
    public void init() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_movies;
    }
}
