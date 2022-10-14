package com.iosix.eldblesample.retrofit;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.shared_prefs.SessionManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

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
        // Add default headers
        Request.Builder requestBuilder = chain.request()
                .newBuilder()
                .addHeader("accept", "*/*")
                .addHeader("accept-encoding", "gzip, deflate");

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if (sessionManager.fetchAccessToken() != null) {
            requestBuilder.addHeader("www-authenticate", "Bearer realm=" + sessionManager.fetchAccessToken());
        }

        return chain.proceed(requestBuilder.build());
    }
}
