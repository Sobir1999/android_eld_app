package com.iosix.eldblesample.retrofit;

import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.shared_prefs.LastStopSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.io.IOException;
import java.util.Calendar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {

    private final LastStopSharedPrefs lastStopSharedPrefs;
    private final TokenServiceHolder tokenServiceHolder;
    private final SessionManager sessionManager;
    private final CompositeDisposable disposables;
    private LoginResponse loginResponse = null;

    private final String time = "" + Calendar.getInstance().getTime();
    private final String today = time.split(" ")[1] + " " + time.split(" ")[2];

    public TokenAuthenticator(LastStopSharedPrefs lastStopSharedPrefs, TokenServiceHolder tokenServiceHolder, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.tokenServiceHolder = tokenServiceHolder;
        this.lastStopSharedPrefs = lastStopSharedPrefs;
        disposables = new CompositeDisposable();
    }

    @Override
    public Request authenticate(Route route, @NonNull Response response) throws IOException {

        if (tokenServiceHolder == null) return null;
        APIInterface service = tokenServiceHolder.get();
        if (service == null) return null;


        if (sessionManager.fetchToken() != null) {
            Disposable disposable = service
                    .refreshToken(sessionManager.fetchToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s ->{
                        loginResponse = s;
                        sessionManager.saveAccessToken(s.getAccessToken());
                        sessionManager.saveToken(s.getrefreshToken());
                        lastStopSharedPrefs.saveLastStopTime(getCurrentSeconds());
                        lastStopSharedPrefs.saveLastStopDate(today);
                    }, throwable -> {

                    });

            disposables.add(disposable);


            return response
                    .request()
                    .newBuilder()
                    .header("Authorization", "Bearer " + loginResponse.getAccessToken())
                    .build();
        }
        return null;
    }

}
