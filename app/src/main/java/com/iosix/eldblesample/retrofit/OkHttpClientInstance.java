package com.iosix.eldblesample.retrofit;

import android.content.Context;

import com.iosix.eldblesample.shared_prefs.SessionManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpClientInstance {

    public static class Builder {
        private HashMap<String, String> headers = new HashMap<>();
        private Context context;
        private TokenServiceHolder myServiceHolder;

        public Builder(Context context, TokenServiceHolder myServiceHolder) {
            this.context = context;
            this.myServiceHolder = myServiceHolder;
        }

        public Builder addHeader(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public OkHttpClient build() {
            TokenAuthenticator authenticator = new TokenAuthenticator(context, myServiceHolder);

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                    .addInterceptor(
                            chain -> {
                                // Add default headers
                                Request.Builder requestBuilder = chain.request().newBuilder()
                                        .addHeader("accept", "*/*")
                                        .addHeader("accept-encoding", "gzip, deflate");

                                // Add additional headers
                                Iterator it = headers.entrySet().iterator();

                                for (Map.Entry<String, String> entry : headers.entrySet()) {
                                    if (entry.getKey() != null && entry.getValue() != null) {
                                        requestBuilder.addHeader(entry.getKey(), entry.getValue());
                                    }
                                }

                                SessionManager sessionManager = new SessionManager(context);
                                    if (sessionManager.fetchAccessToken() != null) {
                                        requestBuilder.addHeader("www-authenticate","Bearer realm=" + sessionManager.fetchAccessToken());
                                    }
                                return chain.proceed(requestBuilder.build());
                            }
                    )
                    .connectTimeout(20,TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS);

            okHttpClientBuilder.authenticator(authenticator);

            return okHttpClientBuilder.build();
        }
    }
}
