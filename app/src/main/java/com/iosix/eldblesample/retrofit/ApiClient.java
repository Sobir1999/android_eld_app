package com.iosix.eldblesample.retrofit;

import android.content.Context;
import android.util.Log;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.iosix.eldblesample.MyApplication;
import com.iosix.eldblesample.shared_prefs.LastStopSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final Object LOCK = new Object();

    public static final String BASE_URL = "https://api.v2.fastlogz.com/";
    private volatile Retrofit retrofit = null;

    public Retrofit getClient() {
        if (retrofit == null) {
            synchronized (LOCK) {
                if (retrofit == null) {
                    retrofit = creteInstance();
                }
            }
        }

        return retrofit;
    }

    private Retrofit creteInstance() {
        final Context context = MyApplication.instance;

        SessionManager sessionManager = SessionManager.getInstance(context);

        TokenServiceHolder myServiceHolder = new TokenServiceHolder();
        TokenAuthenticator authenticator = new TokenAuthenticator(myServiceHolder, sessionManager);

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .authenticator(authenticator);

        if (sessionManager.fetchAccessToken() != null) {
            Map<String, String> authHeader = new HashMap<>();
            authHeader.put("Authorization", "Bearer " + sessionManager.fetchAccessToken());

            TokenRefreshInterceptor tokenRefreshInterceptor = new TokenRefreshInterceptor(
                    authHeader,
                    sessionManager
            );

            okHttpBuilder.addInterceptor(tokenRefreshInterceptor);
        }

//        if (com.iosix.eldblesample.BuildConfig.DEBUG) {
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
//            });
//            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
//            okHttpBuilder.addInterceptor(createChucker(context));
//            okHttpBuilder.addInterceptor(loggingInterceptor);
//        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpBuilder.build())
                .build();

        APIInterface apiInterface = retrofit.create(APIInterface.class);
        myServiceHolder.set(apiInterface);

        return retrofit;
    }

    private static Interceptor createChucker(Context context) {
        return new ChuckerInterceptor.Builder(context)
                .maxContentLength(250_000L)
                .build();
    }
}
