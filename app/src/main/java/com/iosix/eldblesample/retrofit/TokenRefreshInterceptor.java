package com.iosix.eldblesample.retrofit;

import android.os.Build;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.shared_prefs.SessionManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TokenRefreshInterceptor implements Interceptor {

    private final Map<String, String> headers;
    private final SessionManager sessionManager;

    public TokenRefreshInterceptor(
            @NonNull Map<String, String> headers,
            @NonNull SessionManager sessionManager
    ) {
        this.headers = headers;
        this.sessionManager = sessionManager;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        final Request request = chain.request();
        final Response response = chain.proceed(request);
        final ResponseBody responseBody = response.body();
        // Add default headers

        Request.Builder requestBuilder = chain.request()
                .newBuilder();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if (sessionManager.fetchAccessToken() != null) {
            requestBuilder.addHeader("www-authenticate", "Bearer realm=" + sessionManager.fetchAccessToken());
        }
        response.close();

        return chain.proceed(requestBuilder.build());
    }
}
