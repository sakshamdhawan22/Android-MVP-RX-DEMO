package com.application.saksham.stocktracker.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by Saksham Dhawan on 2/17/18.
 */

public class RetrofitService {


    public static RestApi mRestAPI;
    public static final String BASE_URL = "https://www.alphavantage.co/";

    public static RestApi getInstance() {

        if (mRestAPI != null)
            return mRestAPI;

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient okHttpClient = builder
                .addInterceptor(interceptor)
                .build();


        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.computation());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .client(okHttpClient)
                .build();

        mRestAPI = retrofit.create(RestApi.class);
        return mRestAPI;
    }
}