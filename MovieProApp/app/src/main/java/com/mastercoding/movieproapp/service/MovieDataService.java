package com.mastercoding.movieproapp.service;

import com.mastercoding.movieproapp.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDataService {

    // Base Url
    // https://api.themoviedb.org/3/
    //
    // End Point Url
    // movie/popular?api_key=890a86f5656fdca2767b6be3222e3526
    @GET("movie/popular")
    Call<Result> getPopularMovies(@Query("api_key") String apiKey);

}
