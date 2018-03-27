package com.projects.brightcreations.moviesappmvp.web_service;

import android.util.Log;

import com.projects.brightcreations.moviesappmvp.movie.MoviesResponse;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * @author ahmed on 26/03/18.
 */

public class RetrofitErrorCallbacks implements Consumer<Throwable> {

    private static final String TAG = RetrofitErrorCallbacks.class.getSimpleName();
    private MoviesResponseErrorListener listener;

    @Override
    public void accept(@NonNull Throwable throwable) throws Exception {
        listener.onGetMoviesResponseError(throwable.getMessage());
        Log.e(TAG, "Error  "+throwable.getMessage());
    }

    public void setMoviesResponseErrorListener(MoviesResponseErrorListener listener){
        this.listener = listener;
    }

    public interface MoviesResponseErrorListener{
        void onGetMoviesResponseError(String errorMsg);
    }
}
