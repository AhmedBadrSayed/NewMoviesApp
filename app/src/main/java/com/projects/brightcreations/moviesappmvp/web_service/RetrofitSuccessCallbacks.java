package com.projects.brightcreations.moviesappmvp.web_service;

import android.util.Log;

import com.projects.brightcreations.moviesappmvp.movie.MoviesResponse;
import com.projects.brightcreations.moviesappmvp.movie.Result;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * @author ahmed on 26/03/18.
 */

public class RetrofitSuccessCallbacks implements Consumer<MoviesResponse>{

    private static final String TAG = RetrofitSuccessCallbacks.class.getSimpleName();
    private MoviesResponseSuccessListener listener;


    @Override
    public void accept(@NonNull MoviesResponse moviesResponse) throws Exception {
        if (moviesResponse!=null){
            listener.onGetMoviesResponseSuccess(moviesResponse);
            Log.e(TAG, "Success  "+moviesResponse.getPage());
        }
    }

    public void setMoviesResponseSuccessListener(MoviesResponseSuccessListener listener){
        this.listener = listener;
    }

    public interface MoviesResponseSuccessListener{
        void onGetMoviesResponseSuccess(MoviesResponse response);
    }
}
