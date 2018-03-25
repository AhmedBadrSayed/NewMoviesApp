package com.projects.brightcreations.moviesappmvp.screens.movies_posters;


/**
 * Created by amr on 06/12/17.
 */

public interface onGetMoviesListener {

    void onGetMoviesSuccess();

    void onGetMoviesFailure(String errorMSg);

    void onGetMoviesFromDBSuccess();

    void onGetMoviesFromDBFailure(String errorMSg);
}
