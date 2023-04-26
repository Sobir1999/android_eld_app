package com.iosix.eldblesample.retrofit;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.shared_prefs.LastStopSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Optional;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {

    private final TokenServiceHolder tokenServiceHolder;
    private final SessionManager sessionManager;
    private final CompositeDisposable disposables;
    private LoginResponse loginResponse = null;
    private ReplaySubject<Optional<String>> tokenRefreshSubject = ReplaySubject.create();

    private final String time = "" + Calendar.getInstance().getTime();
    private final String today = time.split(" ")[1] + " " + time.split(" ")[2];

    public TokenAuthenticator(TokenServiceHolder tokenServiceHolder, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.tokenServiceHolder = tokenServiceHolder;
        disposables = new CompositeDisposable();
    }

    @Override
    public Request authenticate(Route route, @NonNull Response response) throws IOException {

        if (tokenServiceHolder == null) return null;
        APIInterface service = tokenServiceHolder.get();
        if (service == null) return null;

        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

            // Subscribe to the token refresh subject and initiate a token refresh if necessary
            if (sessionManager.fetchAccessToken() != null) {
                synchronized (this) {
                    if (tokenRefreshSubject.hasObservers()) {
                        // Refresh is already in progress - wait for it to complete
                        return waitForAccessToken(response);
                    } else {
                        // Initiate a new token refresh
                        refreshToken(response);
                        return waitForAccessToken(response);
                    }
                }
            } else {
                // Access token is expired and there's no refresh token available - return null
                return null;
            }
        } else {
            return null;
        }
    }

    @NonNull
    private Request waitForAccessToken(Response response) throws IOException {

        try {
            String accessToken = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                accessToken = tokenRefreshSubject
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .firstOrError()
                        .blockingGet();
            }


            // Retry the original request with the updated Authorization header
            return response.request().newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .build();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private void refreshToken(Response response) {
        String refreshToken = sessionManager.fetchToken();
        if (refreshToken == null) {
            return;
        }

        // Use RxJava to refresh the token
        APIInterface tokenService = tokenServiceHolder.get();
        tokenService.refreshToken(refreshToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        token -> {
                            // Save the new access token and notify the subscribers
                            String newAccessToken = token.getAccessToken();
                            sessionManager.saveAccessToken(newAccessToken);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                tokenRefreshSubject.onNext(Optional.of(newAccessToken));
                            }
                            tokenRefreshSubject.onComplete();
                        },
                        error -> {
                            // Notify the subscribers that token refresh failed


                        });
    }
}
