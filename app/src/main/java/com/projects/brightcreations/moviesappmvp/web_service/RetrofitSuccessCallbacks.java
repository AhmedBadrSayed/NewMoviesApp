package com.projects.brightcreations.moviesappmvp.web_service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author ahmed on 26/03/18.
 */

public class RetrofitCallbacks<S> implements Callback<BaseResponse<S>> {


    @Override
    public void onResponse(Call<BaseResponse<S>> call, Response<BaseResponse<S>> response) {

    }

    @Override
    public void onFailure(Call<BaseResponse<S>> call, Throwable t) {

    }
}
