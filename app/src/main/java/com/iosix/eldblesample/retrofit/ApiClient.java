package com.iosix.eldblesample.retrofit;

import android.content.Context;

import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final Context context = BaseActivity.appContext;
    public static final String BASE_URL = "http://logisticadmin.napaautomotive.uz/";
    private static Retrofit retrofit = null;
//    private static final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjI0MDExMjg2LCJqdGkiOiI2MGU2MGFiZDM2MWU0N2Y2YjgyNWVkNzI1NGJiYjQ1ZCIsInVzZXJfaWQiOjl9.yOV4obu7lOXpEZrYgO7MBXRvWZQriK0qX1Gb7FEcXRo";
//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjI0MDExMjg2LCJqdGkiOiI2MGU2MGFiZDM2MWU0N2Y2YjgyNWVkNzI1NGJiYjQ1ZCIsInVzZXJfaWQiOjl9.yOV4obu7lOXpEZrYgO7MBXRvWZQriK0qX1Gb7FEcXRo
    public static Retrofit getClient() {

//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor).build();

        assert context != null;
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    SessionManager sessionManager = new SessionManager(context);
                        Request.Builder newRequest  = chain.request().newBuilder();
                    if (sessionManager.fetchAccessToken() != null){
                                newRequest.addHeader("Authorization", "Bearer " + sessionManager.fetchAccessToken());
                    }
                    return chain.proceed(newRequest.build());
                })
                .addInterceptor(new ChuckInterceptor(context))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
