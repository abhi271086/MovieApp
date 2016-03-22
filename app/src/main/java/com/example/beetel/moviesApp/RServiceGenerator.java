package com.example.beetel.moviesApp;

import com.example.beetel.moviesApp.utilities.MovieAPIUtility;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by beetel on 12/03/2016.
 */
public class RServiceGenerator {
    private static OkHttpClient httpClient=new OkHttpClient();
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(MovieAPIUtility.API_BASE_URL)
             .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create());



    public static <C> C createService(Class<C> serviceClass) {

        Retrofit retrofit = builder
                .client(httpClient)
                .build();
        return retrofit.create(serviceClass);
    }
}
