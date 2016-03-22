package com.example.beetel.moviesApp;

import com.example.beetel.moviesApp.data.model.MovieResultListModel;
import com.example.beetel.moviesApp.data.model.MovieReviewListModel;
import com.example.beetel.moviesApp.data.model.MovieVideoListModel;

import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import retrofit2.http.GET;

/**
 * Created by beetel on 2/03/2016.
 */
public interface MovieAPI {
@GET("discover/movie")
    Observable<MovieResultListModel>IodeMoviesRx(@Query("sort_by")String sort,@Query("api_key")String apikey);
    @GET("movie/{id}/videos")
    Observable<MovieVideoListModel>getMovieTrailerRx(@Path("id")String id,@Query("api_key")String apikey);
    @GET("movie/{id}/reviews")
    Observable<MovieReviewListModel>getMovieReviewRx(@Path("id")String id,@Query("api_key")String apikey);
}
