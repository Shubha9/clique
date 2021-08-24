package com.us.clique.networkUtils;

/**
 * Created by junaid on 08-Aug-19.
 */


import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static final String BASE_URL = "https://api.thecatapi.com/v1/images/";

    private static Retrofit retrofit = null;
    private static OkHttpClient client = null;


    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .readTimeout(500, TimeUnit.SECONDS)
                .connectTimeout(500, TimeUnit.SECONDS)
                .writeTimeout(500, TimeUnit.SECONDS)// write timeout
                .addInterceptor((Interceptor) new NetworkInterceptor())
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
