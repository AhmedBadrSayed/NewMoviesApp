package com.projects.brightcreations.moviesappmvp.screens.movie_details;

import android.content.Context;

import com.projects.brightcreations.moviesappmvp.movie.Movie;
import com.projects.brightcreations.moviesappmvp.local_db.MoviesDB;



/**
 * Created by ahmed on 13/12/17.
 */

public class MoviesDetailsPresenter {

    private MoviesDB moviesDB;

    MoviesDetailsPresenter(Context context) {
        moviesDB = new MoviesDB(context);
    }

    void addMovieToDB(Movie movie, MoviesDetailsListener moviesDetailsListener){
        if(moviesDB.isInDataBase(movie.getMovieTittle())){
            moviesDB.deleteMovie(movie.getMovieTittle());
            moviesDetailsListener.addedToDataBaseSuccess("Removed from favorites");
        } else {
            moviesDB.addMovie(movie.getMovieID(), movie.getMovieTittle(),
                    movie.getMovieOverview(), movie.getMovieVoteAverage(),
                    movie.getMovieDate(), movie.getMoviePoster(),
                    movie.getMovieBackdrop());
            moviesDetailsListener.addedToDataBaseSuccess("Added to favorites");
        }
    }
}
