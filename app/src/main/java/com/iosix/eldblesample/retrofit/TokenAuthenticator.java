package com.iosix.eldblesample.retrofit;

import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.shared_prefs.LastStopSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {

    private final LastStopSharedPrefs lastStopSharedPrefs;
    private final TokenServiceHolder tokenServiceHolder;
    private final SessionManager sessionManager;

    private final String time = "" + Calendar.getInstance().getTime();
    private final String today = time.split(" ")[1] + " " + time.split(" ")[2];

    public TokenAuthenticator(LastStopSharedPrefs lastStopSharedPrefs, TokenServiceHolder tokenServiceHolder, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.tokenServiceHolder = tokenServiceHolder;
        this.lastStopSharedPrefs = lastStopSharedPrefs;
    }

    @Override
    public Request authenticate(Route route, @NonNull Response response) throws IOException {

        if (tokenServiceHolder == null) return null;
        APIInterface service = tokenServiceHolder.get();
        if (service == null) return null;


        if (sessionManager.fetchToken() != null) {
            retrofit2.Response<LoginResponse> bodyResponse = service.refreshToken(sessionManager.fetchToken()).execute();
            sessionManager.saveAccessToken(bodyResponse.body().getAccessToken());
            sessionManager.saveToken(bodyResponse.body().getrefreshToken());
            lastStopSharedPrefs.saveLastStopTime(getCurrentSeconds());
            lastStopSharedPrefs.saveLastStopDate(today);

            return response
                    .request()
                    .newBuilder()
                    .header("Authorization", "Bearer " + bodyResponse.body().getAccessToken())
                    .build();
        }
        return null;
    }

}
