package com.projects.brightcreations.moviesappmvp.webService;


import com.projects.brightcreations.moviesappmvp.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ahmed Badr on 14/6/2016.
 */
public class RestClient {

    private Retrofit retrofit;

    public RestClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <S> S createService(Class<S> serviceInterface){
        return retrofit.create(serviceInterface);
    }
}
