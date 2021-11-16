package com.iosix.eldblesample.retrofit;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.shared_prefs.SessionManager;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator{

    private Context context;
    private final TokenServiceHolder tokenServiceHolder;

    public TokenAuthenticator(Context context,TokenServiceHolder tokenServiceHolder) {
        this.context = context;
        this.tokenServiceHolder = tokenServiceHolder;
    }

    @Override
    public Request authenticate(Route route, @NonNull Response response) throws IOException {

        if (tokenServiceHolder == null) {
            return null;
        }

        SessionManager sessionManager = new SessionManager(context);
        APIInterface service = tokenServiceHolder.get();

        assert service != null;
        if (sessionManager.fetchToken() != null) {
            retrofit2.Response<ResponseBody> bodyResponse = service.refreshToken(sessionManager.fetchToken()).execute();
            Gson gson = new Gson();
            assert bodyResponse.body() != null;
            LoginResponse loginResponse = gson.fromJson(bodyResponse.body().string(), LoginResponse.class);
            sessionManager.clearAccessToken();
            sessionManager.clearToken();
            sessionManager.saveAccessToken(loginResponse.getAccessToken());
            sessionManager.saveToken(loginResponse.getrefreshToken());
            return response.request().newBuilder()
                    .header("Authorization","Bearer " + loginResponse.getAccessToken())
                .build();
        }
        return null;
    }

}
