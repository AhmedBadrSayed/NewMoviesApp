package com.projects.brightcreations.moviesappmvp.Screens.MovieDetails;

/**
 * Created by ahmed on 13/12/17.
 */

public interface MoviesDetailsListener {

    void addedToDataBaseSuccess(String successMsg);

    void addedToDataBaseFailure(String errorMsg);
}
