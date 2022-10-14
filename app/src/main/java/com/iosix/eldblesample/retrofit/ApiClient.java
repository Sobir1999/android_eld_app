package com.iosix.eldblesample.retrofit;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.iosix.eldblesample.MyApplication;
import com.iosix.eldblesample.shared_prefs.LastStopSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldbluetooth.BuildConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final Object LOCK = new Object();

    public static final String BASE_URL = "https://fastlogz.herokuapp.com/";
    private static volatile Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            synchronized (LOCK) {
                if (retrofit == null) {
                    retrofit = creteInstance();
                }
            }
        }

        return retrofit;
    }

    private static Retrofit creteInstance() {
        final Context context = MyApplication.instance;

        SessionManager sessionManager = SessionManager.getInstance(context);
        LastStopSharedPrefs lastStopSharedPrefs = LastStopSharedPrefs.getInstance(context);

        TokenServiceHolder myServiceHolder = new TokenServiceHolder();
        TokenAuthenticator authenticator = new TokenAuthenticator(lastStopSharedPrefs, myServiceHolder, sessionManager);

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
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

        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(createChucker(context));
        }

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
