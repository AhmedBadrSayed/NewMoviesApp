package com.projects.brightcreations.moviesappmvp.webService;


import com.projects.brightcreations.moviesappmvp.Utilities.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ahmed Badr on 14/6/2016.
 */
public class RetrofitBuilder {

    private Retrofit retrofit;

    public RetrofitBuilder(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public ServiceInterfaces.Movies BuildMovies(){
        return retrofit.create(ServiceInterfaces.Movies.class);
    }
}
