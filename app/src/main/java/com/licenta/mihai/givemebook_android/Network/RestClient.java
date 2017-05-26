package com.licenta.mihai.givemebook_android.Network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mihai on 23.05.2017.
 */

public class RestClient {

    private static String BASE_URL = "http://192.168.100.217:8080/";
    private static API REST_CLIENT;

    static {
        setupRestClient();
    }

    public static API networkHandler() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        builder.networkInterceptors().add(httpLoggingInterceptor);
        OkHttpClient okHttpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        REST_CLIENT = retrofit.create(API.class);
    }
}
