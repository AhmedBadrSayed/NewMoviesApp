package com.projects.brightcreations.moviesappmvp.web_service;

import com.projects.brightcreations.moviesappmvp.movie.MoviesResponse;
import com.projects.brightcreations.moviesappmvp.movie.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ahmed Badr on 14/6/2016.
 */
public interface ApiInterfaces {

    @GET("movie/{SortType}")
    Observable<PaginatedResponse<Result>> getMovies(
            @Path("SortType") String SortBy,
            @Query("api_key") String API_KEY,
            @Query("page") int page
    );

}
