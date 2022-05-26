package com.iosix.eldblesample.retrofit;

import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;

import android.content.Context;
import android.net.Proxy;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.shared_prefs.LastStopSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {

    private Context context;
    private final TokenServiceHolder tokenServiceHolder;
    private String time = "" + Calendar.getInstance().getTime();
    private String today = time.split(" ")[1] + " " + time.split(" ")[2];

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
        LastStopSharedPrefs lastStopSharedPrefs = new LastStopSharedPrefs(context);
        APIInterface service = tokenServiceHolder.get();

        assert service != null;
        if (sessionManager.fetchToken() != null) {
            retrofit2.Response<ResponseBody> bodyResponse = service.refreshToken(sessionManager.fetchToken()).execute();
            Gson gson = new Gson();
            LoginResponse loginResponse = gson.fromJson(bodyResponse.body().string(), LoginResponse.class);
            sessionManager.saveAccessToken(loginResponse.getAccessToken());
            sessionManager.saveToken(loginResponse.getrefreshToken());
            lastStopSharedPrefs.saveLastStopTime(getCurrentSeconds());
            lastStopSharedPrefs.saveLastStopDate(today);
            return response.request().newBuilder()
                    .header("Authorization","Bearer " + loginResponse.getAccessToken())
                .build();
        }
        return null;
    }

}
