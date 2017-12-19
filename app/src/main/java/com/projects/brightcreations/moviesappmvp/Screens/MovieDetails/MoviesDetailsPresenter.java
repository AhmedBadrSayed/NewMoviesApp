package com.projects.brightcreations.moviesappmvp.Screens.MovieDetails;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.projects.brightcreations.moviesappmvp.Models.MovieObject;
import com.projects.brightcreations.moviesappmvp.SQLiteDataBase.MoviesDB;



/**
 * Created by ahmed on 13/12/17.
 */

public class MoviesDetailsPresenter {

    private MoviesDB moviesDB;

    MoviesDetailsPresenter(Context context) {
        moviesDB = new MoviesDB(context);
    }

    void addMovieToDB(MovieObject movieObject, MoviesDetailsListener moviesDetailsListener){
        if(moviesDB.isInDataBase(movieObject.getMovieTittle())){
            moviesDB.deleteMovie(movieObject.getMovieTittle());
            moviesDetailsListener.addedToDataBaseSuccess("Removed from favorites");
        } else {
            moviesDB.addMovie(movieObject.getMovieID(), movieObject.getMovieTittle(),
                    movieObject.getMovieOverview(), movieObject.getMovieVoteAverage(),
                    movieObject.getMovieDate(), movieObject.getMoviePoster(),
                    movieObject.getMovieBackdrop());
            moviesDetailsListener.addedToDataBaseSuccess("Added to favorites");
        }
    }
}
