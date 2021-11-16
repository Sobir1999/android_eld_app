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
     public static final String BASE_URL = "https://shaxbozaka-logistic.herokuapp.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        TokenServiceHolder myServiceHolder = new TokenServiceHolder();
        SessionManager sessionManager = new SessionManager(context);
        OkHttpClient okHttpClient;

        if (sessionManager.fetchAccessToken() != null){
            okHttpClient = new OkHttpClientInstance.Builder(context, myServiceHolder)
                .addHeader("Authorization", "Bearer " + sessionManager.fetchAccessToken())
                .build();
        }else {
            okHttpClient = new OkHttpClientInstance.Builder(context, myServiceHolder)
                    .build();
        }

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        return retrofit;
    }
}
