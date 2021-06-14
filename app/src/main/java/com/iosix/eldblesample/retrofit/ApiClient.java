package com.iosix.eldblesample.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://fastlogz.napaautomotive.uz/backend/";
    private static Retrofit retrofit = null;
    private static final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjI0MDExMjg2LCJqdGkiOiI2MGU2MGFiZDM2MWU0N2Y2YjgyNWVkNzI1NGJiYjQ1ZCIsInVzZXJfaWQiOjl9.yOV4obu7lOXpEZrYgO7MBXRvWZQriK0qX1Gb7FEcXRo";
//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjI0MDExMjg2LCJqdGkiOiI2MGU2MGFiZDM2MWU0N2Y2YjgyNWVkNzI1NGJiYjQ1ZCIsInVzZXJfaWQiOjl9.yOV4obu7lOXpEZrYgO7MBXRvWZQriK0qX1Gb7FEcXRo
    public static Retrofit getClient() {

//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor).build();

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
